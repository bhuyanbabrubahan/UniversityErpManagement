import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
  inject
} from '@angular/core';

import {
  Subscription
} from 'rxjs';

import {
  ToastMessage,
  ToastService
} from '../../services/toast.service';


@Component({
  selector: 'app-toast',
  standalone: true,
  templateUrl: './toast.component.html',
  styleUrl: './toast.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ToastComponent
  implements OnInit, OnDestroy {

  private readonly toastService =
    inject(ToastService);

  private readonly cdr =
    inject(ChangeDetectorRef);


  toast: ToastMessage | null = null;

  private subscription:
    Subscription | null = null;

  private hideTimer:
    ReturnType<typeof setTimeout> | null = null;


  // =====================================================
  // LIFECYCLE
  // =====================================================

  ngOnInit(): void {

    this.subscription =
      this.toastService.toast$
        .subscribe(toast => {

          this.showToast(toast);

        });
  }


  ngOnDestroy(): void {

    this.clearHideTimer();

    this.subscription?.unsubscribe();
  }


  // =====================================================
  // SHOW TOAST
  // =====================================================

  private showToast(
    toast: ToastMessage
  ): void {

    this.clearHideTimer();

    this.toast = toast;

    this.cdr.markForCheck();


    this.hideTimer = setTimeout(() => {

      this.close();

    }, toast.duration ?? 5000);
  }


  // =====================================================
  // CLOSE
  // =====================================================

  close(): void {

    this.toast = null;

    this.clearHideTimer();

    this.cdr.markForCheck();
  }


  // =====================================================
  // CLEAR TIMER
  // =====================================================

  private clearHideTimer(): void {

    if (this.hideTimer) {

      clearTimeout(this.hideTimer);

      this.hideTimer = null;
    }
  }


  // =====================================================
  // ICON
  // =====================================================

  getIcon(): string {

    switch (this.toast?.type) {

      case 'success':
        return '✓';

      case 'error':
        return '!';

      case 'warning':
        return '⚠';

      case 'info':
        return 'i';

      default:
        return '';
    }
  }


  // =====================================================
  // TITLE
  // =====================================================

  getTitle(): string {

    switch (this.toast?.type) {

      case 'success':
        return 'Success';

      case 'error':
        return 'Error';

      case 'warning':
        return 'Warning';

      case 'info':
        return 'Info';

      default:
        return '';
    }
  }
}