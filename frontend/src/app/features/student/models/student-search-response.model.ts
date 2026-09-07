import { Student } from './student.model';

export interface StudentSearchResponse {

    content: Student[];

    totalElements: number;

    totalPages: number;

    pageNumber: number;

    pageSize: number;

    first: boolean;

    last: boolean;

}