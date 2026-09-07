export interface Curriculum {
  id: number;

  code: string;
  academicYear: string;
  course: string;
  semester: string;
  subject: string;

  credits: string | number;
  contactHours: string | number;

  status: string;
  description: string;

  // ================================
  // Audit Information
  // ================================

  createdByUserId?: number | null;
  createdBy?: string | null;
  createdAt?: string | null;

  updatedByUserId?: number | null;
  updatedBy?: string | null;
  updatedAt?: string | null;
}
