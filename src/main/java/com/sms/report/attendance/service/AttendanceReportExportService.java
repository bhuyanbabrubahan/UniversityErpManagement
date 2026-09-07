package com.sms.report.attendance.service;

public interface AttendanceReportExportService {
    byte[] export(String format);
    byte[] exportSession(Long sessionId, String format);
    String contentType(String format);
}
