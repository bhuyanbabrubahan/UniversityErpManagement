export interface TimetableRequest {
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
}
