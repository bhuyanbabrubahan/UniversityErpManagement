import { Component, Input, Output, EventEmitter } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { Hostel } from "../models/hostel.model";
@Component({
  selector: "app-hostel-table",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./hostel-table.component.html",
  styleUrl: "./hostel-table.component.scss",
})
export class HostelTableComponent {
  @Input() rows: Hostel[] = [];
  @Output() remove = new EventEmitter<number>();
}
