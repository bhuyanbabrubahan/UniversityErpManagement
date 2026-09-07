import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';

import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import {
  ActivatedRoute,
  Router
} from '@angular/router';

import { MATERIAL_MODULES } from '../../../../shared/material/material';

import { FacultyService } from '../../services/faculty.service';

import { FacultyResponse } from '../../model/faculty-response.model';
import { FacultyRequest } from '../../model/faculty-request.model';

import { FacultyFormComponent } from '../../components/faculty-form/faculty-form.component';

import { DepartmentService } from '../../../department/services/department.service';

import { Gender } from '../../../../shared/enums/gender.enum';
import { Designation } from '../../../../shared/enums/designation.enum';
import { Qualification } from '../../../../shared/enums/qualification.enum';
import { AddressType } from '../../../../shared/enums/address-type.enum';

import { ToastService } from '@app/shared/services/toast.service';


@Component({
  selector: 'app-edit-faculty',

  standalone: true,

  imports: [
    CommonModule,
    ReactiveFormsModule,
    FacultyFormComponent,
    ...MATERIAL_MODULES
  ],

  templateUrl: './edit-faculty.component.html',

  styleUrl: './edit-faculty.component.scss'
})
export class EditFacultyComponent implements OnInit {

  // ==========================================================
  // SERVICES
  // ==========================================================

  private readonly fb =
    inject(NonNullableFormBuilder);

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly facultyService =
    inject(FacultyService);

  private readonly departmentService =
    inject(DepartmentService);

  private readonly toast =
    inject(ToastService);


  // ==========================================================
  // STATE
  // ==========================================================

  loading = false;

  saving = false;

  facultyId = 0;

  faculty: FacultyResponse | null = null;

  departments: any[] = [];


  // ==========================================================
  // ENUMS
  // ==========================================================

  readonly genders =
    Object.values(Gender);

  readonly designations =
    Object.values(Designation);

  readonly qualifications =
    Object.values(Qualification);

  readonly addressTypes =
    Object.values(AddressType);


  // ==========================================================
  // FACULTY FORM
  // ==========================================================

  readonly facultyForm =
    this.fb.group({

      // ======================================================
      // ACCOUNT INFORMATION
      // ======================================================

      email: [
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],

      password: [
        ''
      ],

      mobileNumber: [
        '',
        [
          Validators.required,
          Validators.pattern(/^[6-9]\d{9}$/)
        ]
      ],


      // ======================================================
      // PERSONAL INFORMATION
      // ======================================================

      firstName: [
        '',
        Validators.required
      ],

      middleName: [
        ''
      ],

      lastName: [
        '',
        Validators.required
      ],

      gender: [
        null as Gender | null,
        Validators.required
      ],

      dateOfBirth: [
        '',
        Validators.required
      ],


      // ======================================================
      // FACULTY INFORMATION
      // ======================================================

      joiningDate: [
        '',
        Validators.required
      ],

      designation: [
        null as Designation | null,
        Validators.required
      ],

      qualification: [
        null as Qualification | null,
        Validators.required
      ],

      salary: [
        0,
        [
          Validators.required,
          Validators.min(0)
        ]
      ],

      departmentId: [
        0,
        Validators.required
      ],


      // ======================================================
      // PROFILE
      // ======================================================

      profileImage: [
        ''
      ],


      // ======================================================
      // ADDRESS
      // ======================================================

      address: this.fb.group({

        countryId: [
          0,
          Validators.required
        ],

        stateId: [
          0,
          Validators.required
        ],

        districtId: [
          0,
          Validators.required
        ],

        tehsilId: [
          0,
          Validators.required
        ],

        villageId: [
          0,
          Validators.required
        ],

        pincode: [
          ''
        ],

        houseNumber: [
          ''
        ],

        street: [
          ''
        ],

        landmark: [
          ''
        ],

        addressType: [
          null as AddressType | null
        ]

      })

    });


  // ==========================================================
  // INIT
  // ==========================================================

  ngOnInit(): void {

    const idParam =
      this.route.snapshot.paramMap.get('id');

    const id =
      Number(idParam);

    if (
      !idParam ||
      Number.isNaN(id) ||
      id <= 0
    ) {

      this.toast.error(
        'Invalid faculty id.'
      );

      this.router.navigate([
        '/dashboard/faculty'
      ]);

      return;
    }

    this.facultyId = id;

    this.loadDepartments();

    this.loadFaculty();
  }


  // ==========================================================
  // LOAD DEPARTMENTS
  // ==========================================================

  private loadDepartments(): void {

    this.departmentService
      .getAllDepartments()
      .subscribe({

        next: response => {

          this.departments =
            response?.data?.content ??
            response?.data ??
            [];

        },

        error: error => {

          console.error(
            'Department Load Failed:',
            error
          );

          this.departments = [];

          this.toast.error(
            error?.error?.message ??
            'Unable to load departments.'
          );

        }

      });

  }


  // ==========================================================
  // LOAD FACULTY
  // ==========================================================

