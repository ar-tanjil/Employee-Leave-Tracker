import { Component, inject, OnInit, output, signal } from '@angular/core';
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

  // output
  readonly onSaved = output<void>();
  readonly onCancelled = output<void>();

  // form data
  isSaving = signal(false);
  leaveForm!: FormGroup;

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {
    this.leaveForm = this.fb.group(
      {
        startDate: [null, [Validators.required]],
        endDate: [null, [Validators.required]],
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

    this.isSaving.set(true);
    const formValue = this.leaveForm.getRawValue();

    this.leaveService.saveLeave(formValue as LeaveReq).subscribe({
      next: () => {
        this.isSaving.set(false);
        this.onSaved.emit();
      },
      error: () => {
        // Error interceptor already showed the toast
        this.isSaving.set(false);
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
