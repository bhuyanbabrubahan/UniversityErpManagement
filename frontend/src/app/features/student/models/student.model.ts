import { AddressType } from '../../../shared/enums/address-type.enum';
import { Gender } from '../../../shared/enums/gender.enum';
import { RecordStatus } from '../../../shared/enums/record-status.enum';
import { StudentCategory } from '../../../shared/enums/student-category.enum';
import { StudentStatus } from '../../../shared/enums/student-status.enum';
import { StudentType } from '../../../shared/enums/student-type.enum';
import { BloodGroup } from '../../../shared/enums/blood-group.enum';

export interface Student {

  // USER INFORMATION

  userId: number;
  email: string;
  mobileNumber: string;
  emailVerified: boolean;
  mobileVerified: boolean;

  // STUDENT IDENTITY

  id: number;
  studentCode: string;
  rollNumber: string;
  registrationNumber: string | null;
  admissionNumber: string | null;

  // PERSONAL INFORMATION

  firstName: string;
  middleName: string | null;
  lastName: string;
  fullName: string;

  gender: Gender;
  dateOfBirth: string;

  bloodGroup: BloodGroup | null;
  category: StudentCategory | null;
  religion: string | null;
  nationality: string | null;

  // CONTACT INFORMATION

  alternateMobile: string | null;
  alternateEmail: string | null;

  // IDENTITY DOCUMENTS

  aadhaarNumber: string | null;
  panNumber: string | null;

  // PROFILE

  profileImage: string | null;
  signatureImage: string | null;

  // ACADEMIC INFORMATION

  admissionDate: string;
  academicYear: string | null;

  departmentId: number;
  departmentName: string;

  courseId: number | null;
  courseName: string | null;

  currentSemesterId: number | null;
  semesterName: string | null;

  section: string | null;
  batch: string | null;
  currentYear: number | null;

  studentType: StudentType | null;
  studentStatus: StudentStatus | null;

  fees: number | null;

  // ADDRESS INFORMATION

  addressId: number | null;

  houseNumber: string;
  street: string;
  landmark: string | null;

  addressType: AddressType;

  countryId: number | null;
  stateId: number | null;
  districtId: number | null;
  tehsilId: number | null;
  villageId: number | null;

  village: string | null;
  tehsil: string | null;
  district: string | null;
  state: string | null;
  country: string | null;
  pincode: string | null;

  // STATUS

  status: RecordStatus;

  // AUDIT

  createdAt: string;
  updatedAt: string;

  createdByUserId: number;
  createdBy: string;
 

  updatedByUserId: number;
  updatedBy: string;
  
}

