import {
  Component,
  EventEmitter,
  Input,
  Output
} from '@angular/core';


@Component({
  selector: 'app-delete-payment-dialog',
  standalone: true,
  templateUrl: './delete-payment-dialog.component.html',
  styleUrl: './delete-payment-dialog.component.scss'
})
export class DeletePaymentDialogComponent {

  @Input({ required: true })
  paymentId!: number;


  @Input()
  loading = false;


  @Output()
  closed = new EventEmitter<void>();


  @Output()
  deleted = new EventEmitter<void>();


  close(): void {

    if (this.loading) {
      return;
    }

    this.closed.emit();
  }


  confirmDelete(): void {

    if (
      !this.paymentId ||
      this.loading
    ) {
      return;
    }

    this.deleted.emit();
  }

}