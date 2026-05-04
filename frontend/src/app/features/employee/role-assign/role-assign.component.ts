import { Component, computed, inject, input, linkedSignal, NgModule, OnInit, output, signal } from '@angular/core';
import { DialogComponent } from "../../../shared/components/dialog/dialog.component";
import { EmployeeService } from '../employee.service';
import { PermissionInfo, RoleInfo } from '../../../models/auth.model';
import { LoadingService } from '../../../core/services/loading.service';
import { catchError, finalize, forkJoin, of, switchMap, tap } from 'rxjs';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-role-assign',
  imports: [DialogComponent],
  templateUrl: './role-assign.component.html',
  styleUrl: './role-assign.component.css',
})
export class RoleAssignComponent {
  // services
  private readonly employeeService = inject(EmployeeService);

  // state
  readonly dataLoader = signal(false);

  // input & output
  readonly employeeId = input.required<number>();
  readonly onCompleted = output<void>();
  readonly onCancelled = output<void>();

  // data fetching
  private readonly _employeeId$ = toObservable(this.employeeId);

  private readonly rolesData$ = this._employeeId$.pipe(
    switchMap(id => forkJoin({
      employeeRoles: this.employeeService.getRoleInfo(id),
      allRoles: this.employeeService.getAllRoles()
    }).pipe(
      catchError((error) => {
        console.error('API Error:', error);
        return of(null);
      }),
      finalize(() => this.dataLoader.set(true))
    ))
  );

  // convert observable to signal for easier template usage
  readonly roleData = toSignal(this.rolesData$);

  // This signal is WRITABLE, but resets whenever roleData changes.
  readonly selectedRoleIds = linkedSignal({
    source: this.roleData,
    computation: (data) => new Set(data?.employeeRoles.map(r => r.id) ?? [])
  });

  // derived state
  readonly employeeInfo = computed(() => {
    const data = this.roleData();
      return {
          name: data?.employeeRoles[0]?.employeeName ?? 'Unknown',
          code: data?.employeeRoles[0]?.employeeCode ?? 'N/A'
      }});
  readonly allRoles = computed(() => this.roleData()?.allRoles ?? []);

  // select/deselect role
  toggleRole(roleId: number): void {
    // linkedSignal is updated just like a normal signal
    this.selectedRoleIds.update((prev) => {
      const next = new Set(prev);
      next.has(roleId) ? next.delete(roleId) : next.add(roleId);
      return next;
    });
  }

  // assign roles to employee
  assignRoles(): void {
    const payload = {
      employeeId: this.employeeId(),
      roleIds: Array.from(this.selectedRoleIds())
    };

    this.employeeService.updateEmployeeRoles(payload).subscribe({
      next: () => this.onCompleted.emit(),
      error: (err) => console.error('Save failed', err)
    });
  }

  cancel(): void {
    this.onCancelled.emit();
  }

}
