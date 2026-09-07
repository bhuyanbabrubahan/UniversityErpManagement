package com.sms.fee.report.export.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sms.enums.RecordStatus;
import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.entity.StudentFeeReceipt;
import com.sms.fee.installment.entity.StudentFeeInstallment;
import com.sms.fee.installment.repository.StudentFeeInstallmentRepository;
import com.sms.fee.refund.entity.StudentFeeRefund;
import com.sms.fee.refund.repository.StudentFeeRefundRepository;
import com.sms.fee.report.export.service.FeeExportService;
import com.sms.fee.repository.StudentFeePaymentRepository;
import com.sms.fee.repository.StudentFeeReceiptRepository;
import com.sms.scholarship.entity.StudentScholarship;
import com.sms.scholarship.repository.StudentScholarshipRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeeExportServiceImpl implements FeeExportService {
    private final StudentFeePaymentRepository paymentRepository;
    private final StudentFeeReceiptRepository receiptRepository;
    private final StudentFeeRefundRepository refundRepository;
    private final StudentFeeInstallmentRepository installmentRepository;
    private final StudentScholarshipRepository scholarshipRepository;

    @Override
    public byte[] export(String report, String format) {
        String normalizedReport = report == null ? "PAYMENTS" : report.trim().toUpperCase();
        String normalizedFormat = format == null ? "xlsx" : format.trim().toLowerCase();
        List<String[]> rows = rows(normalizedReport);
        return "pdf".equals(normalizedFormat) ? pdf(normalizedReport, rows) : xlsx(normalizedReport, rows);
    }

    @Override
    public String contentType(String format) {
        return "pdf".equalsIgnoreCase(format) ? "application/pdf" : "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }

    @Override
    public String fileName(String report, String format) {
        String ext = "pdf".equalsIgnoreCase(format) ? "pdf" : "xlsx";
        return "fee-" + report.toLowerCase() + "-report." + ext;
    }

    private List<String[]> rows(String report) {
        List<String[]> rows = new ArrayList<>();
        switch (report) {
            case "PAYMENTS", "PENDING_DUE" -> {
                rows.add(new String[]{"ID","Payment Date","Academic Year","Total Fee","Paid","Due","Status","Mode","Reference"});
                for (StudentFeePayment p : paymentRepository.findByStatusNot(RecordStatus.DELETED, PageRequest.of(0, 10000)).getContent()) {
                    if ("PENDING_DUE".equals(report) && nz(p.getDueAmount()).signum() <= 0) continue;
                    rows.add(new String[]{str(p.getId()),str(p.getPaymentDate()),p.getAcademicYear(),money(p.getTotalFee()),money(p.getPaidAmount()),money(p.getDueAmount()),str(p.getPaymentStatus()),str(p.getPaymentMode()),str(p.getTransactionReference())});
                }
            }
            case "RECEIPTS" -> {
                rows.add(new String[]{"ID","Receipt No","Date","Student","Roll No","Amount","Status"});
                for (StudentFeeReceipt r : receiptRepository.findByStatusNot(RecordStatus.DELETED, PageRequest.of(0, 10000)).getContent())
                    rows.add(new String[]{str(r.getId()),r.getReceiptNumber(),str(r.getReceiptDate()),r.getStudentName(),str(r.getRollNumber()),money(r.getPaidAmount()),str(r.getPaymentStatus())});
            }
            case "REFUNDS" -> {
                rows.add(new String[]{"ID","Refund No","Date","Student","Amount","Status","Mode","Reference"});
                for (StudentFeeRefund r : refundRepository.findByStatusNot(RecordStatus.DELETED, PageRequest.of(0, 10000)).getContent())
                    rows.add(new String[]{str(r.getId()),r.getRefundNumber(),str(r.getRefundDate()),r.getStudentName(),money(r.getRefundAmount()),str(r.getRefundStatus()),str(r.getRefundMode()),str(r.getRefundTransactionReference())});
            }
            case "INSTALLMENTS" -> {
                rows.add(new String[]{"ID","Installment No","Student","Roll No","Due Date","Amount","Paid","Remaining","Status"});
                for (StudentFeeInstallment i : installmentRepository.findByStatusNot(RecordStatus.DELETED, PageRequest.of(0, 10000)).getContent())
                    rows.add(new String[]{str(i.getId()),i.getInstallmentNumber(),i.getStudentName(),i.getRollNumber(),str(i.getDueDate()),money(i.getInstallmentAmount()),money(i.getPaidAmount()),money(i.getRemainingAmount()),str(i.getInstallmentStatus())});
            }
            case "SCHOLARSHIPS" -> {
                rows.add(new String[]{"ID","Scholarship","Academic Year","Requested","Approved","Status","Approval Date"});
                for (StudentScholarship s : scholarshipRepository.findByStatusNot(RecordStatus.DELETED, PageRequest.of(0, 10000)).getContent())
                    rows.add(new String[]{str(s.getId()),s.getScholarshipName(),s.getAcademicYear(),money(s.getRequestedAmount()),money(s.getApprovedAmount()),str(s.getScholarshipStatus()),str(s.getApprovalDate())});
            }
            default -> throw new IllegalArgumentException("Unsupported fee report: " + report);
        }
        return rows;
    }

    private byte[] xlsx(String report, List<String[]> rows) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(report.substring(0, Math.min(report.length(), 31)));
            for (int r = 0; r < rows.size(); r++) {
                Row row = sheet.createRow(r);
                String[] values = rows.get(r);
                for (int c = 0; c < values.length; c++) row.createCell(c).setCellValue(values[c]);
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to create Excel report", ex);
        }
    }

    private byte[] pdf(String report, List<String[]> rows) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();
            Font titleFont = new Font(Font.HELVETICA, 15, Font.BOLD);
            Paragraph title = new Paragraph("University ERP - " + report + " Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("Generated: " + LocalDate.now()));
            if (!rows.isEmpty()) {
                PdfPTable table = new PdfPTable(rows.get(0).length);
                table.setWidthPercentage(100);
                for (String[] values : rows) for (String value : values) {
                    PdfPCell cell = new PdfPCell(new Phrase(value == null ? "" : value));
                    table.addCell(cell);
                }
                document.add(table);
            }
            document.close();
            return out.toByteArray();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to create PDF report", ex);
        }
    }

    private static String str(Object value) { return value == null ? "" : String.valueOf(value); }
    private static BigDecimal nz(BigDecimal value) { return value == null ? BigDecimal.ZERO : value; }
    private static String money(BigDecimal value) { return nz(value).toPlainString(); }
}
