import { Component, Input, Output, EventEmitter } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { Library } from "../models/library.model";
@Component({
  selector: "app-library-table",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./library-table.component.html",
  styleUrl: "./library-table.component.scss",
})
export class LibraryTableComponent {
  @Input() rows: Library[] = [];
  @Output() remove = new EventEmitter<number>();
}
