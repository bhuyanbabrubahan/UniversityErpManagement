import { Component, Input, Output, EventEmitter } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { Marks } from "../models/marks.model";
@Component({
  selector: "app-marks-table",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./marks-table.component.html",
  styleUrl: "./marks-table.component.scss",
})
export class MarksTableComponent {
  @Input() rows: Marks[] = [];
  @Output() remove = new EventEmitter<number>();
}
