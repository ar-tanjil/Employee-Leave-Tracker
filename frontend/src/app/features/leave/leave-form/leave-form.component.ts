import { Component, computed, inject, OnInit, output, signal } from '@angular/core';
import { LeaveService } from '../leave.service';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CustomValidators } from '../../../shared/validators/custom-validators';
import { LeaveReq, LeaveDuration } from '../../../models/leave.models';
import { DialogComponent } from '../../../shared/components/dialog/dialog.component';
import { ValidationErrorMessagePipe } from '../../../shared/pipe/validation-error-message.pipe';
import { CharCountPipe } from '../../../shared/pipe/char-count.pipe';
import { catchError, forkJoin, of } from 'rxjs';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-leave-form',
  imports: [ReactiveFormsModule, DialogComponent, ValidationErrorMessagePipe, CharCountPipe],
  templateUrl: './leave-form.component.html',
  styleUrl: './leave-form.component.css',
})
export class LeaveFormComponent implements OnInit {
  // inject
  private readonly fb = inject(FormBuilder);
  protected readonly leaveService = inject(LeaveService);

  // input & output
  readonly onSaved = output<void>();
  readonly onCancelled = output<void>();

  // form data
  leaveForm!: FormGroup;

  // data fetching
  private readonly data$ = forkJoin({
    leaveType: this.leaveService.getLeaveTypes()
    })
    .pipe(
      catchError((error) => {
        console.error('API Error:', error);
        return of(null);
      })
    );
  readonly data = toSignal(this.data$, {initialValue: undefined});

  // derived state
  readonly leaveType = computed(() => this.data()?.leaveType);
  readonly isLoading = computed(() => this.data() === undefined)



  ngOnInit(): void {
    this.initForm();
  }

  initForm() {
    this.leaveForm = this.fb.group(
      {
        startDate: [null, [Validators.required]],
        endDate: [null, [Validators.required]],
        leaveTypeId: [null, [Validators.required]],
        duration: [LeaveDuration.FULL_DAY, [Validators.required]],
        reason: ['', [Validators.required, Validators.maxLength(255)]],
      },
      {
        validators: [CustomValidators.dateRange('startDate', 'endDate')],
      },
    );
  }

  isInvalid(field: string): boolean {
    const ctrl: AbstractControl | null = this.leaveForm.get(field);
    return !!(ctrl?.invalid && (ctrl.dirty || ctrl.touched));
  }

  onSubmit(): void {
    if (this.leaveForm.invalid) {
      this.leaveForm.markAllAsTouched();
      return;
    }
    const formValue = this.leaveForm.getRawValue();

    this.leaveService.saveLeave(formValue as LeaveReq).subscribe({
      next: () => {
        this.onSaved.emit();
      },
      error: () => {
        // Error interceptor already showed the toast
      },
    });
  }

  // LOOKUP DATA
  readonly leaveDurationType = [
    { key: LeaveDuration.FULL_DAY, value: 'Full Day' },
    { key: LeaveDuration.FIRST_HALF, value: 'First Half' },
    { key: LeaveDuration.SECOND_HALF, value: 'Second Half' },
  ];
}
