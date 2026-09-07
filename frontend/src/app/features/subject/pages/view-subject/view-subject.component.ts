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
  selector: 'app-view-subject',
  standalone: true,
  imports: [],
  templateUrl: './view-subject.component.html',
  styleUrl: './view-subject.component.scss'
})
export class ViewSubjectComponent implements OnInit {

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly subjectService =
    inject(SubjectService);

  private readonly destroyRef =
    inject(DestroyRef);

  subject: SubjectResponse | null = null;

  loading = true;
  errorMessage = '';

  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {

    const id =
      Number(this.route.snapshot.paramMap.get('id'));

    if (!id || id <= 0) {

      this.errorMessage =
        'Invalid subject ID.';

      this.loading = false;

      return;
    }

    this.loadSubject(id);
  }

  // =====================================================
  // LOAD
  // =====================================================

  private loadSubject(id: number): void {

    this.subjectService
      .getSubjectById(id)
      .pipe(
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({

        next: (response) => {

          this.subject =
            response.data;

          this.loading = false;
        },

        error: (error: any) => {

          this.errorMessage =
            error?.error?.message ||
            'Unable to load subject.';

          this.loading = false;
        }

      });
  }

  // =====================================================
  // ACTIONS
  // =====================================================

  edit(): void {

    if (!this.subject) {
      return;
    }

    this.router.navigate([
      '/dashboard/subjects/edit',
      this.subject.id
    ]);
  }

  back(): void {

    this.router.navigate([
      '/dashboard/subject'
    ]);
  }
}