import { Component } from '@angular/core';
import { LeaveService } from './leave.service';
import { LeaveTableComponent } from "./leave-table/leave-table.component";

@Component({
  selector: 'app-leave.component',
  imports: [LeaveTableComponent],
  providers: [LeaveService],
  templateUrl: './leave.component.html',
  styleUrl: './leave.component.css',
})
export class LeaveComponent {}
