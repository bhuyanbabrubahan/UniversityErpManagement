export interface NotificationRequest {
  code: string;
  title: string;
  message: string;
  targetRole: string;
  channel: string;
  publishedAt: string | null;
  status: string;
  description: string;
}
