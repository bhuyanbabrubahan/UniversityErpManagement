import { Component, OnInit, inject } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialog } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';

import { CurrentUserService } from '../../../../core/services/current-user.service';
import { ToastService } from '../../../../shared/services/toast.service';

import { ChangePasswordDialogComponent } from '../../components/change-password-dialog/change-password-dialog.component';
import { ProfileResponse } from '../../models/profile-response.model';
import { UpdateProfileRequest } from '../../models/update-profile-request.model';
import { ProfileService } from '../../services/profile.service';

import { API } from '../../../../core/constants/api.constants';

import { StudentCategory } from '../../../../shared/enums/student-category.enum';
import { BloodGroup } from '../../../../shared/enums/blood-group.enum';
import { Gender } from '../../../../shared/enums/gender.enum';
import { StudentType } from '../../../../shared/enums/student-type.enum';
import { AddressType } from '../../../../shared/enums/address-type.enum';
import { StudentStatus } from '../../../../shared/enums/student-status.enum';
import { Designation } from '../../../../shared/enums/designation.enum';
import { Qualification } from '../../../../shared/enums/qualification.enum';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDividerModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    MatSelectModule
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {

  private readonly currentUserService = inject(CurrentUserService);
  private readonly fb = inject(FormBuilder);
  private readonly profileService = inject(ProfileService);
  private readonly dialog = inject(MatDialog);
  private readonly toastService = inject(ToastService);

  readonly studentCategories = Object.values(StudentCategory);
  readonly bloodGroups = Object.values(BloodGroup);
  readonly genders = Object.values(Gender);
  readonly studentTypes = Object.values(StudentType);
  readonly addressTypes = Object.values(AddressType);
  readonly studentStatuses = Object.values(StudentStatus);
  readonly designations = Object.values(Designation);
  readonly qualifications = Object.values(Qualification);

  profile?: ProfileResponse;

  loading = false;
  editing = false;
  saving = false;
  uploadingImage = false;

  profileForm = this.fb.group({

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

    mobileNumber: [
      '',
      [
        Validators.required,
        Validators.pattern(/^[6-9]\d{9}$/)
      ]
    ],

    dateOfBirth: [
      ''
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
      ''
    ],

    alternateMobile: [
      ''
    ],

    alternateEmail: [
      '',
      Validators.email
    ],

    aadhaarNumber: [
      ''
    ],

    panNumber: [
      ''
    ],

    signatureImage: [
      ''
    ],

    academicYear: [
      ''
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

    employeeCode: [
      ''
    ],

    designation: [
      null as Designation | null
    ],

    qualification: [
      null as Qualification | null
    ],

    joiningDate: [
      ''
    ],

    fullName: [
      ''
    ],

    adminDesignation: [
      ''
    ],

    superAdminDesignation: [
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
    ],

    villageId: [
      null as number | null,
      Validators.required
    ]
  });

  ngOnInit(): void {

    this.loading = true;

    this.currentUserService.currentUser$
      .subscribe({
        next: user => {

          this.profile = user ?? undefined;

          this.patchProfileForm();
          this.loading = false;
        },

        error: error => {

          console.error(
            'Profile Load Failed',
            error
          );

          this.loading = false;

          this.toastService.error(
            'Unable to load profile.'
          );
        }
      });
  }

  private patchProfileForm(): void {

    if (!this.profile) {
      return;
    }

    this.profileForm.patchValue({

      firstName:
        this.profile.firstName ?? '',

      middleName:
        this.profile.middleName ?? '',

      lastName:
        this.profile.lastName ?? '',

      gender:
        this.profile.gender ?? null,

      mobileNumber:
        this.profile.mobileNumber ?? '',

      dateOfBirth:
        this.profile.dateOfBirth ?? '',

      bloodGroup:
        this.profile.bloodGroup as BloodGroup | null,

      category:
        this.profile.category as StudentCategory | null,

      religion:
        this.profile.religion ?? '',

      nationality:
        this.profile.nationality ?? '',

      alternateMobile:
        this.profile.alternateMobile ?? '',

      alternateEmail:
        this.profile.alternateEmail ?? '',

      aadhaarNumber:
        this.profile.aadhaarNumber ?? '',

      panNumber:
        this.profile.panNumber ?? '',

      signatureImage:
        this.profile.signatureImage ?? '',

      academicYear:
        this.profile.academicYear ?? '',

      section:
        this.profile.section ?? '',

      batch:
        this.profile.batch ?? '',

      currentYear:
        this.profile.currentYear ?? null,

      studentType:
        this.profile.studentType as StudentType | null,

      studentStatus:
        this.profile.studentStatus as StudentStatus | null,

      employeeCode:
        this.profile.employeeCode ?? '',

      designation:
        this.profile.facultyDesignation as Designation | null,

      qualification:
        this.profile.qualification as Qualification | null,

      joiningDate:
        this.profile.joiningDate ?? '',

      fullName:
        this.profile.fullName ?? '',

      adminDesignation:
        this.profile.adminDesignation ?? '',

      superAdminDesignation:
        this.profile.superAdminDesignation ?? '',

      houseNumber:
        this.profile.houseNumber ?? '',

      street:
        this.profile.street ?? '',

      landmark:
        this.profile.landmark ?? '',

      addressType:
        this.profile.addressType ?? null,

      villageId:
        this.profile.villageId ?? null
    });
  }

  startEdit(): void {

    this.patchProfileForm();
    this.editing = true;
  }

  cancelEdit(): void {

    this.editing = false;
    this.patchProfileForm();
  }

  updateProfile(): void {

    if (this.profileForm.invalid) {

      this.profileForm.markAllAsTouched();

      this.toastService.warning(
        'Please correct the highlighted fields.'
      );

      return;
    }

    const value = this.profileForm.getRawValue();

    if (!value.gender) {

      this.profileForm.controls.gender.markAsTouched();

      this.toastService.warning(
        'Gender is required.'
      );

      return;
    }

    if (!value.addressType) {

      this.profileForm.controls.addressType.markAsTouched();

      this.toastService.warning(
        'Address type is required.'
      );

      return;
    }

    if (!value.villageId) {

      this.profileForm.controls.villageId.markAsTouched();

      this.toastService.warning(
        'Village is required.'
      );

      return;
    }

    const request: UpdateProfileRequest = {

      firstName:
        value.firstName!,

      middleName:
        value.middleName || undefined,

      lastName:
        value.lastName!,

      mobileNumber:
        value.mobileNumber!,

      gender:
        value.gender!,

      dateOfBirth:
        value.dateOfBirth || undefined,

      houseNumber:
        value.houseNumber!,

      street:
        value.street!,

      landmark:
        value.landmark || undefined,

      addressType:
        value.addressType!,

      villageId:
        value.villageId!
    };

    if (this.isStudent) {

      Object.assign(request, {

        bloodGroup:
          value.bloodGroup || undefined,

        category:
          value.category || undefined,

        religion:
          value.religion || undefined,

        nationality:
          value.nationality || undefined,

        alternateMobile:
          value.alternateMobile || undefined,

        alternateEmail:
          value.alternateEmail || undefined,

        aadhaarNumber:
          value.aadhaarNumber || undefined,

        panNumber:
          value.panNumber || undefined,

        signatureImage:
          value.signatureImage || undefined,

        academicYear:
          value.academicYear || undefined,

        section:
          value.section || undefined,

        batch:
          value.batch || undefined,

        currentYear:
          value.currentYear ?? undefined,

        studentType:
          value.studentType || undefined,

        studentStatus:
          value.studentStatus || undefined
      });
    }

    if (this.isFaculty) {

      Object.assign(request, {

        employeeCode:
          value.employeeCode || undefined,

        facultyDesignation:
          value.designation || undefined,

        qualification:
          value.qualification || undefined,

        joiningDate:
          value.joiningDate || undefined
      });
    }

    if (this.isAdmin) {

      Object.assign(request, {

        fullName:
          value.fullName || undefined,

        adminDesignation:
          value.adminDesignation || undefined
      });
    }

    if (this.isSuperAdmin) {

      Object.assign(request, {

        superAdminDesignation:
          value.superAdminDesignation || undefined
      });
    }

    this.saving = true;

    this.profileService
      .updateProfile(request)
      .subscribe({

        next: response => {

          this.profile = response.data;

          this.currentUserService
            .setCurrentUser(response.data);

          this.patchProfileForm();

          this.editing = false;
          this.saving = false;

          this.toastService.success(
            'Profile updated successfully.'
          );
        },

        error: error => {

          console.error(
            'Profile Update Failed:',
            error
          );

          this.saving = false;

          this.toastService.error(
            error?.error?.message ??
            'Unable to update profile.'
          );
        }
      });
  }

  get displayName(): string {
    return this.profile?.fullName ?? 'User';
  }

  get displayRole(): string {
    return this.profile?.role ?? '';
  }

  get isStudent(): boolean {
    return this.profile?.role === 'STUDENT';
  }

  get isFaculty(): boolean {
    return this.profile?.role === 'FACULTY';
  }

  get isAdmin(): boolean {
    return this.profile?.role === 'ADMIN';
  }

  get isSuperAdmin(): boolean {
    return this.profile?.role === 'SUPER_ADMIN';
  }

  get departmentName(): string {
    return this.profile?.departmentName ?? '-';
  }

  get courseName(): string {
    return this.profile?.courseName ?? '-';
  }

  get semesterName(): string {
    return this.profile?.semesterName ?? '-';
  }

  get profileImage(): string {

    if (!this.profile?.profileImage) {
      return 'images/default-avatar.png';
    }

    return `${API.PROFILE}/photo/${this.profile.profileImage}`;
  }

  onImageSelected(event: Event): void {

    const input =
      event.target as HTMLInputElement;

    if (!input.files?.length) {
      return;
    }

    const file = input.files[0];

    const allowedTypes = [
      'image/jpeg',
      'image/png',
      'image/jpg'
    ];

    if (!allowedTypes.includes(file.type)) {

      this.toastService.warning(
        'Only JPG, JPEG and PNG images are allowed.'
      );

      input.value = '';
      return;
    }

    if (file.size > 2 * 1024 * 1024) {

      this.toastService.warning(
        'Maximum file size is 2 MB.'
      );

      input.value = '';
      return;
    }

    this.uploadingImage = true;

    this.profileService
      .uploadProfileImage(file)
      .subscribe({

        next: response => {

          this.uploadingImage = false;

          this.profile = response.data;

          this.currentUserService
            .setCurrentUser(response.data);

          input.value = '';

          this.toastService.success(
            'Profile image uploaded successfully.'
          );
        },

        error: error => {

          console.error(
            'Profile Image Upload Failed:',
            error
          );

          this.uploadingImage = false;
          input.value = '';

          this.toastService.error(
            error?.error?.message ??
            'Unable to upload profile image.'
          );
        }
      });
  }

  removeImage(): void {

    if (!confirm('Remove profile photo?')) {
      return;
    }

    this.profileService
      .removeProfileImage()
      .subscribe({

        next: () => {

          this.currentUserService
            .loadCurrentUser();

          this.toastService.success(
            'Profile image removed successfully.'
          );
        },

        error: error => {

          console.error(
            'Profile Image Remove Failed:',
            error
          );

          this.toastService.error(
            error?.error?.message ??
            'Unable to remove profile image.'
          );
        }
      });
  }

  openChangePasswordDialog(): void {

    this.dialog.open(
      ChangePasswordDialogComponent,
      {
        width: '100%',
        maxWidth: '420px',
        minWidth: '280px',
        maxHeight: '90vh',
        autoFocus: false,
        restoreFocus: false,
        disableClose: true,
        panelClass: 'change-password-dialog'
      }
    );
  }
}
