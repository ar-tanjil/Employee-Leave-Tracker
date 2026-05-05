import { Component, computed, inject, signal } from '@angular/core';
import { DashboardService } from './dashboard.service';
import { catchError, forkJoin, of } from 'rxjs';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent {

  // inject
  private readonly dashboardService = inject(DashboardService);


  // state
  currentDate = signal(new Date().toLocaleDateString('en-US', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  }));

  private readonly DEFAULT_DASHBOARD = {
    totalEmployees: "-",
    employeesOnLeave: "-",
    employeeAttendance: "-"
  };

  // data fetching

  private readonly data$ = forkJoin({
    dashboard: this.dashboardService.getDashboardData(),
  }).pipe(
    catchError((error) => {
      console.error('API Error:', error);
      return of(null);
    })
  );


  // convert observable to signal for easier template usage
  private readonly data = toSignal(this.data$, { initialValue: undefined });

  // derived state
  readonly dashboard = computed(() => this.data()?.dashboard ?? this.DEFAULT_DASHBOARD);
  readonly isLoading = computed(() => this.data() === undefined);



}
