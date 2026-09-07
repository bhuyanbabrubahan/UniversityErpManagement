import { AddressType } from '../../../shared/enums/address-type.enum';
import { Designation } from '../../../shared/enums/designation.enum';
import { Gender } from '../../../shared/enums/gender.enum';
import { Qualification } from '../../../shared/enums/qualification.enum';

export interface FacultyResponse {

  // ==========================
  // FACULTY
  // ==========================

  id: number;
  userId: number;
  employeeCode: string;

  // ==========================
  // PERSONAL INFORMATION
  // ==========================

  firstName: string;
  lastName: string;
  middleName: string;
  fullName: string;

  gender: Gender;
  dateOfBirth: string;
  joiningDate: string;

  // ==========================
  // PROFESSIONAL INFORMATION
  // ==========================

  designation: Designation;
  qualification: Qualification;
  salary: number;

  profileImage?: string | null;
  signatureImage?: string | null;

  // ==========================
  // CONTACT
  // ==========================

  email: string;
  mobileNumber: string;

  // ==========================
  // DEPARTMENT
  // ==========================

  departmentId: number;
  departmentName: string;

  // ==========================
  // STATUS / USER
  // ==========================

  status: string;
  userStatus: string;
  role: string;

  // ==========================
  // ADDRESS
  // ==========================

  addressId: number;

  houseNumber?: string | null;
  street?: string | null;
  landmark?: string | null;
  addressType?: AddressType | null;

  // ==========================
  // LOCATION
  // ==========================

  villageId: number;
  villageName: string;

  tehsilId: number;
  tehsilName: string;

  districtId: number;
  districtName: string;

  stateId: number;
  stateName: string;

  countryId: number;
  countryName: string;

  pincode: string;

  // ==========================
  // AUDIT
  // ==========================

  createdAt: string;
  updatedAt: string;

  createdByUserId?: number | null;
  createdBy?: string | null;
  createdDate?: string | null;

  updatedByUserId?: number | null;
  updatedBy?: string | null;
  updatedDate?: string | null;
}