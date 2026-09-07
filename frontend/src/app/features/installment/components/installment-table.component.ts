import { Component, Input, Output, EventEmitter } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { Installment } from "../models/installment.model";
@Component({
  selector: "app-installment-table",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./installment-table.component.html",
  styleUrl: "./installment-table.component.scss",
})
export class InstallmentTableComponent {
  @Input() rows: Installment[] = [];
  @Output() remove = new EventEmitter<number>();
}
