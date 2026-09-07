import { Gender } from '../../../shared/enums/gender.enum';
import { BloodGroup } from '../../../shared/enums/blood-group.enum';
import { StudentCategory } from '../../../shared/enums/student-category.enum';
import { StudentStatus } from '../../../shared/enums/student-status.enum';
import { StudentType } from '../../../shared/enums/student-type.enum';
import { RecordStatus } from '../../../shared/enums/record-status.enum';

export interface StudentResponse {
  // =====================================================
  // IDENTITY
  // =====================================================

  id: number;
  studentCode: string;
  rollNumber: string;
  registrationNumber: string;
  admissionNumber: string;

  // =====================================================
  // PERSONAL INFORMATION
  // =====================================================

  firstName: string;
  middleName: string;
  lastName: string;
  fullName: string;
  gender: Gender;
  dateOfBirth: string;
  bloodGroup: BloodGroup;
  category: StudentCategory;
  religion: string;
  nationality: string;

  // =====================================================
  // USER ACCOUNT
  // =====================================================

  user: UserResponse;

  // =====================================================
  // CONTACT
  // =====================================================

  alternateMobile: string;
  alternateEmail: string;

  // =====================================================
  // DOCUMENTS
  // =====================================================

  aadhaarNumber: string;
  panNumber: string;
  signatureImage: string | null;
  profileImage: string | null;

  // =====================================================
  // ADMISSION
  // =====================================================

  admissionDate: string;
  academicYear: string;

  // =====================================================
  // ACADEMIC
  // =====================================================

  department: DepartmentResponse;
  course: CourseResponse | null;
  currentSemester: SemesterResponse | null;
  section: string;
  batch: string;
  currentYear: number;
  studentType: StudentType;
  studentStatus: StudentStatus;

  // =====================================================
  // ADDRESS
  // =====================================================

  address: AddressResponse | null;

  // =====================================================
  // STATUS
  // =====================================================

  status: RecordStatus;

  // =====================================================
  // AUDIT
  // =====================================================

  createdByUserId: number | null;
  createdBy: string | null;
  createdAt: string | null;
  updatedByUserId: number | null;
  updatedBy: string | null;
  updatedAt: string | null;
}

// =====================================================
// USER
// =====================================================

export interface UserResponse {
  id: number;
  email: string;
  mobileNumber: string;
  role: string;
  status: string;
  emailVerified: boolean;
  mobileVerified: boolean;
  failedLoginAttempts: number;
  accountLockedUntil: string | null;
}

// =====================================================
// DEPARTMENT
// =====================================================

export interface DepartmentResponse {
  id: number;
  departmentName: string;
  departmentCode: string;
  description: string | null;
  status: string;
  createdAt: string | null;
  updatedAt: string | null;
  createdByUserId: number | null;
  createdBy: string | null;
  createdDate: string | null;
  updatedByUserId: number | null;
  updatedBy: string | null;
  updatedDate: string | null;
}

// =====================================================
// COURSE
// =====================================================

export interface CourseResponse {
  id: number;
  courseCode: string;
  courseName: string;
  durationInMonths: number;
  description: string | null;
  status: string;
  departmentId: number;
  departmentName: string;
}

// =====================================================
// SEMESTER
// =====================================================

export interface SemesterResponse {
  id: number;
  semesterName: string;
  semesterNumber: number;
  semesterStartDate: string;
  semesterEndDate: string;
  totalWorkingDays: number;
  courseId: number;
  courseCode: string;
  courseName: string;
  status: string;
}

// =====================================================
// ADDRESS
// =====================================================

export interface AddressResponse {
  id: number;
  houseNumber: string;
  street: string;
  landmark: string | null;
  addressType: string;
  villageId: number;
  village: string;
  tehsilId: number;
  tehsil: string;
  districtId: number;
  district: string;
  stateId: number;
  state: string;
  countryId: number;
  country: string;
  pincode: string | null;
  createdAt: string | null;
  updatedAt: string | null;
}