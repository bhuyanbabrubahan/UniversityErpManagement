import { Staff } from './staff.model';
export type StaffRequest = Omit<Staff, 'id'>;
