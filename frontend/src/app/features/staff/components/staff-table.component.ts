import { Component, Input, Output, EventEmitter } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { Staff } from "../models/staff.model";
@Component({
  selector: "app-staff-table",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./staff-table.component.html",
  styleUrl: "./staff-table.component.scss",
})
export class StaffTableComponent {
  @Input() rows: Staff[] = [];
  @Output() remove = new EventEmitter<number>();
}
