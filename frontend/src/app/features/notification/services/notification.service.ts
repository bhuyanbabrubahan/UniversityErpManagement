import { Injectable, inject } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable, map } from "rxjs";

import { environment } from "../../../../environments/environment";

import { Notification } from "../models/notification.model";
import { NotificationRequest } from "../models/notification-request.model";

import {
  NotificationPage,
  NotificationSearch,
} from "../models/notification-search.model";

@Injectable({
  providedIn: "root",
})
export class NotificationService {
  private readonly http = inject(HttpClient);

  private readonly url = `${environment.apiUrl}/erp/notification`;

  private readonly sendUrl =
    `${environment.apiUrl.replace(/\/api\/?$/, "")}` +
    `/api/v1/notifications/send`;

  // =========================================================
  // LIST
  // =========================================================

  list(page = 0, size = 20): Observable<NotificationPage<Notification>> {
    return this.http
      .get<unknown>(this.url, {
        params: this.pageParams(page, size),
      })
      .pipe(map((response) => this.normalizePage(response)));
  }

  // =========================================================
  // SEARCH
  // =========================================================

  search(
    search: NotificationSearch,
    page = 0,
    size = 20,
  ): Observable<NotificationPage<Notification>> {
    return this.http
      .post<unknown>(`${this.url}/search`, search, {
        params: this.pageParams(page, size),
      })
      .pipe(map((response) => this.normalizePage(response)));
  }

  // =========================================================
  // GET BY ID
  // =========================================================

  get(id: number): Observable<Notification> {
    return this.http
      .get<unknown>(`${this.url}/${id}`)
      .pipe(map((response) => this.normalizeNotification(response)));
  }

  // =========================================================
  // CREATE
  // =========================================================

  create(request: NotificationRequest): Observable<Notification> {
    return this.http
      .post<unknown>(this.url, request)
      .pipe(map((response) => this.normalizeNotification(response)));
  }

  // =========================================================
  // UPDATE
  // =========================================================

  update(id: number, request: NotificationRequest): Observable<Notification> {
    return this.http
      .put<unknown>(`${this.url}/${id}`, request)
      .pipe(map((response) => this.normalizeNotification(response)));
  }

  // =========================================================
  // DELETE
  // =========================================================

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  // =========================================================
  // DIRECT EMAIL / SMS SEND
  // =========================================================

  send(request: {
    recipient: string;
    subject: string;
    message: string;
    channel: string;
  }): Observable<void> {
    return this.http.post<void>(this.sendUrl, request);
  }

  // =========================================================
  // HTTP PARAMS
  // =========================================================

  private pageParams(page: number, size: number): HttpParams {
    return new HttpParams().set("page", page).set("size", size);
  }

  // =========================================================
  // NORMALIZE SINGLE RESPONSE
  // =========================================================

  private normalizeNotification(value: unknown): Notification {
    const raw = (value ?? {}) as Record<string, unknown>;

    return {
      id: this.toNumber(raw["id"]),

      code: this.toString(raw["code"]),

      title: this.toString(raw["title"]),

      message: this.toString(raw["message"]),

      targetRole: this.toString(raw["targetRole"]),

      channel: this.toString(raw["channel"]),

      publishedAt: this.toStringOrNull(raw["publishedAt"]),

      status: this.toString(raw["status"]),

      description: this.toString(raw["description"]),

      createdByUserId: this.toNumberOrNull(raw["createdByUserId"]),

      createdBy: this.toStringOrNull(raw["createdBy"]),

      updatedByUserId: this.toNumberOrNull(raw["updatedByUserId"]),

      updatedBy: this.toStringOrNull(raw["updatedBy"]),

      createdAt: this.toStringOrNull(raw["createdAt"]),

      updatedAt: this.toStringOrNull(raw["updatedAt"]),
    };
  }

  // =========================================================
  // NORMALIZE PAGE
  // =========================================================

  private normalizePage(value: unknown): NotificationPage<Notification> {
    const raw = (value ?? {}) as Record<string, unknown>;

    const content = Array.isArray(raw["content"]) ? raw["content"] : [];

    return {
      content: content.map((item) => this.normalizeNotification(item)),

      totalElements: this.toNumber(raw["totalElements"]) ?? 0,

      totalPages: this.toNumber(raw["totalPages"]) ?? 0,

      number: this.toNumber(raw["number"]) ?? 0,

      size: this.toNumber(raw["size"]) ?? 20,

      first: Boolean(raw["first"]),

      last: Boolean(raw["last"]),
    };
  }

  // =========================================================
  // STRING
  // =========================================================

  private toString(value: unknown): string | undefined {
    if (value === null || value === undefined || value === "") {
      return undefined;
    }

    return String(value);
  }

  // =========================================================
  // STRING OR NULL
  // =========================================================

  private toStringOrNull(value: unknown): string | null {
    const result = this.toString(value);

    return result ?? null;
  }

  // =========================================================
  // NUMBER
  // =========================================================

  private toNumber(value: unknown): number | undefined {
    if (value === null || value === undefined || value === "") {
      return undefined;
    }

    const number = Number(value);

    return Number.isFinite(number) ? number : undefined;
  }

  // =========================================================
  // NUMBER OR NULL
  // =========================================================

  private toNumberOrNull(value: unknown): number | null {
    return this.toNumber(value) ?? null;
  }
}
