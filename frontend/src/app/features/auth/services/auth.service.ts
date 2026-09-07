import { Injectable, inject } from '@angular/core';
import {
  HttpClient
} from '@angular/common/http';

import { Router } from '@angular/router';

import {
  Observable
} from 'rxjs';

import { API } from '../../../core/constants/api.constants';

import {
  StorageService
} from '../../../core/services/storage.service';

import {
  CurrentUserService
} from '../../../core/services/current-user.service';

import {
  LoginRequest
} from '../models/login-request.model';

import {
  LoginResponse
} from '../models/login-response.model';

import {
  ForgotPasswordRequest
} from '../models/forgot-password-request.model';

import {
  ApiResponse
} from '../../../shared/models/api-response.model';

import {
  PasswordResetResponse
} from '../models/password-reset-response.model';

import {
  VerifyForgotPasswordOtpRequest
} from '../models/verify-forgot-password-otp-request.model';

import {
  VerifyForgotPasswordOtpResponse
} from '../models/verify-forgot-password-otp-response.model';

import {
  ResetPasswordRequest
} from '../models/reset-password-request.model';

import {
  Role
} from '../../../shared/enums/role.enum';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly http = inject(HttpClient);

  private readonly router = inject(Router);

  private readonly storageService =
    inject(StorageService);

  private readonly currentUserService =
    inject(CurrentUserService);


  // ============================================================
  // LOGIN
  // ============================================================

  login(
    request: LoginRequest
  ): Observable<LoginResponse> {

    return this.http.post<LoginResponse>(
      `${API.AUTH}/login`,
      request
    );
  }


  // ============================================================
  // SAVE LOGIN SESSION
  // ============================================================

  saveLoginSession(data: any): void {

    if (!data) {
      throw new Error(
        'Login response data is empty.'
      );
    }

    const accessToken =
      this.cleanToken(data.accessToken);

    const refreshToken =
      this.cleanToken(data.refreshToken);

    if (!accessToken) {
      throw new Error(
        'Login response does not contain accessToken.'
      );
    }

    /*
     * Save access token.
     */
    this.storageService.saveAccessToken(
      accessToken
    );

    /*
     * Save refresh token.
     */
    if (refreshToken) {
      this.storageService.saveRefreshToken(
        refreshToken
      );
    }

    /*
     * Save user information.
     */
    if (data.email) {
      this.storageService.saveUserEmail(
        data.email
      );
    }

    if (data.role) {
      this.storageService.saveUserRole(
        data.role
      );
    }

    console.log(
      '[AuthService] Login session saved:',
      {
        hasAccessToken: !!accessToken,
        hasRefreshToken: !!refreshToken,
        role: data.role,
        email: data.email
      }
    );
  }


  private cleanToken(
    token: string | null | undefined
  ): string {

    if (!token) {
      return '';
    }

    return String(token)
      .trim()
      .replace(/^Bearer\s+/i, '')
      .replace(/^["']|["']$/g, '');
  }


  // ============================================================
  // ROLE
  // ============================================================

  getRole(): Role | null {

    const role =
      this.storageService.getUserRole();

    if (!role) {
      return null;
    }

    return role as Role;
  }


  // ============================================================
  // LOGOUT
  // ============================================================

  logout(): void {

    console.log(
      '[AuthService] LOGOUT'
    );

    this.storageService.clear();

    this.currentUserService
      .clearCurrentUser();

    sessionStorage.removeItem(
      'resetToken'
    );

    sessionStorage.removeItem(
      'forgotMobile'
    );

    sessionStorage.removeItem(
      'verificationUser'
    );

    this.router.navigate(
      ['/login'],
      {
        replaceUrl: true
      }
    );
  }


  // ============================================================
  // MOBILE OTP
  // ============================================================

  sendMobileOtp(
    mobileNumber: string
  ) {

    return this.http.post(
      `${API.AUTH}/mobile/send`,
      {
        mobileNumber
      }
    );
  }


  verifyMobileOtp(
    mobileNumber: string,
    otp: string
  ) {

    return this.http.post(
      `${API.AUTH}/mobile/verify`,
      {
        mobileNumber,
        otp
      }
    );
  }


  // ============================================================
  // EMAIL VERIFICATION
  // ============================================================

  resendVerificationEmail(
    email: string
  ) {

    return this.http.post(
      `${API.AUTH}/resend-verification`,
      {
        email
      }
    );
  }


  getVerificationStatus(
    email: string
  ) {

    return this.http.get<any>(
      `${API.AUTH}/verification-status`,
      {
        params: {
          email
        }
      }
    );
  }


  verifyEmail(
    token: string
  ) {

    return this.http.get(
      `${API.AUTH}/verify-email`,
      {
        params: {
          token
        }
      }
    );
  }


  // ============================================================
  // FORGOT PASSWORD
  // ============================================================

  forgotPassword(
    request: ForgotPasswordRequest
  ) {

    return this.http.post<
      ApiResponse<PasswordResetResponse>
    >(
      `${API.AUTH}/forgot-password`,
      request
    );
  }


  // ============================================================
  // VERIFY FORGOT PASSWORD OTP
  // ============================================================

  verifyForgotPasswordOtp(
    request: VerifyForgotPasswordOtpRequest
  ) {

    return this.http.post<
      ApiResponse<VerifyForgotPasswordOtpResponse>
    >(
      `${API.AUTH}/verify-forgot-password-otp`,
      request
    );
  }


  // ============================================================
  // RESET PASSWORD
  // ============================================================

  resetPassword(
    request: ResetPasswordRequest
  ) {

    return this.http.post<
      ApiResponse<any>
    >(
      `${API.AUTH}/reset-password`,
      request
    );
  }


  // ============================================================
  // REFRESH TOKEN
  // ============================================================

  refreshToken(
    refreshToken: string
  ) {

    return this.http.post<
      ApiResponse<any>
    >(
      `${API.AUTH}/refresh-token`,
      {
        refreshToken
      }
    );
  }
}