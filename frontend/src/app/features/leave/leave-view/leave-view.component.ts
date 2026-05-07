import { Component, input, output, signal } from '@angular/core';
import { DialogComponent } from "../../../shared/components/dialog/dialog.component";
import { LeaveApproverTable } from '../../../models/leave.models';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-leave-view',
  imports: [DialogComponent, DatePipe],
  templateUrl: './leave-view.component.html',
  styleUrl: './leave-view.component.css',
})
export class LeaveViewComponent {

    readonly leave = input.required<LeaveApproverTable>();
    readonly onCancelled = output<void>();

   // presentation
   getStatusClass(status: string): string {
   switch (status) {
     case 'APPROVED': return 'bg-green-100 text-green-800';
     case 'REJECTED': return 'bg-red-800 text-red-100';
     default: return 'bg-gray-100 text-gray-800';
   }
 }

}
