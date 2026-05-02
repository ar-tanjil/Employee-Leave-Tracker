import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AUTH_STORAGE_KEY, AuthService } from '../../../../core/services/auth.service';
import { CommonModule } from '@angular/common';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { UserLogin } from '../../../../models/auth.model';
import { environment } from '../../../../../environments/environment';

@Component({
  selector: 'ims-login',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent implements OnInit {

  readonly appName = environment.appName || 'Leave Tracker';

  private readonly auth = inject(AuthService);
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);


  form!: FormGroup;
  ngOnInit(): void {

    localStorage.clear();

    this.form = this.fb.group({
      username: ['admin', [Validators.required, Validators.minLength(3)]],
      password: ['admin@123#', [Validators.required, Validators.minLength(6)]],
    });
  }

  isInvalid(field: string) {
    const ctrl: AbstractControl | null = this.form.get(field);
    return !!(ctrl?.invalid && (ctrl.dirty || ctrl.touched));
  }

  isRequired(field: string) {
    const ctrl: AbstractControl | null = this.form.get(field);
    return ctrl?.hasValidator(Validators.required);
  }

  login(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const formData: UserLogin = this.form.getRawValue();
    this.auth.login(formData);
  }

  register(): void {
    this.router.navigate(['/auth/register']);
  }
}
