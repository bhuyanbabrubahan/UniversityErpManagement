import { CommonModule } from '@angular/common';
import {
  Component,
  EventEmitter,
  Input,
  Output
} from '@angular/core';

import { DepartmentResponse } from '../../models/department.model';

@Component({
  selector: 'app-department-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './department-table.component.html',
  styleUrl: './department-table.component.scss'
})
export class DepartmentTableComponent {

  @Input() departments: DepartmentResponse[] = [];
  @Input() loading = false;

  @Output() view =
    new EventEmitter<DepartmentResponse>();

  @Output() edit =
    new EventEmitter<DepartmentResponse>();

  @Output() delete =
    new EventEmitter<DepartmentResponse>();

  trackById(
    index: number,
    department: DepartmentResponse
  ): number | string {
    return department.id ?? index;
  }

  onView(department: DepartmentResponse): void {
    this.view.emit(department);
  }

  onEdit(department: DepartmentResponse): void {
    this.edit.emit(department);
  }

  onDelete(department: DepartmentResponse): void {
    this.delete.emit(department);
  }
}