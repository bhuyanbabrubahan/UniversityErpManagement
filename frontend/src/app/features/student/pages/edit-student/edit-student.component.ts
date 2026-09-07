import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { MATERIAL_MODULES } from '../../../../shared/material/material';

import { AddressType } from '../../../../shared/enums/address-type.enum';
import { Gender } from '../../../../shared/enums/gender.enum';
import { StudentType } from '../../../../shared/enums/student-type.enum';
import { StudentStatus } from '../../../../shared/enums/student-status.enum';

import { StudentFormComponent } from '../../components/student-form/student-form.component';
import { StudentRequest } from '../../models/student-request.model';

import { DepartmentService } from '../../../department/services/department.service';
import { StudentService } from '../../services/student.service';

import { ToastService } from '@app/shared/services/toast.service';

@Component({
  selector: 'app-edit-student',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ...MATERIAL_MODULES,
    StudentFormComponent
  ],
  templateUrl: './edit-student.component.html',
  styleUrl: './edit-student.component.scss'
})
export class EditStudentComponent implements OnInit {

  // ==========================================================
  // SERVICES
  // ==========================================================

  private readonly fb =
    inject(NonNullableFormBuilder);

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly studentService =
    inject(StudentService);

  private readonly departmentService =
    inject(DepartmentService);

  private readonly toast =
    inject(ToastService);


  // ==========================================================
  // STATE
  // ==========================================================

  loading = false;

  saving = false;

  studentId = 0;

  departments: any[] = [];


  // ==========================================================
  // STUDENT FORM
  // ==========================================================

