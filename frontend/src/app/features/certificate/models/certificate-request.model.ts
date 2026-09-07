import { Certificate } from './certificate.model';
export type CertificateRequest = Omit<Certificate, 'id'>;
