import { AddressType } from "../../../shared/enums/address-type.enum";
import { AdminDesignation } from "../../../shared/enums/admin-designation.enum";
import { BloodGroup } from "../../../shared/enums/blood-group.enum";
import { Designation } from "../../../shared/enums/designation.enum";
import { Gender } from "../../../shared/enums/gender.enum";
import { Qualification } from "../../../shared/enums/qualification.enum";
import { StudentCategory } from "../../../shared/enums/student-category.enum";
import { StudentStatus } from "../../../shared/enums/student-status.enum";
import { StudentType } from "../../../shared/enums/student-type.enum";
import { SuperAdminDesignation } from "../../../shared/enums/super-admin-designation.enum";

export interface ProfileResponse {

  // COMMON USER INFORMATION
  userId: number;
  email: string;
  role: string;
  mobileNumber: string | null;
  mobileVerified: boolean;
  emailVerified: boolean;

  // COMMON PERSONAL INFORMATION
  firstName: string | null;
  middleName: string | null;
  lastName: string | null;
  fullName: string | null;
  gender: Gender | null;
  dateOfBirth: string | null;

  // EXTRA PERSONAL INFORMATION
  bloodGroup: BloodGroup | null;
  category: StudentCategory | null;
  religion: string | null;
  nationality: string | null;
  alternateMobile: string | null;
  alternateEmail: string | null;
  aadhaarNumber: string | null;
  panNumber: string | null;

  // PROFILE
  profileImage: string | null;
  signatureImage: string | null;

  // STUDENT INFORMATION
  studentId: number | null;
  studentCode: string | null;
  rollNumber: string | null;
  registrationNumber: string | null;
  admissionNumber: string | null;
  admissionDate: string | null;

  // ACADEMIC INFORMATION
  academicYear: string | null;
  section: string | null;
  batch: string | null;
  currentYear: number | null;
  studentType: StudentType | null;
  studentStatus: StudentStatus | null;

  courseId: number | null;
  courseName: string | null;
  currentSemesterId: number | null;
  semesterName: string | null;

  // DEPARTMENT
  departmentId: number | null;
  departmentName: string | null;

  // FACULTY INFORMATION
  facultyId: number | null;
  employeeCode: string | null;
  facultyDesignation?: Designation | null;
  qualification: Qualification | null;
  joiningDate: string | null;

  // ADMIN INFORMATION
  adminId: number | null;
  adminDesignation: AdminDesignation | null;

  // SUPER ADMIN INFORMATION
  superAdminId: number | null;
  superAdminDesignation: SuperAdminDesignation | null;

  // ADDRESS INFORMATION
  addressId: number | null;
  houseNumber: string | null;
  street: string | null;
  landmark: string | null;
  addressType: AddressType | null;

  // LOCATION
  countryId: number | null;
  stateId: number | null;
  districtId: number | null;
  tehsilId: number | null;
  villageId: number | null;
  villageName: string | null;
  pincode: string | null;
  tehsilName: string | null;
  districtName: string | null;
  stateName: string | null;
  countryName: string | null;

  // AUDIT
  createdByUserId: number | null;
  createdBy: string | null;
  createdDate: string | null;
  createdAt: string | null;
  updatedByUserId: number | null;
  updatedBy: string | null;
  updatedDate: string | null;
  updatedAt: string | null;
}