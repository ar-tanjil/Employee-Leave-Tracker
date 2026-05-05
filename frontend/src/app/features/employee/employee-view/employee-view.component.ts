import { Component, computed, inject, input, OnInit, output, signal } from '@angular/core';
import { DialogComponent } from "../../../shared/components/dialog/dialog.component";
import { DatePipe } from '@angular/common';
import { EmployeeService } from '../employee.service';
import { rxResource, toObservable, toSignal } from '@angular/core/rxjs-interop';
import { catchError, of, switchMap, tap } from 'rxjs';
import { IconComponent } from "../../../shared/components/icon/icon.component";

@Component({
  selector: 'app-employee-view',
  imports: [DialogComponent, DatePipe, IconComponent],
  templateUrl: './employee-view.component.html',
  styleUrl: './employee-view.component.css',
})
export class EmployeeViewComponent {
  private readonly employeeService = inject(EmployeeService);

  readonly employeeId = input.required<number>();
  readonly onClose = output<void>();

  // convert the input to an observable so we can react to changes
  private readonly employeeId$ = toObservable(this.employeeId);

  // the Data Stream
  private readonly employeeData$ = this.employeeId$.pipe(
    switchMap(id => this.employeeService.getEmployeeById(id).pipe(
      catchError(error => {
        console.error(error);
        return of(null);
      })
    ))
  );

  // convert back to a Signal for the view
  // initialValue: undefined helps us show the "Loading" state
  readonly employee = toSignal(this.employeeData$, { initialValue: undefined });

  // derived states for the template
  readonly isLoading = computed(() => this.employee() === undefined);
  readonly hasData = computed(() => !!this.employee());
}
