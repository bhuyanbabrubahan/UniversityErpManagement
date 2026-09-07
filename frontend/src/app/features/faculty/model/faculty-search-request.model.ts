import { Designation } from '../../../shared/enums/designation.enum';
import { Qualification } from '../../../shared/enums/qualification.enum';

export interface FacultySearchRequest {

  id?: number | null;

  employeeCode?: string | null;

  firstName?: string | null;

  lastName?: string | null;

  fullName?: string | null;

  departmentId?: number | null;

  designation?: Designation | null;

  qualification?: Qualification | null;

  status?: string | null;
}