import { Component, Input, Output, EventEmitter } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { Scholarship } from "../models/scholarship.model";
@Component({
  selector: "app-scholarship-table",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./scholarship-table.component.html",
  styleUrl: "./scholarship-table.component.scss",
})
export class ScholarshipTableComponent {
  @Input() rows: Scholarship[] = [];
  @Output() remove = new EventEmitter<number>();
}
