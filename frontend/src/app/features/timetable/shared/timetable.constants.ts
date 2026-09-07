export const TIMETABLE_STATUS = {
  ACTIVE: "ACTIVE",
  INACTIVE: "INACTIVE",
} as const;

export const TIMETABLE_STATUS_OPTIONS = [
  {
    value: TIMETABLE_STATUS.ACTIVE,
    label: "Active",
  },
  {
    value: TIMETABLE_STATUS.INACTIVE,
    label: "Inactive",
  },
];

export const DAYS_OF_WEEK = [
  {
    value: "MONDAY",
    label: "Monday",
  },
  {
    value: "TUESDAY",
    label: "Tuesday",
  },
  {
    value: "WEDNESDAY",
    label: "Wednesday",
  },
  {
    value: "THURSDAY",
    label: "Thursday",
  },
  {
    value: "FRIDAY",
    label: "Friday",
  },
  {
    value: "SATURDAY",
    label: "Saturday",
  },
];

export const PAGE_SIZE_OPTIONS = [5, 10, 20, 50, 100];
