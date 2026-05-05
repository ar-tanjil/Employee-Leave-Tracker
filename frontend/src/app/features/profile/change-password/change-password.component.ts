import { Component, inject, output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DialogComponent } from "../../../shared/components/dialog/dialog.component";
import { ProfileService } from '../profile.service';

@Component({
  selector: 'app-change-password',
  imports: [ReactiveFormsModule, DialogComponent],
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.css',
})
export class ChangePassword {

  private readonly fb = inject(FormBuilder);
  private readonly profileService = inject(ProfileService);

  // output
  readonly onSubmit = output<void>();
  readonly onClose = output<void>();

  passwordForm!: FormGroup;

  ngOnInit(): void {
    this.initForm();
  }
  private initForm(): void {
    this.passwordForm = this.fb.group({
      oldPassword: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.minLength(5)]],
      confirmPassword: ['', [Validators.required]]
    }, {
      validators: this.passwordMatchValidator
    });
  }

  // Custom validator to check if passwords match
  passwordMatchValidator(g: FormGroup) {
    const newPass = g.get('newPassword')?.value;
    const confirmPass = g.get('confirmPassword')?.value;
    return newPass === confirmPass ? null : { mismatch: true };
  }

  updatePassword() {
    if (this.passwordForm.valid) {
      const { oldPassword, newPassword } = this.passwordForm.getRawValue();

      this.profileService.changePassword({ oldPassword, newPassword }).subscribe({
        next: () => this.onSubmit.emit(),
        error: (err) => console.error('Save failed', err)
      })
    }
  }

}
