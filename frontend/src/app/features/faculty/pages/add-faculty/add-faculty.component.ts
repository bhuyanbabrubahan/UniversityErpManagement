import { CommonModule } from '@angular/common';

import {
  Component,
  OnInit,
  inject
} from '@angular/core';

import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { Router } from '@angular/router';

import { MATERIAL_MODULES } from '../../../../shared/material/material';

import { AddressType } from '../../../../shared/enums/address-type.enum';
import { Designation } from '../../../../shared/enums/designation.enum';
import { Gender } from '../../../../shared/enums/gender.enum';
import { Qualification } from '../../../../shared/enums/qualification.enum';

import { FacultyRequest } from '../../model/faculty-request.model';

import { FacultyFormComponent } from '../../components/faculty-form/faculty-form.component';

import { DepartmentService } from '../../../department/services/department.service';

import { FacultyService } from '../../services/faculty.service';

import { ToastService } from '@app/shared/services/toast.service';


@Component({
  selector: 'app-add-faculty',

  standalone: true,

  imports: [
    CommonModule,
    ReactiveFormsModule,
    ...MATERIAL_MODULES,
    FacultyFormComponent
  ],

  templateUrl: './add-faculty.component.html',

  styleUrl: './add-faculty.component.scss'
})
export class AddFacultyComponent implements OnInit {

  // ==========================================================
  // SERVICES
  // ==========================================================

  private readonly fb =
    inject(NonNullableFormBuilder);

  private readonly router =
    inject(Router);

  private readonly departmentService =
    inject(DepartmentService);

  private readonly facultyService =
    inject(FacultyService);

  private readonly toast =
    inject(ToastService);


  // ==========================================================
  // STATE
  // ==========================================================

  loading = false;

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

      // ========================================================
      // ACCOUNT
      // ========================================================

      email: [
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],

      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8)
        ]
      ],

      mobileNumber: [
        '',
        [
          Validators.required,
          Validators.pattern(/^[6-9]\d{9}$/)
        ]
      ],


      // ========================================================
      // PERSONAL
      // ========================================================

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


      // ========================================================
      // PROFESSIONAL
      // ========================================================

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


      // ========================================================
      // PROFILE
      // ========================================================

      profileImage: [
        ''
      ],


      // ========================================================
      // ADDRESS
      // ========================================================

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
          '',
          Validators.required
        ],

        street: [
          '',
          Validators.required
        ],

        landmark: [
          ''
        ],

        addressType: [
          null as AddressType | null,
          Validators.required
        ]

      })

    });


  // ==========================================================
  // INIT
  // ==========================================================

  ngOnInit(): void {

    this.loadDepartments();

  }


  // ==========================================================
  // LOAD DEPARTMENTS
  // ==========================================================

  loadDepartments(): void {

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
  // SAVE FACULTY
  // ==========================================================

  saveFaculty(): void {

    // --------------------------------------------------------
    // PREVENT DUPLICATE REQUEST
    // --------------------------------------------------------

    if (this.loading) {
      return;
    }


    // --------------------------------------------------------
    // FORM VALIDATION
    // --------------------------------------------------------

    if (this.facultyForm.invalid) {

      this.facultyForm.markAllAsTouched();

      this.toast.error(
        'Please fix the validation errors before saving the faculty.'
      );

      return;
    }


    const form =
      this.facultyForm.getRawValue();


    // --------------------------------------------------------
    // REQUIRED ENUM / ID VALIDATION
    // --------------------------------------------------------

    if (
      !form.gender ||
      !form.designation ||
      !form.qualification ||
      !form.departmentId ||
      !form.address.villageId ||
      !form.address.addressType
    ) {

      this.facultyForm.markAllAsTouched();

      this.toast.error(
        'Please complete all required faculty information.'
      );

      return;
    }


    // ========================================================
    // REQUEST
    // ========================================================

    const request: FacultyRequest = {

      // ACCOUNT

      email:
        form.email.trim(),

      password:
        form.password.trim(),

      mobileNumber:
        form.mobileNumber.trim(),


      // PERSONAL

      firstName:
        form.firstName.trim(),

      middleName:
        form.middleName.trim(),

      lastName:
        form.lastName.trim(),

      gender:
        form.gender,

      dateOfBirth:
        form.dateOfBirth,


      // PROFESSIONAL

      joiningDate:
        form.joiningDate,

      designation:
        form.designation,

      qualification:
        form.qualification,

      salary:
        form.salary,

      departmentId:
        form.departmentId,


      // PROFILE

      profileImage:
        form.profileImage.trim() || null,


      // ======================================================
      // ADDRESS
      // ======================================================

      address: {

        villageId:
          form.address.villageId,

        houseNumber:
          form.address.houseNumber.trim() || null,

        street:
          form.address.street.trim() || null,

        landmark:
          form.address.landmark.trim() || null,

        addressType:
          form.address.addressType

      }

    };


    // ========================================================
    // START REQUEST
    // ========================================================

    this.loading = true;


    this.facultyService
      .createFaculty(request)
      .subscribe({

        // ====================================================
        // SUCCESS
        // ====================================================

        next: response => {

          this.loading = false;

          this.toast.success(
            response?.message ??
            'Faculty created successfully.'
          );

          this.router.navigate([
            '/dashboard/faculty'
          ]);

        },


        // ====================================================
        // ERROR
        // ====================================================

        error: error => {

          this.loading = false;

          console.error(
            'Faculty Create Failed:',
            error
          );

          this.toast.error(
            error?.error?.message ??
            'Unable to save faculty.'
          );

        }

      });

  }


  // ==========================================================
  // CANCEL
  // ==========================================================

  cancel(): void {

    if (this.loading) {
      return;
    }

    this.router.navigate([
      '/dashboard/faculty'
    ]);

  }

}