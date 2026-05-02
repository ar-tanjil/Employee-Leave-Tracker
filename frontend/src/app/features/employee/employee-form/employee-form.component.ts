import { Component, computed, inject, input, OnInit, output, signal } from '@angular/core';
import { DialogComponent } from "../../../shared/components/dialog/dialog.component";
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { EmployeeService } from '../employee.service';
import { CommonApiService } from '../../../shared/services/common-api.service';
import { IDName } from '../../../models/common.model';
import { EmployeeReq, EmployeeRes } from '../../../models/employee.model';
import { LoadingService } from '../../../core/services/loading.service';

@Component({
  selector: 'app-employee-form',
  imports: [DialogComponent, ReactiveFormsModule],
  templateUrl: './employee-form.component.html',
  styleUrl: './employee-form.component.css',
})
export class EmployeeFormComponent implements OnInit {

  // inject services
  private readonly fb = inject(FormBuilder);
  private readonly employeeService = inject(EmployeeService);
  private readonly commonApiService = inject(CommonApiService);

  // input
  employee = input<EmployeeRes | null>(null);

  // output
  readonly onSaved = output<void>();
  readonly onCancelled = output<void>();

  // data
  employeeForm!: FormGroup;


  readonly isSaving = signal(false);
  readonly departments = signal<IDName[]>([]);
  readonly designations = signal<IDName[]>([]);

  readonly isEditing = computed(() => !!this.employee()?.id);


  ngOnInit(): void {
    this.initForm();

    if (this.employee()) {
      this.employeeForm.patchValue(this.employee()!);
    }

    // load lookup data
    this.commonApiService.getDepartmentList().subscribe(res => {
      // If your API wraps the data, you might need res.data or res.departments
      console.log('Departments loaded:', res);
      this.departments.set(res || []);
    });

    this.commonApiService.getDesignationList().subscribe({
      next: (data) => this.designations.set(data),
      error: (err) => console.error('Failed to load designations', err)
    });

  }

  private initForm(): void {
    this.employeeForm = this.fb.group({
      id: [null], // Optional, usually hidden or handled by backend
      employeeCode: ['', [Validators.required]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      departmentId: [null, [Validators.required]],
      designationId: [null, [Validators.required]],
      managerId: [null],
      hireDate: [new Date(), [Validators.required]],
      employmentType: ['Full-Time', [Validators.required]]
    });
  }


  isInvalid(field: string): boolean {
    const ctrl: AbstractControl | null = this.employeeForm.get(field);
    return !!(ctrl?.invalid && (ctrl.dirty || ctrl.touched));
  }

  onSubmit(): void {
    if (this.employeeForm.invalid) {
      this.employeeForm.markAllAsTouched();
      return;
    }

    this.isSaving.set(true);
    const formValue = this.employeeForm.getRawValue();
    const emp = this.employee();

    const request$ = emp && emp.id
      ? this.employeeService.saveAndUpdateEmployee({ id: emp.id, ...formValue } as EmployeeReq)
      : this.employeeService.saveAndUpdateEmployee(formValue as EmployeeReq);

    request$.subscribe({
      next: () => {
        this.isSaving.set(false);
        this.onSaved.emit();
      },
      error: () => {
        // Error interceptor already showed the toast
        this.isSaving.set(false);
      }
    })
  }

  // loockup data
  readonly employmentTypes = [
    { key: 'FULL_TIME', value: 'Full Time' },
    { key: 'PART_TIME', value: 'Part Time' },
    { key: 'CONTRACT', value: 'Contract' }
  ];

}
