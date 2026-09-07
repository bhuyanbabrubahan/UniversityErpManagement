import { CommonModule } from "@angular/common";

import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
  inject,
} from "@angular/core";

import { MatIconModule } from "@angular/material/icon";

import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";

import { Notification } from "../models/notification.model";
import { NotificationRequest } from "../models/notification-request.model";

import {
  NOTIFICATION_CHANNELS,
  NOTIFICATION_STATUSES,
  NOTIFICATION_TARGET_ROLES,
} from "../shared/notification.constants";

@Component({
  selector: "app-notification-form",

  standalone: true,

  imports: [CommonModule, ReactiveFormsModule, MatIconModule],

  templateUrl: "./notification-form.component.html",

  styleUrl: "./notification-form.component.scss",
})
export class NotificationFormComponent implements OnChanges {
  private readonly fb = inject(FormBuilder);

  @Input()
  model: Partial<Notification> = {};

  @Input()
  submitLabel = "Save changes";

  @Input()
  busy = false;

  @Output()
  save = new EventEmitter<NotificationRequest>();

  @Output()
  cancel = new EventEmitter<void>();

  readonly statuses = NOTIFICATION_STATUSES;

  readonly channels = NOTIFICATION_CHANNELS;

  readonly targetRoles = NOTIFICATION_TARGET_ROLES;

  readonly form = this.fb.nonNullable.group({
    code: ["", [Validators.required, Validators.maxLength(60)]],

    title: ["", [Validators.required, Validators.maxLength(160)]],

    message: ["", [Validators.required, Validators.maxLength(5000)]],

    targetRole: ["ALL", Validators.required],

    channel: ["EMAIL", Validators.required],

    publishedAt: [""],

    status: ["DRAFT", Validators.required],

    description: ["", Validators.maxLength(1000)],
  });

  // =========================================================
  // INPUT CHANGE
  // =========================================================

  ngOnChanges(changes: SimpleChanges): void {
    if (!changes["model"]) {
      return;
    }

    this.form.patchValue(
      {
        code: this.model.code ?? "",

        title: this.model.title ?? "",

        message: this.model.message ?? "",

        targetRole: this.model.targetRole ?? "ALL",

        channel: this.model.channel ?? "IN_APP",

        publishedAt: this.toDateTimeLocal(this.model.publishedAt),

        status: this.model.status ?? "DRAFT",

        description: this.model.description ?? "",
      },
      {
        emitEvent: false,
      },
    );

    this.form.markAsPristine();

    this.form.markAsUntouched();
  }

  // =========================================================
  // SUBMIT
  // =========================================================

  submit(): void {
    if (this.busy) {
      return;
    }

    this.form.markAllAsTouched();

    if (this.form.invalid) {
      return;
    }

    const raw = this.form.getRawValue();

    const request: NotificationRequest = {
      code: raw.code.trim(),

      title: raw.title.trim(),

      message: raw.message.trim(),

      targetRole: raw.targetRole,

      channel: raw.channel,

      status: raw.status,

      publishedAt: raw.publishedAt ? this.toApiDateTime(raw.publishedAt) : null,

      description: raw.description.trim(),
    };

    this.save.emit(request);
  }

  // =========================================================
  // CANCEL
  // =========================================================

  cancelForm(): void {
    if (this.busy) {
      return;
    }

    this.cancel.emit();
  }

  // =========================================================
  // VALIDATION
  // =========================================================

  invalid(name: string): boolean {
    const control = this.form.get(name);

    return !!(control && control.invalid && control.touched);
  }

  errorMessage(name: string): string {
    const control = this.form.get(name);

    if (!control || !control.touched) {
      return "";
    }

    if (control.hasError("required")) {
      return "This field is required.";
    }

    if (control.hasError("maxlength")) {
      const max = control.getError("maxlength")?.requiredLength;

      return `Maximum ${max} characters allowed.`;
    }

    return "Please enter a valid value.";
  }

  // =========================================================
  // API DATETIME -> DATETIME LOCAL
  // =========================================================

  private toDateTimeLocal(value?: string | null): string {
    if (!value) {
      return "";
    }

    const raw = String(value).trim();

    if (!raw) {
      return "";
    }

    /*
     * Backend can return:
     *
     * 2026-08-20T09:00:00
     *
     * or:
     *
     * 2026-08-20 09:00:00
     */

    const match = raw.match(/^(\d{4})-(\d{2})-(\d{2})[T ](\d{2}):(\d{2})/);

    if (match) {
      return (
        `${match[1]}-` +
        `${match[2]}-` +
        `${match[3]}T` +
        `${match[4]}:` +
        `${match[5]}`
      );
    }

    return "";
  }

  // =========================================================
  // DATETIME LOCAL -> API
  // =========================================================

  private toApiDateTime(value: string): string {
    if (!value) {
      return "";
    }

    if (/^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}$/.test(value)) {
      return `${value}:00`;
    }

    return value;
  }
}
