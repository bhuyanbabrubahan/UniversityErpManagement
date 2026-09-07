import { Component, Input, Output, EventEmitter } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { Placement } from "../models/placement.model";
@Component({
  selector: "app-placement-table",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./placement-table.component.html",
  styleUrl: "./placement-table.component.scss",
})
export class PlacementTableComponent {
  @Input() rows: Placement[] = [];
  @Output() remove = new EventEmitter<number>();
}
