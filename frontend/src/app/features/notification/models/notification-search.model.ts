export interface NotificationSearch {
  code?: string;
  status?: string;
  keyword?: string;
}

export interface NotificationPage<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}
