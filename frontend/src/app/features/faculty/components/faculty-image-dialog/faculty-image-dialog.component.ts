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
  selector: 'app-faculty-image-dialog',

  standalone: true,

  imports: [
    MatDialogModule,
    MatIconModule
  ],

  templateUrl: './faculty-image-dialog.component.html',

  styleUrl: './faculty-image-dialog.component.scss'
})
export class FacultyImageDialogComponent {

  // =====================================================
  // CONSTRUCTOR
  // =====================================================

  constructor(
    private readonly dialogRef:
      MatDialogRef<FacultyImageDialogComponent>,

    @Inject(MAT_DIALOG_DATA)
    public readonly data: {
      imageUrl: string;
      facultyName: string;
    }
  ) {}


  // =====================================================
  // CLOSE
  // =====================================================

  close(): void {
    this.dialogRef.close();
  }
}