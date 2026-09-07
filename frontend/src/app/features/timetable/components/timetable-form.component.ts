import { Component, EventEmitter, Input, Output } from "@angular/core";

import { FormsModule } from "@angular/forms";

import { Timetable } from "../models/timetable.model";
import {
  DAYS_OF_WEEK,
  TIMETABLE_STATUS_OPTIONS,
} from "../shared/timetable.constants";

@Component({
  selector: "app-timetable-form",
  standalone: true,

  imports: [FormsModule],

  templateUrl: "./timetable-form.component.html",

  styleUrl: "./timetable-form.component.scss",
})
export class TimetableFormComponent {
  @Input()
  model: Partial<Timetable> = {};

  @Input()
  submitLabel = "Save";

  @Input()
  loading = false;

  @Output()
  save = new EventEmitter<Partial<Timetable>>();

  @Output()
  cancel = new EventEmitter<void>();

  readonly statusOptions = TIMETABLE_STATUS_OPTIONS;

  readonly daysOfWeek = DAYS_OF_WEEK;

  submit(): void {
    if (
      !this.model.code?.trim() ||
      !this.model.academicYear?.trim() ||
      !this.model.semester?.trim() ||
      !this.model.course?.trim() ||
      !this.model.section?.trim() ||
      !this.model.subject?.trim() ||
      !this.model.faculty?.trim() ||
      !this.model.room?.trim() ||
      !this.model.dayOfWeek ||
      !this.model.startTime ||
      !this.model.endTime ||
      !this.model.period?.trim() ||
      !this.model.status ||
      !this.model.description?.trim()
    ) {
      return;
    }

    this.save.emit(this.model);
  }
}
