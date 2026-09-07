import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';

import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { Router } from '@angular/router';

import { MATERIAL_MODULES } from '../../../../shared/material/material';

import { AddressType } from '../../../../shared/enums/address-type.enum';
import { BloodGroup } from '../../../../shared/enums/blood-group.enum';
import { Gender } from '../../../../shared/enums/gender.enum';
import { StudentCategory } from '../../../../shared/enums/student-category.enum';
import { StudentStatus } from '../../../../shared/enums/student-status.enum';
import { StudentType } from '../../../../shared/enums/student-type.enum';

import { StudentRequest } from '../../models/student-request.model';

import { StudentFormComponent } from '../../components/student-form/student-form.component';

import { DepartmentService } from '../../../department/services/department.service';
import { StudentService } from '../../services/student.service';

import { ToastService } from '@app/shared/services/toast.service';


@Component({
  selector: 'app-add-student',

  standalone: true,

  imports: [
    CommonModule,
    ReactiveFormsModule,
    ...MATERIAL_MODULES,
    StudentFormComponent
  ],

  templateUrl: './add-student.component.html',

  styleUrl: './add-student.component.scss'
})
export class AddStudentComponent implements OnInit {

  // =====================================================
  // SERVICES
  // =====================================================

  private readonly fb =
    inject(NonNullableFormBuilder);

  private readonly router =
    inject(Router);

  private readonly departmentService =
    inject(DepartmentService);

  private readonly studentService =
    inject(StudentService);

  private readonly toast =
    inject(ToastService);


  // =====================================================
  // STATE
  // =====================================================

  loading = false;

  departments: any[] = [];


  // =====================================================
  // ENUM OPTIONS
  // =====================================================

  readonly genders =
    Object.values(Gender);

  readonly categories =
    Object.values(StudentCategory);

  readonly bloodGroups =
    Object.values(BloodGroup);

  readonly studentTypes =
    Object.values(StudentType);

  readonly studentStatuses =
    Object.values(StudentStatus);

  readonly addressTypes =
    Object.values(AddressType);


  // =====================================================
  // STUDENT FORM
  // =====================================================

