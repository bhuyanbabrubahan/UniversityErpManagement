import { AddressType } from '../../../shared/enums/address-type.enum';
import { Designation } from '../../../shared/enums/designation.enum';
import { Gender } from '../../../shared/enums/gender.enum';
import { Qualification } from '../../../shared/enums/qualification.enum';

export interface FacultyRequest {

  firstName: string;
  middleName: string;
  lastName: string;

  gender: Gender;

  dateOfBirth: string;
  joiningDate: string;

  mobileNumber: string;
  email: string;

  designation: Designation;
  qualification: Qualification;

  salary: number;
  departmentId: number;

  profileImage?: string | null;

  password: string;

  address: AddressRequest;
}

export interface AddressRequest {

  houseNumber?: string | null;
  street?: string | null;
  landmark?: string | null;
  addressType?: AddressType | null;

  villageId: number;
}