  private loadFaculty(): void {

    this.loading = true;

    this.facultyService
      .getFacultyById(this.facultyId)
      .subscribe({

        next: response => {

          const faculty =
            response?.data;

          if (!faculty) {

            this.loading = false;

            this.toast.error(
              'Faculty details not found.'
            );

            this.router.navigate([
              '/dashboard/faculty'
            ]);

            return;
          }

          this.faculty = faculty;

          this.patchFacultyForm(
            faculty
          );

          this.loading = false;

        },

        error: error => {

          console.error(
            'Faculty Load Failed:',
            error
          );

          this.loading = false;

          this.toast.error(
            error?.error?.message ??
            'Unable to load faculty details.'
          );

          this.router.navigate([
            '/dashboard/faculty'
          ]);

        }

      });

  }


  // ==========================================================
  // PATCH FACULTY FORM
  // ==========================================================

  private patchFacultyForm(
    faculty: FacultyResponse
  ): void {

    this.facultyForm.patchValue({

      // ACCOUNT

      email:
        faculty.email ?? '',

      // Never populate password
      password:
        '',

      mobileNumber:
        faculty.mobileNumber ?? '',


      // PERSONAL

      firstName:
        faculty.firstName ?? '',

      middleName:
        faculty.middleName ?? '',

      lastName:
        faculty.lastName ?? '',

      gender:
        faculty.gender ?? null,

      dateOfBirth:
        faculty.dateOfBirth ?? '',


      // FACULTY

      joiningDate:
        faculty.joiningDate ?? '',

      designation:
        faculty.designation ?? null,

      qualification:
        faculty.qualification ?? null,

      salary:
        faculty.salary ?? 0,

      departmentId:
        faculty.departmentId ?? 0,


      // PROFILE

      profileImage:
        faculty.profileImage ?? '',


      // ADDRESS

      address: {

        countryId:
          faculty.countryId ?? 0,

        stateId:
          faculty.stateId ?? 0,

        districtId:
          faculty.districtId ?? 0,

        tehsilId:
          faculty.tehsilId ?? 0,

        villageId:
          faculty.villageId ?? 0,

        pincode:
          faculty.pincode ?? '',

        houseNumber:
          faculty.houseNumber ?? '',

        street:
          faculty.street ?? '',

        landmark:
          faculty.landmark ?? '',

        addressType:
          faculty.addressType ?? null

      }

    });

  }


  // ==========================================================
  // UPDATE FACULTY
  // ==========================================================

  updateFaculty(): void {

    // --------------------------------------------------------
    // PREVENT DUPLICATE REQUEST
    // --------------------------------------------------------

    if (this.saving) {
      return;
    }


    // --------------------------------------------------------
    // VALIDATION
    // --------------------------------------------------------

    if (this.facultyForm.invalid) {

      this.facultyForm.markAllAsTouched();

      return;
    }


    const value =
      this.facultyForm.getRawValue();


    // --------------------------------------------------------
    // REQUIRED BUSINESS FIELDS
    // --------------------------------------------------------

    if (
      !value.gender ||
      !value.designation ||
      !value.qualification ||
      !value.departmentId ||
      !value.address.villageId
    ) {

      this.facultyForm.markAllAsTouched();

      return;
    }


    // ========================================================
    // REQUEST
    // ========================================================

    const request: FacultyRequest = {

      // ACCOUNT

      email:
        value.email.trim(),

      mobileNumber:
        value.mobileNumber.trim(),

      // Password only when user entered a new password.
      password:
        value.password.trim(),


      // PERSONAL

      firstName:
        value.firstName.trim(),

      middleName:
        value.middleName.trim(),

      lastName:
        value.lastName.trim(),

      gender:
        value.gender,

      dateOfBirth:
        value.dateOfBirth,


      // FACULTY

      joiningDate:
        value.joiningDate,

      designation:
        value.designation,

      qualification:
        value.qualification,

      salary:
        value.salary,

      departmentId:
        value.departmentId,


      // PROFILE

      profileImage:
        value.profileImage.trim() || null,


      // ADDRESS
      //
      // FacultyRequest ke according
      // sirf villageId + address details
      // send kar rahe hain.

      address: {

        villageId:
          value.address.villageId,

        houseNumber:
          value.address.houseNumber.trim() || null,

        street:
          value.address.street.trim() || null,

        landmark:
          value.address.landmark.trim() || null,

        addressType:
          value.address.addressType

      }

    };


    console.log(
      'Update Faculty Request:',
      request
    );


    // ========================================================
    // API CALL
    // ========================================================

    this.saving = true;

    this.facultyService
      .updateFaculty(
        this.facultyId,
        request
      )
      .subscribe({

        // ----------------------------------------------------
        // SUCCESS
        // ----------------------------------------------------

        next: response => {

          this.saving = false;

          this.toast.success(
            response?.message ??
            'Faculty updated successfully.'
          );

          this.router.navigate([
            '/dashboard/faculty'
          ]);

        },


        // ----------------------------------------------------
        // ERROR
        // ----------------------------------------------------

        error: error => {

          this.saving = false;

          console.error(
            'Faculty Update Failed:',
            error
          );

          this.toast.error(
            error?.error?.message ??
            'Unable to update faculty.'
          );

        }

      });

  }


  // ==========================================================
  // CANCEL
  // ==========================================================

  cancel(): void {

    if (this.saving) {
      return;
    }

    this.router.navigate([
      '/dashboard/faculty'
    ]);

  }

}