  studentForm = this.fb.group({

    // ===================================================
    // ACCOUNT INFORMATION
    // ===================================================

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

    alternateMobile: [
      '',
      Validators.pattern(
        /^$|^[6-9]\d{9}$/
      )
    ],

    alternateEmail: [
      '',
      Validators.email
    ],


    // ===================================================
    // PERSONAL INFORMATION
    // ===================================================

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
      Gender.MALE,
      Validators.required
    ],

    dateOfBirth: [
      '',
      Validators.required
    ],

    bloodGroup: [
      null as BloodGroup | null
    ],

    category: [
      null as StudentCategory | null
    ],

    religion: [
      ''
    ],

    nationality: [
      'Indian'
    ],


    // ===================================================
    // STUDENT ACADEMIC INFORMATION
    // ===================================================

    admissionDate: [
      '',
      Validators.required
    ],

    academicYear: [
      '',
      Validators.required
    ],

    departmentId: [
      0,
      Validators.required
    ],

    courseId: [
      0
    ],

    currentSemesterId: [
      0
    ],

    section: [
      ''
    ],

    batch: [
      ''
    ],

    currentYear: [
      null as number | null
    ],

    studentType: [
      null as StudentType | null
    ],

    studentStatus: [
      null as StudentStatus | null
    ],

    fees: [
      0,
      [
        Validators.required,
        Validators.min(0)
      ]
    ],


    // ===================================================
    // DOCUMENT INFORMATION
    // ===================================================

    aadhaarNumber: [
      '',
      Validators.pattern(
        /^$|^\d{12}$/
      )
    ],

    panNumber: [
      '',
      Validators.pattern(
        /^$|^[A-Z]{5}[0-9]{4}[A-Z]$/
      )
    ],


    // ===================================================
    // FILE / IMAGE INFORMATION
    // ===================================================

    profileImage: [
      ''
    ],

    signatureImage: [
      ''
    ],


    // ===================================================
    // ADDRESS INFORMATION
    // ===================================================

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

      pincode: this.fb.control({
        value: '',
        disabled: true
      }),

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
        AddressType.PERMANENT,
        Validators.required
      ]

    })

  });


  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {

    this.loadDepartments();

  }


  // =====================================================
  // LOAD DEPARTMENTS
  // =====================================================

  loadDepartments(): void {

    this.departmentService
      .getAllDepartments()
      .subscribe({

        next: (response) => {

          this.departments =
            response.data?.content ??
            response.data ??
            [];

        },

        error: (error) => {

          console.error(
            'Department Load Failed',
            error
          );

          this.departments = [];

          this.toast.error(
            'Unable to load departments.'
          );

        }

      });

  }


  // =====================================================
  // SAVE STUDENT
  // =====================================================

  saveStudent(): void {

    // ---------------------------------------------------
    // VALIDATION
    // ---------------------------------------------------

    if (this.studentForm.invalid) {

      this.studentForm.markAllAsTouched();

      this.toast.warning(
        'Please complete all required fields.'
      );

      return;
    }


    // ---------------------------------------------------
    // LOADING
    // ---------------------------------------------------

    this.loading = true;


    // ---------------------------------------------------
    // FORM VALUE
    // ---------------------------------------------------

    const form =
      this.studentForm.getRawValue();


    // ---------------------------------------------------
    // REQUEST
    // ---------------------------------------------------

    const request: StudentRequest = {

      // =================================================
      // ACCOUNT
      // =================================================

      email:
        form.email.trim(),

      password:
        form.password,

      mobileNumber:
        form.mobileNumber,

      alternateMobile:
        form.alternateMobile ||
        undefined,

      alternateEmail:
        form.alternateEmail ||
        undefined,


      // =================================================
      // PERSONAL
      // =================================================

      firstName:
        form.firstName.trim(),

      middleName:
        form.middleName ||
        undefined,

      lastName:
        form.lastName.trim(),

      gender:
        form.gender,

      category:
        form.category ||
        undefined,

      bloodGroup:
        form.bloodGroup ||
        undefined,

      religion:
        form.religion ||
        undefined,

      nationality:
        form.nationality ||
        undefined,

      dateOfBirth:
        form.dateOfBirth,


      // =================================================
      // ACADEMIC
      // =================================================

      admissionDate:
        form.admissionDate,

      academicYear:
        form.academicYear,

      departmentId:
        form.departmentId,

      courseId:
        form.courseId ||
        undefined,

      semesterId:
        form.currentSemesterId ||
        undefined,

      section:
        form.section ||
        undefined,

      batch:
        form.batch ||
        undefined,

      currentYear:
        form.currentYear ??
        undefined,

      studentType:
        form.studentType ||
        undefined,

      studentStatus:
        form.studentStatus ||
        undefined,

      fees:
        form.fees,


      // =================================================
      // DOCUMENTS
      // =================================================

      profileImage:
        form.profileImage ||
        null,

      signatureImage:
        form.signatureImage ||
        null,

      aadhaarNumber:
        form.aadhaarNumber ||
        undefined,

      panNumber:
        form.panNumber ||
        undefined,


      // =================================================
      // ADDRESS
      // =================================================

      address: {

        houseNumber:
          form.address.houseNumber,

        street:
          form.address.street,

        landmark:
          form.address.landmark ||
          undefined,

        addressType:
          form.address.addressType,

        villageId:
          form.address.villageId

      }

    };


    // ===================================================
    // CREATE STUDENT
    // ===================================================

    this.studentService
      .createStudent(request)
      .subscribe({

        // ------------------------------------------------
        // SUCCESS
        // ------------------------------------------------

        next: (response) => {

          this.loading = false;

          this.toast.success(
            response.message ??
            'Student created successfully.'
          );

          this.router.navigate([
            '/dashboard/students'
          ]);

        },


        // ------------------------------------------------
        // ERROR
        // ------------------------------------------------

        error: (error) => {

          this.loading = false;

          console.error(
            'Student Create Failed',
            error
          );

          const message =
            error?.error?.message ??
            'Unable to save student.';

          this.toast.error(
            message
          );

        }

      });

  }


  // =====================================================
  // CANCEL
  // =====================================================

  cancel(): void {

    this.router.navigate([
      '/dashboard/students'
    ]);

  }

}