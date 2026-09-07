import { AddressRequest } from '../../../shared/models/address-request.model';
import { BloodGroup } from '../../../shared/enums/blood-group.enum';
import { Gender } from '../../../shared/enums/gender.enum';
import { StudentCategory } from '../../../shared/enums/student-category.enum';
import { StudentStatus } from '../../../shared/enums/student-status.enum';
import { StudentType } from '../../../shared/enums/student-type.enum';

export interface StudentRequest {
  // =====================================================
  // USER INFORMATION
  // =====================================================

  email: string;

  /**
   * Required while creating a student.
   * Optional while updating.
   *
   * Empty/undefined during update means:
   * existing password remains unchanged.
   */
  password?: string;

  /**
   * Not currently required by backend.
   */
  confirmPassword?: string;

  mobileNumber: string;

  // =====================================================
  // PERSONAL INFORMATION
  // =====================================================

  firstName: string;
  middleName?: string | null;
  lastName: string;
  gender: Gender;
  dateOfBirth: string;
  bloodGroup?: BloodGroup | null;
  category?: StudentCategory | null;
  religion?: string | null;
  nationality?: string | null;

  // =====================================================
  // CONTACT INFORMATION
  // =====================================================

  alternateMobile?: string | null;
  alternateEmail?: string | null;

  // =====================================================
  // IDENTITY DOCUMENTS
  // =====================================================

  aadhaarNumber?: string | null;
  panNumber?: string | null;

  // =====================================================
  // PROFILE
  // =====================================================

  profileImage?: string | null;
  signatureImage?: string | null;

  // =====================================================
  // ADMISSION INFORMATION
  // =====================================================

  registrationNumber?: string | null;
  admissionNumber?: string | null;
  admissionDate: string;
  academicYear?: string | null;

  // =====================================================
  // ACADEMIC INFORMATION
  // =====================================================

  departmentId: number;
  courseId?: number | null;
  semesterId?: number | null;
  section?: string | null;
  batch?: string | null;
  currentYear?: number | null;

  // =====================================================
  // STUDENT TYPE / STATUS
  // =====================================================

  studentType?: StudentType | null;
  studentStatus?: StudentStatus | null;

  // =====================================================
  // FEES
  // =====================================================

  fees?: number | null;

  // =====================================================
  // ADDRESS
  // =====================================================

  address: AddressRequest;
}