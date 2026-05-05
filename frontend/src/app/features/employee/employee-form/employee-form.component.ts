import { Component, computed, effect, inject, input, OnInit, output, signal } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { EmployeeService } from '../employee.service';
import { CommonApiService } from '../../../shared/services/common-api.service';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { catchError, finalize, forkJoin, of, switchMap } from 'rxjs';
import { DialogComponent } from "../../../shared/components/dialog/dialog.component";

@Component({
  selector: 'app-employee-form',
  imports: [ReactiveFormsModule, DialogComponent],
  templateUrl: './employee-form.component.html',
  styleUrl: './employee-form.component.css',
})
export class EmployeeFormComponent {

  // inject services
  private readonly fb = inject(FormBuilder);
  private readonly employeeService = inject(EmployeeService);
  private readonly commonApiService = inject(CommonApiService);


  // input & output
  readonly employeeId = input<number | null>(null);
  readonly onSaved = output<void>();
  readonly onClose = output<void>();


  // form data
  employeeForm!: FormGroup;
  selectedFile!: File;
  imageUrl = signal('');
  readonly isEditing = computed(() => !!this.employeeId());

  // data fetching
  private readonly _employeId$ = toObservable(this.employeeId);

  private readonly dataLoder$ = this._employeId$.pipe(
    switchMap(id => forkJoin({
      employee: id ? this.employeeService.getEmployeeById(id) : of(null),
      departmentList: this.commonApiService.getDepartmentList(),
      designationList: this.commonApiService.getDesignationList()
    }).pipe(
      catchError((error) => {
        console.error('API Error:', error);
        return of(null);
      })
    ))
  );

  // covert to signal
  readonly data = toSignal(this.dataLoder$, { initialValue: undefined });

  // data
  readonly isDataLoaded = computed(() => this.data() !== undefined);
  readonly employee = computed(() => this.data()?.employee);
  readonly departments = computed(() => this.data()?.departmentList);
  readonly designations = computed(() => this.data()?.designationList);


  constructor() {
    this.initForm();

    // The "Auto-Fill" Logic
    effect(() => {
      const emp = this.employee();

      if (emp) {
        // Logic for EDIT mode
        this.employeeForm.patchValue(emp);

        if (this.isEditing()) {
          this.employeeForm.get('email')?.disable();
          this.employeeForm.get('departmentId')?.disable();
          this.employeeForm.get('designationId')?.disable();
          this.employeeForm.get('hireDate')?.disable();
          this.employeeForm.get('employmentType')?.disable();
        }

        const image = emp?.image;
        if (image) {
          this.imageUrl.set(image);
        }

      } else if (this.isDataLoaded() && !this.employeeId()) {
        // Logic for CREATE mode
        this.employeeForm.reset({
          employmentType: 'Full_Time',
          hireDate: new Date()
        });
      }
    });
  }

  private initForm(): void {

    this.employeeForm = this.fb.group({
      id: [null], // Optional, usually hidden or handled by backend
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      departmentId: [null, [Validators.required]],
      designationId: [null, [Validators.required]],
      hireDate: [new Date(), [Validators.required]],
      employmentType: ['Full-Time', [Validators.required]],
      address: ['', [Validators.required]],
      image: [null, [Validators.required]]
    });
  }


  isInvalid(field: string): boolean {
    const ctrl: AbstractControl | null = this.employeeForm.get(field);
    return !!(ctrl?.invalid && (ctrl.dirty || ctrl.touched));
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const file = input.files[0];
      this.selectedFile = file;
      this.employeeForm.patchValue({ image: file });

      // Create a local URL for previewing
      const reader = new FileReader();
      reader.onload = () => (this.imageUrl.set(reader.result as string));
      reader.readAsDataURL(file);
    }
  }

  onSubmit(): void {
    if (this.employeeForm.invalid) {
      this.employeeForm.markAllAsTouched();
      return;
    }

    // Create FormData instance
    const formData = new FormData();

    // Append all form fields
    const formRawValue = this.employeeForm.getRawValue();
    Object.keys(formRawValue).forEach(key => {
      if (key !== 'image' && formRawValue[key] !== null) {
        formData.append(key, formRawValue[key]);
      }
    });

    // Append the file specifically
    if (this.selectedFile) {
      formData.append('image', this.selectedFile, this.selectedFile.name);
    }

    // Handle Edit ID logic
    const emp = this.employee();
    if (emp?.id) {
      formData.append('id', emp.id.toString());
    }

    // Send the request
    this.employeeService.saveAndUpdateEmployee(formData).subscribe({
      next: () => {
        this.onSaved.emit();
      },
      error: () => {
      }
    });
  }


  // loockup data
  readonly employmentTypes = [
    { key: 'FULL_TIME', value: 'Full Time' },
    { key: 'PROVISION', value: 'Provision' },
  ];

}
