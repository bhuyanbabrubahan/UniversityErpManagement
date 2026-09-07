import { Transport } from './transport.model';
export type TransportRequest = Omit<Transport, 'id'>;
