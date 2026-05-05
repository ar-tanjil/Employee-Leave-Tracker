import { EmployeeRes } from './../../models/employee.model';
import { catchError, finalize, forkJoin, of, retry, switchMap } from 'rxjs';
import { DatePipe } from '@angular/common';
import { Component, computed, inject, input, signal } from '@angular/core';
import { EmployeeService } from '../employee/employee.service';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { ProfileService } from './profile.service';
import { ChangePassword } from './change-password/change-password.component';


@Component({
  selector: 'app-profile',
  imports: [DatePipe, ChangePassword],
  providers: [ProfileService],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent {

  // inject
  private readonly profileService = inject(ProfileService);

  // state
  readonly dataLoader = signal(false);
  readonly showPasswordForm = signal(false);

  // data fetching

  private readonly profileData$ = forkJoin({
    employee: this.profileService.getUserProfile(),
  }).pipe(
    catchError((error) => {
      console.error('API Error:', error);
      return of(null);
    })
  );


  // convert observable to signal for easier template usage
  private readonly profileData = toSignal(this.profileData$, { initialValue: undefined });

  // derived state
  readonly employee = computed(() => this.profileData()?.employee);
  readonly isLoading = computed(() => this.profileData() === undefined);



}
