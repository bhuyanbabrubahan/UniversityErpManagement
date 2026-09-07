import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { MATERIAL_MODULES } from '../../../../shared/material/material';
import { StudentService } from '../../services/student.service';
import { StudentResponse } from '../../models/student-response.model';

@Component({
  selector: 'app-view-student',
  standalone: true,
  imports: [
    CommonModule,
    ...MATERIAL_MODULES
  ],
  templateUrl: './view-student.component.html',
  styleUrl: './view-student.component.scss'
})
export class ViewStudentComponent implements OnInit {

  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly studentService = inject(StudentService);

  student = signal<StudentResponse | null>(null);
  loading = signal<boolean>(false);
  error = signal<string | null>(null);

  ngOnInit(): void {
    const studentId = Number(
      this.route.snapshot.paramMap.get('id')
    );

    if (studentId) {
      this.loadStudent(studentId);
    }
  }

private loadStudent(id: number): void {
  this.loading.set(true);
  this.error.set(null);

  this.studentService.getStudentById(id)
    .subscribe({
      next: (response) => {
        this.student.set(response.data);
        
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Student loading failed', err);

        this.error.set(
          'Student details load nahi ho paye.'
        );

        
        this.loading.set(false);
      }
    });
}

  backToList(): void {
    this.router.navigate(['/dashboard/students']);
  }
}