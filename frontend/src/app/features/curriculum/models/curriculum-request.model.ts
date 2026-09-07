import { Curriculum } from "./curriculum.model";

export type CurriculumRequest = Omit<
  Curriculum,
  "id" | "createdBy" | "updatedBy"
>;
