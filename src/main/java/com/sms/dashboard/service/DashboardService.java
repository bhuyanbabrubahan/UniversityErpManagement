package com.sms.dashboard.service;

import org.springframework.security.core.Authentication;

import com.sms.dto.DashboardResponseDto;

public interface DashboardService {

	DashboardResponseDto getDashboard(Authentication authentication);

}