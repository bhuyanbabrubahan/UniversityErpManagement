import { Hod } from "./hod.model";

export type HodRequest = Omit<
  Hod,
  | "id"
  | "createdByUserId"
  | "createdBy"
  | "createdAt"
  | "updatedByUserId"
  | "updatedBy"
  | "updatedAt"
>;
