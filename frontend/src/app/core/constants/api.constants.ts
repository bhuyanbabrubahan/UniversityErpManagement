import { environment } from '../../../environments/environment';

const BASE_URL = environment.apiUrl;

export const API = {

  AUTH: `${BASE_URL}/auth`,

AUTH_API: {
  LOGIN: `${BASE_URL}/login`,
  REGISTER: `${BASE_URL}/auth/register`,
  LOGOUT: `${BASE_URL}/auth/logout`,
  REFRESH_TOKEN: `${BASE_URL}/auth/refresh-token`,
  FORGOT_PASSWORD: `${BASE_URL}/auth/forgot-password`,
  RESET_PASSWORD: `${BASE_URL}/auth/reset-password`,
  VERIFY_EMAIL: `${BASE_URL}/auth/verify-email`,
  RESEND_VERIFICATION: `${BASE_URL}/auth/resend-verification`,
  SEND_MOBILE_OTP: `${BASE_URL}/auth/mobile/send`,
  VERIFY_MOBILE_OTP: `${BASE_URL}/auth/mobile/verify`
},
  STUDENTS: `${BASE_URL}/students`,

  FACULTIES: `${BASE_URL}/faculties`,

  DEPARTMENTS: `${BASE_URL}/departments`,

  COURSES: `${BASE_URL}/courses`,

  SEMESTERS: `${BASE_URL}/semesters`,

  SUBJECTS: `${BASE_URL}/subjects`,

  ADMISSIONS: `${BASE_URL}/admissions`,

  ATTENDANCE: `${BASE_URL}/attendances`,

  EXAMS: `${BASE_URL}/exams`,

  RESULTS: `${BASE_URL}/results`,

  PROFILE: `${BASE_URL}/profile`,

  DASHBOARD: `${BASE_URL}/dashboard`,

  FEE: {
    REPORTS: `${BASE_URL}/v1/fee-reports`,
    REFUNDS: `${BASE_URL}/v1/fee-refunds`,
    STRUCTURES: `${BASE_URL}/v1/fee-structures`,
    PAYMENTS: `${BASE_URL}/v1/student-fee-payments`,
    RECEIPTS: `${BASE_URL}/v1/fee-receipts`
  }

} as const;