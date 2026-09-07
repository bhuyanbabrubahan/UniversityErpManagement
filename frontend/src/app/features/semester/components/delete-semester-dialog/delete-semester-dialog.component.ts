import {
  Component,
  EventEmitter,
  Input,
  Output
} from '@angular/core';

import {
  SemesterResponse
} from '../../models/semester.model';


@Component({
  selector: 'app-delete-semester-dialog',
  standalone: true,
  imports: [],
  templateUrl: './delete-semester-dialog.component.html',
  styleUrl: './delete-semester-dialog.component.scss'
})
export class DeleteSemesterDialogComponent {

  @Input()
  semester: SemesterResponse | null = null;

  @Input()
  loading = false;

  @Output()
  confirm =
    new EventEmitter<number>();

  @Output()
  cancel =
    new EventEmitter<void>();


  onConfirm(): void {

    if (
      !this.semester?.id ||
      this.loading
    ) {

      return;
    }

    this.confirm.emit(
      this.semester.id
    );

  }


  onCancel(): void {

    if (this.loading) {

      return;
    }

    this.cancel.emit();

  }

}