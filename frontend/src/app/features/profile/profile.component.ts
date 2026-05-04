import { EmployeeRes } from './../../models/employee.model';
import { catchError, finalize, forkJoin, of, retry, switchMap } from 'rxjs';
import { DatePipe } from '@angular/common';
import { Component, computed, inject, input, signal } from '@angular/core';
import { EmployeeService } from '../employee/employee.service';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-profile',
  imports: [DatePipe],
  providers: [EmployeeService],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent {

  // inject
  private readonly employeeService = inject(EmployeeService);

  // input & output
  readonly employeeId = input.required<number>();

  // state
  readonly dataLoader = signal(false);

  // data fetching
  private readonly _employeeId$ = toObservable(this.employeeId);

   private readonly profileData$ = this._employeeId$.pipe(
     switchMap(id => forkJoin({
       employee: this.employeeService.getEmployeeById(id),
     }).pipe(
       catchError((error) => {
         console.error('API Error:', error);
         return of(null);
       }),
       finalize(() => this.dataLoader.set(true))
     ))
   );


  // convert observable to signal for easier template usage
  private readonly profileData = toSignal(this.profileData$);

  // derived state
  readonly employee = computed(() => this.profileData()?.employee);

  // readonly employee = {
  //   "id": "EMP-9582",
  //   "fullName": "Abdur Rahman",
  //   "designation": "Senior Backend Developer",
  //   "department": "Engineering",
  //   "email": "a.rahman@company.com",
  //   "joinedDate": "2022-03-15",
  //   "manager": "Faisal Khan",
  //   "location": "Remote / New York Hub",
  //   "avatarUrl": "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=150&h=150"
  // }


}
