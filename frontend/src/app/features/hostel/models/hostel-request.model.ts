import { Hostel } from './hostel.model';
export type HostelRequest = Omit<Hostel, 'id'>;
