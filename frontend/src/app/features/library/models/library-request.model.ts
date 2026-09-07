import { Library } from './library.model';
export type LibraryRequest = Omit<Library, 'id'>;
