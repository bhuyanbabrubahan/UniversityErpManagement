import { Component, Input, Output, EventEmitter } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { Grievance } from "../models/grievance.model";
@Component({
  selector: "app-grievance-table",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./grievance-table.component.html",
  styleUrl: "./grievance-table.component.scss",
})
export class GrievanceTableComponent {
  @Input() rows: Grievance[] = [];
  @Output() remove = new EventEmitter<number>();
}
