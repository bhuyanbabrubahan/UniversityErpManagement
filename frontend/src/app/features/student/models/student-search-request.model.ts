import { Gender } from '../../../shared/enums/gender.enum';
import { RecordStatus } from '../../../shared/enums/record-status.enum';

export interface StudentSearchRequest {

    // Student
    id?: number;

    rollNumber?: string;

    firstName?: string;

    lastName?: string;

    fullName?: string;

    gender?: Gender;

    // Department
    departmentId?: number;

    // Address
    village?: string;

    tehsil?: string;

    district?: string;

    state?: string;

    country?: string;

    pincode?: string;

    // Admission Date Range
    admissionDateFrom?: string;

    admissionDateTo?: string;

    // Date Of Birth Range
    dateOfBirthFrom?: string;

    dateOfBirthTo?: string;

    // Fee Range
    minFees?: number;

    maxFees?: number;

    // Status
    status?: RecordStatus;

}