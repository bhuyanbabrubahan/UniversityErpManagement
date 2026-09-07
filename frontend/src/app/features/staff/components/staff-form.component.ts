import { Component, Input, Output, EventEmitter } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Staff } from "../models/staff.model";
@Component({
  selector: "app-staff-form",
  standalone: true,
  imports: [FormsModule],
  templateUrl: "./staff-form.component.html",
  styleUrl: "./staff-form.component.scss",
})
export class StaffFormComponent {
  @Input() model: Partial<Staff> = {};
  @Input() submitLabel = "Save";
  @Output() save = new EventEmitter<Partial<Staff>>();
  @Output() cancel = new EventEmitter<void>();
}
