import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-delete-department-dialog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './delete-department-dialog.component.html',
  styleUrl: './delete-department-dialog.component.scss'
})
export class DeleteDepartmentDialogComponent {

  @Input() visible = false;
  @Input() departmentName = '';
  @Input() loading = false;

  @Output() confirmDelete = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();

  onConfirm(): void {
    if (!this.loading) {
      this.confirmDelete.emit();
    }
  }

  onCancel(): void {
    if (!this.loading) {
      this.cancel.emit();
    }
  }
}