export interface Notification {
  id?: number;
  code?: string;
  title?: string;
  message?: string;
  targetRole?: string;
  channel?: string;
  publishedAt?: string | null;
  status?: string;
  description?: string;
  createdByUserId?: number | null;
  createdBy?: string | null;
  updatedByUserId?: number | null;
  updatedBy?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
}
