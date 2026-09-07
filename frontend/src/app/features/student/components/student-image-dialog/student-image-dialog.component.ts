import {
  Component,
  Inject
} from '@angular/core';

import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef
} from '@angular/material/dialog';

import {
  MatIconModule
} from '@angular/material/icon';


@Component({
  selector: 'app-student-image-dialog',

  standalone: true,

  imports: [
    MatDialogModule,
    MatIconModule
  ],

  templateUrl: './student-image-dialog.component.html',

  styleUrl: './student-image-dialog.component.scss'
})
export class StudentImageDialogComponent {

  constructor(

    private readonly dialogRef:
      MatDialogRef<StudentImageDialogComponent>,

    @Inject(MAT_DIALOG_DATA)

    public readonly data: {
      imageUrl: string;
      studentName: string;
    }

  ) {}


  // =====================================================
  // CLOSE
  // =====================================================

  close(): void {

    this.dialogRef.close();

  }

}