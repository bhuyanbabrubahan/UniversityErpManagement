import { AddressType } from '../../../shared/enums/address-type.enum';
import { Gender } from '../../../shared/enums/gender.enum';
import { BloodGroup } from '../../../shared/enums/blood-group.enum';
import { StudentCategory } from '../../../shared/enums/student-category.enum';
import { StudentStatus } from '../../../shared/enums/student-status.enum';
import { StudentType } from '../../../shared/enums/student-type.enum';
import { Designation } from '../../../shared/enums/designation.enum';
import { Qualification } from '../../../shared/enums/qualification.enum';
import { AdminDesignation } from '../../../shared/enums/admin-designation.enum';
import { SuperAdminDesignation } from '../../../shared/enums/super-admin-designation.enum';


export interface UpdateProfileRequest {

  // COMMON PERSONAL INFORMATION

  firstName: string;
  middleName?: string;
  lastName: string;

  mobileNumber: string;

  gender?: Gender;
  dateOfBirth?: string;

  // EXTRA PERSONAL INFORMATION

  bloodGroup?: BloodGroup;
  category?: StudentCategory;
  religion?: string;
  nationality?: string;

  alternateMobile?: string;
  alternateEmail?: string;

  aadhaarNumber?: string;
  panNumber?: string;

  // STUDENT INFORMATION

  academicYear?: string;
  section?: string;
  batch?: string;
  currentYear?: number | null;

  studentType?: StudentType;
  studentStatus?: StudentStatus;

  // FACULTY INFORMATION

  employeeCode?: string;
  facultyDesignation?: Designation | null;
  qualification?: Qualification | null;
  joiningDate?: string;

  // ADMIN INFORMATION

  adminName?: string;
  adminDesignation?: string;

  // SUPER ADMIN INFORMATION

  superAdminDesignation?: string;

  // ADDRESS

  houseNumber: string;
  street: string;
  landmark?: string;

  addressType: AddressType;
  villageId: number;

  // PROFILE

  signatureImage?: string;
}