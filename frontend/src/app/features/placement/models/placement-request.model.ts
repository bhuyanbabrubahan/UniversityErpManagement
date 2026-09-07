import { Placement } from './placement.model';
export type PlacementRequest = Omit<Placement, 'id'>;
