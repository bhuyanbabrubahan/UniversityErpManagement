import { Research } from './research.model';
export type ResearchRequest = Omit<Research, 'id'>;
