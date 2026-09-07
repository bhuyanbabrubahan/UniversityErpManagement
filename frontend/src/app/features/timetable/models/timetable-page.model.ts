import { Timetable } from "./timetable.model";

export interface TimetablePage {
  content: Timetable[];

  totalElements: number;
  totalPages: number;

  size: number;
  number: number;

  first: boolean;
  last: boolean;
  empty: boolean;

  numberOfElements: number;
}
