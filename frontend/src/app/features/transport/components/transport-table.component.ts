import { Component, Input, Output, EventEmitter } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { Transport } from "../models/transport.model";
@Component({
  selector: "app-transport-table",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./transport-table.component.html",
  styleUrl: "./transport-table.component.scss",
})
export class TransportTableComponent {
  @Input() rows: Transport[] = [];
  @Output() remove = new EventEmitter<number>();
}
