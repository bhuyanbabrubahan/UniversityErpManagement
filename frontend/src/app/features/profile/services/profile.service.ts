import { Injectable, inject } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

import { API } from "../../../core/constants/api.constants";

import { ApiResponse } from "../../../shared/models/api-response.model";
import { ProfileResponse } from "../models/profile-response.model";
import { UpdateProfileRequest } from "../models/update-profile-request.model";
import { ChangePasswordResponse } from "../models/change-password-response.model";
import { ChangePasswordRequest } from "../models/change-password-request.model";

@Injectable({
  providedIn: "root",
})
export class ProfileService {
  private readonly http = inject(HttpClient);

  getProfile(): Observable<ApiResponse<ProfileResponse>> {
    return this.http.get<ApiResponse<ProfileResponse>>(`${API.PROFILE}/me`);
  }

  updateProfile(
    request: UpdateProfileRequest,
  ): Observable<ApiResponse<ProfileResponse>> {
    return this.http.put<ApiResponse<ProfileResponse>>(API.PROFILE, request);
  }

  uploadProfileImage(file: File): Observable<ApiResponse<ProfileResponse>> {
    const formData = new FormData();

    formData.append("file", file);

    return this.http.post<ApiResponse<ProfileResponse>>(
      `${API.PROFILE}/photo`,
      formData,
    );
  }

  removeProfileImage(): Observable<ApiResponse<ProfileResponse>> {
    return this.http.delete<ApiResponse<ProfileResponse>>(
      `${API.PROFILE}/photo`,
    );
  }

  changePassword(
    request: ChangePasswordRequest,
  ): Observable<ApiResponse<ChangePasswordResponse>> {
    return this.http.put<ApiResponse<ChangePasswordResponse>>(
      `${API.PROFILE}/change-password`,
      request,
    );
  }

  resendVerificationEmail(email: string): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(
      API.AUTH_API.RESEND_VERIFICATION,
      {
        email,
      },
    );
  }

  sendMobileOtp(mobileNumber: string): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(API.AUTH_API.SEND_MOBILE_OTP, {
      mobileNumber,
    });
  }

  verifyMobileOtp(
    mobileNumber: string,
    otp: string,
  ): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(API.AUTH_API.VERIFY_MOBILE_OTP, {
      mobileNumber,
      otp,
    });
  }
}
