package com.sms.fee.report.export.service;

public interface FeeExportService {
    byte[] export(String report, String format);
    String contentType(String format);
    String fileName(String report, String format);
}