  studentForm = this.fb.group({

    // ========================================================
    // ACCOUNT INFORMATION
    // ========================================================

    email: [
      '',
      [
        Validators.required,
        Validators.email
      ]
    ],

    password: [''],

    mobileNumber: [
      '',
      [
        Validators.required,
        Validators.pattern(/^[0-9]{10}$/)
      ]
    ],

    alternateMobile: [
      '',
      Validators.pattern(/^[0-9]{10}$/)
    ],

    alternateEmail: [
      '',
      Validators.email
    ],


    // ========================================================
    // PERSONAL INFORMATION
    // ========================================================

    firstName: [
      '',
      Validators.required
    ],

    middleName: [''],

    lastName: [
      '',
      Validators.required
    ],

    gender: [
      Gender.MALE,
      Validators.required
    ],

    category:
      this.fb.control<string | null>(null),

    bloodGroup:
      this.fb.control<string | null>(null),

    religion: [''],

    nationality: [''],

    dateOfBirth: [
      '',
      Validators.required
    ],


    // ========================================================
    // ADMISSION INFORMATION
    // ========================================================

    admissionDate: [
      '',
      Validators.required
    ],

    academicYear: [''],


    // ========================================================
    // ACADEMIC INFORMATION
    // ========================================================

    departmentId: [
      0,
      Validators.required
    ],

    courseId: [0],

    currentSemesterId: [0],

    section: [''],

    batch: [''],

    currentYear:
      this.fb.control<number | null>(null),

    studentType:
      this.fb.control<StudentType | null>(null),

    studentStatus:
      this.fb.control<StudentStatus | null>(null),


    // ========================================================
    // PROFILE / DOCUMENT INFORMATION
    // ========================================================

    profileImage: [''],

    signatureImage: [''],

    aadhaarNumber: [
      '',
      Validators.pattern(/^[0-9]{12}$/)
    ],

    panNumber: [
      '',
      Validators.pattern(
        /^[A-Z]{5}[0-9]{4}[A-Z]{1}$/
      )
    ],


    // ========================================================
    // ADDRESS INFORMATION
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

      landmark: [''],

      addressType: [
        AddressType.PERMANENT,
        Validators.required
      ]

    })

  });


  // ==========================================================
  // INIT
  // ==========================================================

  ngOnInit(): void {

    const id =
      this.route.snapshot.paramMap.get('id');

    this.studentId = Number(id);

    if (!this.studentId || this.studentId <= 0) {

      this.toast.error(
        'Invalid student ID.'
      );

      this.router.navigate([
        '/dashboard/students'
      ]);

      return;
    }

    this.loadDepartments();
    this.loadStudent();
  }


  // ==========================================================
  // LOAD DEPARTMENTS
  // ==========================================================

  loadDepartments(): void {

    this.departmentService
      .getAllDepartments()
      .subscribe({

        next: (response) => {

          this.departments =
            response?.data?.content ??
            response?.data ??
            [];

        },

        error: (error) => {

          console.error(
            'Unable to load departments:',
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
  // LOAD STUDENT
  // ==========================================================

  loadStudent(): void {

    this.loading = true;

    this.studentService
      .getStudentById(this.studentId)
      .subscribe({

        next: (response) => {

          const student =
            response?.data;

          if (!student) {

            this.loading = false;

            this.toast.error(
              'Student information not found.'
            );

            this.router.navigate([
              '/dashboard/students'
            ]);

            return;
          }


          // ==================================================
          // ACCOUNT + PERSONAL + ACADEMIC
          // ==================================================

          this.studentForm.patchValue({

            // ACCOUNT

            email:
              student.user?.email ?? '',

            mobileNumber:
              student.user?.mobileNumber ?? '',

            // Password is never populated
            password: '',


            // PERSONAL

            alternateMobile:
              student.alternateMobile ?? '',

            alternateEmail:
              student.alternateEmail ?? '',

            firstName:
              student.firstName ?? '',

            middleName:
              student.middleName ?? '',

            lastName:
              student.lastName ?? '',

            gender:
              student.gender ?? Gender.MALE,

            category:
              student.category ?? null,

            bloodGroup:
              student.bloodGroup ?? null,

            religion:
              student.religion ?? '',

            nationality:
              student.nationality ?? '',

            dateOfBirth:
              student.dateOfBirth ?? '',


            // ADMISSION

            admissionDate:
              student.admissionDate ?? '',

            academicYear:
              student.academicYear ?? '',


            // ACADEMIC

            departmentId:
              student.department?.id ?? 0,

            courseId:
              student.course?.id ?? 0,

            currentSemesterId:
              student.currentSemester?.id ?? 0,

            section:
              student.section ?? '',

            batch:
              student.batch ?? '',

            currentYear:
              student.currentYear ?? null,

            studentType:
              student.studentType ?? null,

            studentStatus:
              student.studentStatus ?? null,


            // DOCUMENTS

            profileImage:
              student.profileImage ?? '',

            signatureImage:
              student.signatureImage ?? '',

            aadhaarNumber:
              student.aadhaarNumber ?? '',

            panNumber:
              student.panNumber ?? ''

          });


          // ==================================================
          // ADDRESS
          // ==================================================

          const address =
            this.studentForm.get('address');

          address?.patchValue({

            countryId:
              student.address?.countryId ?? 0,

            stateId:
              student.address?.stateId ?? 0,

            districtId:
              student.address?.districtId ?? 0,

            tehsilId:
              student.address?.tehsilId ?? 0,

            villageId:
              student.address?.villageId ?? 0,

            pincode:
              student.address?.pincode ?? '',

            houseNumber:
              student.address?.houseNumber ?? '',

            street:
              student.address?.street ?? '',

            landmark:
              student.address?.landmark ?? '',

            addressType:
              student.address?.addressType
                ? student.address.addressType as AddressType
                : AddressType.PERMANENT

          });

          this.loading = false;

        },

        error: (error) => {

          this.loading = false;

          console.error(
            'Unable to load student:',
            error
          );

          this.toast.error(
            error?.error?.message ??
            'Unable to load student.'
          );

          this.router.navigate([
            '/dashboard/students'
          ]);

        }

      });

  }


  // ==========================================================
  // UPDATE STUDENT
  // ==========================================================

  updateStudent(): void {

    // Prevent duplicate request

    if (this.saving) {
      return;
    }


    // Validation

    if (this.studentForm.invalid) {

      this.studentForm.markAllAsTouched();

      this.toast.error(
        'Please fix the validation errors before updating the student.'
      );

      return;
    }


    this.saving = true;


    const form =
      this.studentForm.getRawValue();


    // ========================================================
    // REQUEST OBJECT
    // ========================================================

    const request: StudentRequest = {

      // USER / ACCOUNT

      email:
        form.email.trim(),

      mobileNumber:
        form.mobileNumber,

      password:
        form.password || '',


      // PERSONAL

      alternateMobile:
        form.alternateMobile || undefined,

      alternateEmail:
        form.alternateEmail || undefined,

      firstName:
        form.firstName.trim(),

      middleName:
        form.middleName || undefined,

      lastName:
        form.lastName.trim(),

      gender:
        form.gender,

      category:
        form.category as StudentRequest['category'],

      bloodGroup:
        form.bloodGroup as StudentRequest['bloodGroup'],

      religion:
        form.religion || undefined,

      nationality:
        form.nationality || undefined,

      dateOfBirth:
        form.dateOfBirth,


      // ADMISSION

      admissionDate:
        form.admissionDate,

      academicYear:
        form.academicYear,


      // ACADEMIC

      departmentId:
        form.departmentId,

      courseId:
        form.courseId || undefined,

      semesterId:
        form.currentSemesterId || undefined,

      section:
        form.section || undefined,

      batch:
        form.batch || undefined,

      currentYear:
        form.currentYear ?? undefined,

      studentType:
        form.studentType ?? undefined,

      studentStatus:
        form.studentStatus ?? undefined,


      // PROFILE / DOCUMENTS

      profileImage:
        form.profileImage || null,

      signatureImage:
        form.signatureImage || null,

      aadhaarNumber:
        form.aadhaarNumber || undefined,

      panNumber:
        form.panNumber || undefined,


      // ADDRESS

      address: {

        houseNumber:
          form.address.houseNumber,

        street:
          form.address.street,

        landmark:
          form.address.landmark || undefined,

        addressType:
          form.address.addressType as AddressType,

        villageId:
          form.address.villageId

      }

    };


    // ========================================================
    // API CALL
    // ========================================================

    this.studentService
      .updateStudent(
        this.studentId,
        request
      )
      .subscribe({

        // ====================================================
        // SUCCESS
        // ====================================================

        next: (response) => {

          this.saving = false;

          this.toast.success(
            response?.message ??
            'Student updated successfully.'
          );

          this.router.navigate([
            '/dashboard/students'
          ]);

        },


        // ====================================================
        // ERROR
        // ====================================================

        error: (error) => {

          this.saving = false;

          console.error(
            'Update Student Error:',
            error
          );

          this.toast.error(
            error?.error?.message ??
            'Unable to update student.'
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
      '/dashboard/students'
    ]);

  }

}