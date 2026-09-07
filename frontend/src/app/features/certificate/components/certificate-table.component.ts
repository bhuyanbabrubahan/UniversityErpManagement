import { Component, Input, Output, EventEmitter } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { Certificate } from "../models/certificate.model";
@Component({
  selector: "app-certificate-table",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./certificate-table.component.html",
  styleUrl: "./certificate-table.component.scss",
})
export class CertificateTableComponent {
  @Input() rows: Certificate[] = [];
  @Output() remove = new EventEmitter<number>();
}
