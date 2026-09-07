export interface Hod {
  id: number;

  code: string;
  employeeCode: string;
  fullName: string;
  department: string;
  email: string;
  phone: string;
  designation: string;
  status: string;
  description: string;

  // Audit
  createdByUserId: number | null;
  createdBy: string | null;
  updatedByUserId: number | null;
  updatedBy: string | null;
  createdAt: string | null;
  updatedAt: string | null;
}
