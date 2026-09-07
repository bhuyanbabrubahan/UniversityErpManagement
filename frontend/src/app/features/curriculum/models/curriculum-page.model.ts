import { Curriculum } from "./curriculum.model";

export interface CurriculumPage {
  content: Curriculum[];

  totalElements: number;
  totalPages: number;

  number: number;
  size: number;

  first: boolean;
  last: boolean;

  numberOfElements?: number;
  empty: boolean;
}
