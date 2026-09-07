import { SubjectResponse } from '../models/subject.model';


// =====================================================
// DISPLAY NAME
// =====================================================

export function getSubjectDisplayName(
  subject: SubjectResponse
): string {

  if (subject.subjectCode) {

    return `${subject.subjectCode} - ${subject.subjectName}`;

  }

  return subject.subjectName;
}


// =====================================================
// STATUS LABEL
// =====================================================

export function formatSubjectStatus(
  status: string | null | undefined
): string {

  if (!status) {

    return '-';

  }

  return status
    .replace(/_/g, ' ')
    .toLowerCase()
    .replace(/\b\w/g, char => char.toUpperCase());
}


// =====================================================
// MARKS
// =====================================================

export function getTotalMarks(
  subject: SubjectResponse
): number {

  return (
    subject.theoryMarks ?? 0
  ) + (
    subject.practicalMarks ?? 0
  );
}


// =====================================================
// MARKS DISPLAY
// =====================================================

export function getMarksDisplay(
  subject: SubjectResponse
): string {

  const theory =
    subject.theoryMarks ?? 0;

  const practical =
    subject.practicalMarks ?? 0;

  return `${theory} + ${practical}`;
}


// =====================================================
// DATE
// =====================================================

export function formatSubjectDate(
  value: string | null | undefined
): string {

  if (!value) {

    return '-';

  }

  const date = new Date(value);

  if (Number.isNaN(date.getTime())) {

    return value;

  }

  return date.toLocaleDateString(
    'en-IN',
    {
      day: '2-digit',
      month: 'short',
      year: 'numeric'
    }
  );
}