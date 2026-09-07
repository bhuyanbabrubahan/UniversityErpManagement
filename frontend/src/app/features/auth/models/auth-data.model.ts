export interface AuthData {

  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  email: string;
  role: string;
  mobileNumber: string;
  status: string;

  // Verification Status
  emailVerified: boolean;
  mobileVerified: boolean;

}