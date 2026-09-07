import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

export type ToastType =
  | 'success'
  | 'error'
  | 'warning'
  | 'info';

export interface ToastMessage {
  type: ToastType;
  message: string;
  duration?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {

  private readonly toastSubject =
    new Subject<ToastMessage>();

  readonly toast$ =
    this.toastSubject.asObservable();


  // =====================================================
  // SUCCESS
  // =====================================================

  success(
    message: string,
    duration = 5000
  ): void {

    this.show({
      type: 'success',
      message,
      duration
    });
  }


  // =====================================================
  // ERROR
  // =====================================================

  error(
    message: string,
    duration = 5000
  ): void {

    this.show({
      type: 'error',
      message,
      duration
    });
  }


  // =====================================================
  // WARNING
  // =====================================================

  warning(
    message: string,
    duration = 5000
  ): void {

    this.show({
      type: 'warning',
      message,
      duration
    });
  }


  // =====================================================
  // INFO
  // =====================================================

  info(
    message: string,
    duration = 5000
  ): void {

    this.show({
      type: 'info',
      message,
      duration
    });
  }


  // =====================================================
  // SHOW
  // =====================================================

  private show(
    toast: ToastMessage
  ): void {

    this.toastSubject.next(toast);
  }
}