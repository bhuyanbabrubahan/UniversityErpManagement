import {
  Component,
  DestroyRef,
  OnInit,
  inject
} from '@angular/core';

import {
  ActivatedRoute,
  Router
} from '@angular/router';

import {
  takeUntilDestroyed
} from '@angular/core/rxjs-interop';

import {
  SubjectResponse
} from '../../models/subject.model';

import {
  SubjectService
} from '../../services/subject.service';

@Component({
  selector: 'app-delete-subject-dialog',
  standalone: true,
  imports: [],
  templateUrl: './delete-subject-dialog.component.html',
  styleUrl: './delete-subject-dialog.component.scss'
})
export class DeleteSubjectDialogComponent
  implements OnInit {

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly subjectService =
    inject(SubjectService);

  private readonly destroyRef =
    inject(DestroyRef);

  subjectId!: number;

  subject: SubjectResponse | null = null;

  loadingData = true;
  deleting = false;

  errorMessage = '';

  ngOnInit(): void {

    const id =
      Number(this.route.snapshot.paramMap.get('id'));

    if (!id || id <= 0) {

      this.errorMessage =
        'Invalid subject ID.';

      this.loadingData = false;

      return;
    }

    this.subjectId = id;

    this.loadSubject();
  }

  private loadSubject(): void {

    this.subjectService
      .getSubjectById(this.subjectId)
      .pipe(
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({

        next: (response) => {

          this.subject =
            response.data;

          this.loadingData = false;
        },

        error: (error) => {

          this.errorMessage =
            error?.error?.message ||
            'Unable to load subject.';

          this.loadingData = false;
        }

      });
  }

  confirmDelete(): void {

    if (!this.subject) {
      return;
    }

    this.deleting = true;
    this.errorMessage = '';

    this.subjectService
      .deleteSubject(this.subjectId)
      .pipe(
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({

        next: () => {

          this.deleting = false;

          this.router.navigate([
            '/dashboard/subject'
          ]);
        },

        error: (error) => {

          this.deleting = false;

          this.errorMessage =
            error?.error?.message ||
            'Unable to delete subject.';
        }

      });
  }

  cancel(): void {

    this.router.navigate([
      '/dashboard/subject'
    ]);
  }
}