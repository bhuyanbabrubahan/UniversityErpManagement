export interface Timetable {
  id: number;

  code: string;
  academicYear: string;
  semester: string;
  course: string;
  section: string;
  subject: string;
  faculty: string;
  room: string;
  dayOfWeek: string;
  startTime: string;
  endTime: string;
  period: string;
  status: string;
  description: string;

  createdBy?: string;
  updatedBy?: string;
  createdAt?: string;
  updatedAt?: string;
}
