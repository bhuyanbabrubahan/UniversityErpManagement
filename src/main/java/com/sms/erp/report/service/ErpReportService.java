package com.sms.erp.report.service;
import com.sms.erp.report.dto.ReportDefinitionDto;
import java.util.List;
public interface ErpReportService {
 List<ReportDefinitionDto> catalogue();
 boolean exists(String code);
 List<String[]> data(String code);
 byte[] pdf(String code) throws Exception;
 byte[] excel(String code) throws Exception;
}
