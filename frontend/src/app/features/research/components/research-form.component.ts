import { Component, Input, Output, EventEmitter } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Research } from "../models/research.model";

@Component({
  selector: "app-research-form",
  standalone: true,
  imports: [FormsModule],
  templateUrl: "./research-form.component.html",
  styleUrl: "./research-form.component.scss",
})
export class ResearchFormComponent {
  @Input() model: Partial<Research> = {};
  @Input() submitLabel = "Save";
  @Output() save = new EventEmitter<Partial<Research>>();
  @Output() cancel = new EventEmitter<void>();
}
