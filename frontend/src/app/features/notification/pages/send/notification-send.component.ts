import { CommonModule } from "@angular/common";
import { Component, inject, signal } from "@angular/core";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";
import { RouterLink } from "@angular/router";
import { catchError, EMPTY, finalize } from "rxjs";
import { NotificationService } from "../../services/notification.service";

@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: "./notification-send.component.html",
  styleUrl: "./notification-send.component.scss",
})
export class NotificationSendComponent {
  private readonly fb = inject(FormBuilder);
  private readonly service = inject(NotificationService);
  readonly busy = signal(false);
  readonly success = signal(false);
  readonly error = signal("");
  readonly form = this.fb.nonNullable.group({
    recipient: ["", [Validators.required, Validators.maxLength(160)]],
    channel: ["EMAIL", Validators.required],
    subject: ["", [Validators.required, Validators.maxLength(160)]],
    message: ["", [Validators.required, Validators.maxLength(5000)]],
  });
  submit(): void {
    this.form.markAllAsTouched();
    if (this.form.invalid || this.busy()) return;
    this.busy.set(true);
    this.error.set("");
    this.success.set(false);
    this.service
      .send(this.form.getRawValue())
      .pipe(
        catchError((error) => {
          this.error.set(
            error?.error?.message || "Unable to send notification.",
          );
          return EMPTY;
        }),
        finalize(() => this.busy.set(false)),
      )
      .subscribe(() => {
        this.success.set(true);
        this.form.reset({
          recipient: "",
          channel: "EMAIL",
          subject: "",
          message: "",
        });
      });
  }
}
