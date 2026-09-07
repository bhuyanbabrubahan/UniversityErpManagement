import { Installment } from './installment.model';
export type InstallmentRequest = Omit<Installment, 'id'>;
