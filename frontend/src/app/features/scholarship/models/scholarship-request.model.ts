import { Scholarship } from './scholarship.model';
export type ScholarshipRequest = Omit<Scholarship, 'id'>;
