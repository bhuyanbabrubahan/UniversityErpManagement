import { Grievance } from './grievance.model';
export type GrievanceRequest = Omit<Grievance, 'id'>;
