import { Marks } from './marks.model';
export type MarksRequest = Omit<Marks, 'id'>;
