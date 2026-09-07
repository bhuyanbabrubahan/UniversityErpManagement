import {
    Component,
    OnInit,
    inject,
    signal
} from '@angular/core';

import {
    ActivatedRoute,
    Router
} from '@angular/router';

import { CommonModule } from '@angular/common';

import { MATERIAL_MODULES } from '../../../../shared/material/material';

import { FacultyService } from '../../services/faculty.service';

import {
    FacultyResponse
} from '../../model/faculty-response.model';

@Component({
    selector: 'app-view-faculty',
    standalone: true,

    imports: [
        CommonModule,
        ...MATERIAL_MODULES
    ],

    templateUrl: './view-faculty.component.html',
    styleUrl: './view-faculty.component.scss'
})
export class ViewFacultyComponent implements OnInit {

    // =====================================================
    // DEPENDENCIES
    // =====================================================

    private readonly route =
        inject(ActivatedRoute);

    private readonly router =
        inject(Router);

    private readonly facultyService =
        inject(FacultyService);


    // =====================================================
    // STATE
    // =====================================================

    readonly faculty =
        signal<FacultyResponse | null>(null);

    readonly loading =
        signal(false);

    readonly error =
        signal<string | null>(null);


    // =====================================================
    // INIT
    // =====================================================

    ngOnInit(): void {

        const idParam =
            this.route.snapshot.paramMap.get('id');

        const id =
            Number(idParam);

        if (!idParam || Number.isNaN(id) || id <= 0) {

            this.error.set(
                'Invalid faculty ID.'
            );

            return;
        }

        this.loadFaculty(id);
    }


    // =====================================================
    // LOAD FACULTY
    // =====================================================

    private loadFaculty(id: number): void {

        this.loading.set(true);

        this.error.set(null);

        this.faculty.set(null);


        this.facultyService
            .getFacultyById(id)
            .subscribe({

                next: (response) => {

                    if (!response?.data) {

                        this.error.set(
                            'Faculty details not found.'
                        );

                        this.loading.set(false);

                        return;
                    }


                    this.faculty.set(
                        response.data
                    );

                    this.loading.set(false);
                },


                error: (err) => {

                    console.error(
                        'Failed to load faculty:',
                        err
                    );


                    let message =
                        'Failed to load faculty details.';


                    if (err?.status === 404) {

                        message =
                            'Faculty not found.';

                    } else if (
                        err?.status === 0
                    ) {

                        message =
                            'Unable to connect to the server.';

                    } else if (
                        err?.error?.message
                    ) {

                        message =
                            err.error.message;

                    } else if (
                        err?.message
                    ) {

                        message =
                            err.message;
                    }


                    this.error.set(message);

                    this.loading.set(false);
                }

            });
    }


    // =====================================================
    // BACK TO FACULTY LIST
    // =====================================================

    backToList(): void {

        this.router.navigate([
            '/dashboard/faculty'
        ]);
    }


    // =====================================================
    // EDIT FACULTY
    // =====================================================

    editFaculty(): void {

        const faculty =
            this.faculty();

        if (!faculty?.id) {
            return;
        }


        this.router.navigate([
            '/dashboard/faculties/edit',
            faculty.id
        ]);
    }


    // =====================================================
    // RETRY
    // =====================================================

    retry(): void {

        const idParam =
            this.route.snapshot.paramMap.get('id');

        const id =
            Number(idParam);


        if (
            !idParam ||
            Number.isNaN(id) ||
            id <= 0
        ) {

            this.error.set(
                'Invalid faculty ID.'
            );

            return;
        }


        this.loadFaculty(id);
    }

}