import { SemesterRequest } from '../models/semester.model';


/* =====================================================
   EMPTY REQUEST
===================================================== */

export function createEmptySemesterRequest(): SemesterRequest {

  return {
    semesterName: '',
    semesterNumber: 1,
    semesterStartDate: '',
    semesterEndDate: '',
    totalWorkingDays: null,
    courseId: 0
  };
}


/* =====================================================
   CALCULATE WORKING DAYS
===================================================== */

export function calculateWorkingDays(
  startDate: string,
  endDate: string
): number {

  if (!startDate || !endDate) {
    return 0;
  }

  const start = new Date(`${startDate}T00:00:00`);
  const end = new Date(`${endDate}T00:00:00`);

  if (
    Number.isNaN(start.getTime()) ||
    Number.isNaN(end.getTime())
  ) {
    return 0;
  }

  if (start > end) {
    return 0;
  }

  let workingDays = 0;

  const current = new Date(start);

  while (current <= end) {

    const day = current.getDay();

    if (day !== 0 && day !== 6) {
      workingDays++;
    }

    current.setDate(
      current.getDate() + 1
    );
  }

  return workingDays;
}


/* =====================================================
   NORMALIZE REQUEST
===================================================== */

export function normalizeSemesterRequest(
  request: SemesterRequest
): SemesterRequest {

  return {
    ...request,

    semesterName:
      request.semesterName?.trim() ?? '',

    totalWorkingDays:
      request.totalWorkingDays ?? null
  };
}