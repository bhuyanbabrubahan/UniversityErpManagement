import { Hod } from "./hod.model";

export interface HodPage {
  content: Hod[];

  pageable?: {
    pageNumber: number;
    pageSize: number;
    offset: number;
    paged: boolean;
    unpaged: boolean;
    sort?: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
  };

  totalElements: number;
  totalPages: number;
  last: boolean;
  size: number;
  number: number;
  sort?: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}
