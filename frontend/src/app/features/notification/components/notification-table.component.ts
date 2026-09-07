import { CommonModule } from "@angular/common";

import { Component, EventEmitter, Input, Output } from "@angular/core";

import { RouterLink } from "@angular/router";

import { Notification } from "../models/notification.model";

import { formatNotificationDate } from "../shared/notification.utils";

@Component({
  selector: "app-notification-table",

  standalone: true,

  imports: [CommonModule, RouterLink],

  templateUrl: "./notification-table.component.html",

  styleUrl: "./notification-table.component.scss",
})
export class NotificationTableComponent {
  @Input()
  rows: Notification[] = [];

  @Output()
  remove = new EventEmitter<number>();

  // =========================================================
  // DATE
  // =========================================================

  formatDate(value?: string | null): string {
    return formatNotificationDate(value);
  }

  // =========================================================
  // STATUS
  // =========================================================

  statusClass(status?: string | null): string {
    return `status-${(status || "UNKNOWN").toLowerCase()}`;
  }

  // =========================================================
  // CHANNEL
  // =========================================================

  channelClass(channel?: string | null): string {
    return channel?.toUpperCase() === "SMS" ? "sms" : "email";
  }

  // =========================================================
  // TRACK
  // =========================================================

  trackById(index: number, row: Notification): number {
    return row.id ?? index;
  }

  // =========================================================
  // DELETE
  // =========================================================

  deleteRow(id: number | undefined): void {
    if (id === undefined || id === null) {
      return;
    }

    this.remove.emit(id);
  }
}
