import { Component, Input, Output, EventEmitter } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { Research } from "../models/research.model";
@Component({
  selector: "app-research-table",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./research-table.component.html",
  styleUrl: "./research-table.component.scss",
})
export class ResearchTableComponent {
  @Input() rows: Research[] = [];
  @Output() remove = new EventEmitter<number>();
}
