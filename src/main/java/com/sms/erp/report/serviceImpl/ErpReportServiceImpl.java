package com.sms.erp.report.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import com.lowagie.text.*;
import org.apache.poi.ss.usermodel.Row;
import com.lowagie.text.pdf.PdfWriter;
import com.sms.erp.report.dto.ReportDefinitionDto;
import com.sms.erp.report.service.ErpReportService;
import com.sms.repository.*;
import com.sms.erp.marks.repository.MarksRepository;
import com.sms.fee.repository.StudentFeePaymentRepository;
import com.sms.fee.refund.repository.StudentFeeRefundRepository;
import com.sms.student.report.scholarship.repository.StudentScholarshipReportRepository;
import com.sms.student.report.installment.repository.StudentFeeInstallmentReportRepository;
import com.sms.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ErpReportServiceImpl implements ErpReportService {
	private final StudentRepository studentRepository;
	private final FacultyRepository facultyRepository;
	private final DepartmentRepository departmentRepository;
	private final CourseRepository courseRepository;
	private final SubjectRepository subjectRepository;
	private final AttendanceRepository attendanceRepository;
	private final ExamRepository examRepository;
	private final ResultRepository resultRepository;
	private final MarksRepository marksRepository;
	private final AuditLogRepository auditLogRepository;
	private final StudentFeePaymentRepository paymentRepository;
	private final StudentFeeRefundRepository refundRepository;
	private final StudentScholarshipReportRepository scholarshipRepository;
	private final StudentFeeInstallmentReportRepository installmentRepository;
	private static final List<ReportDefinitionDto> REPORTS = List.of(r("student-mis", "Student MIS", "Students"),
			r("faculty-mis", "Faculty MIS", "Faculty"), r("attendance-daily", "Daily Attendance", "Attendance"),
			r("attendance-date-range", "Date Range Attendance", "Attendance"),
			r("attendance-semester", "Semester Attendance", "Attendance"),
			r("attendance-year", "Year-wise Attendance", "Attendance"),
			r("attendance-subject", "Subject Attendance", "Attendance"),
			r("attendance-department", "Department Attendance", "Attendance"),
			r("exam-schedule", "Exam Schedule", "Examination"), r("marks-sheet", "Marks Sheet", "Examination"),
			r("result-cgpa", "Result / CGPA", "Examination"), r("fee-collection", "Fee Collection", "Finance"),
			r("fee-due", "Pending Dues", "Finance"), r("payment-history", "Payment History", "Finance"),
			r("refund-register", "Refund Register", "Finance"),
			r("scholarship-register", "Scholarship Register", "Finance"),
			r("installment-register", "Installment Register", "Finance"),
			r("admission-register", "Admission Register", "Admission"),
			r("audit-activity", "Audit Activity", "Security"));

	private static ReportDefinitionDto r(String c, String n, String cat) {
		return ReportDefinitionDto.builder().code(c).name(n).category(cat).format("PDF/XLSX").pdfAvailable(true)
				.build();
	}

	public List<ReportDefinitionDto> catalogue() {
		return REPORTS;
	}

	public boolean exists(String code) {
		return REPORTS.stream().anyMatch(r -> r.getCode().equals(code));
	}

	public List<String[]> data(String code) {
		List<String[]> rows = new ArrayList<>();
		rows.add(new String[] { "Metric", "Value" });
		switch (code) {
		case "student-mis" -> {
			long total = studentRepository.count();
			rows.add(new String[] { "Students", String.valueOf(total) });
			rows.add(new String[] { "Active Students", String.valueOf(total) });
		}
		case "faculty-mis" -> rows.add(new String[] { "Faculty", String.valueOf(facultyRepository.count()) });
		case "attendance-daily", "attendance-date-range", "attendance-semester", "attendance-year",
				"attendance-subject", "attendance-department" ->
			rows.add(new String[] { "Attendance Records", String.valueOf(attendanceRepository.count()) });
		case "exam-schedule" -> rows.add(new String[] { "Examinations", String.valueOf(examRepository.count()) });
		case "marks-sheet" -> rows.add(new String[] { "Marks Records", String.valueOf(marksRepository.count()) });
		case "result-cgpa" -> rows.add(new String[] { "Results", String.valueOf(resultRepository.count()) });
		case "fee-collection", "fee-due", "payment-history" ->
			rows.add(new String[] { "Payment Records", String.valueOf(paymentRepository.count()) });
		case "refund-register" -> rows.add(new String[] { "Refund Records", String.valueOf(refundRepository.count()) });
		case "scholarship-register" ->
			rows.add(new String[] { "Scholarship Records", String.valueOf(scholarshipRepository.count()) });
		case "installment-register" ->
			rows.add(new String[] { "Installment Records", String.valueOf(installmentRepository.count()) });
		case "admission-register" -> rows.add(new String[] { "Departments/Courses/Subjects",
				departmentRepository.count() + " / " + courseRepository.count() + " / " + subjectRepository.count() });
		case "audit-activity" -> rows.add(new String[] { "Audit Records", String.valueOf(auditLogRepository.count()) });
		default -> rows.add(new String[] { "Records", String.valueOf(0L) });
		}
		return rows;
	}

	public byte[] pdf(String code) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document doc = new Document(PageSize.A4);
		PdfWriter.getInstance(doc, out);
		doc.open();
		doc.add(new Paragraph("University ERP Management"));
		doc.add(new Paragraph("Report: " + code));
		doc.add(new Paragraph("Generated: " + LocalDateTime.now()));
		for (String[] row : data(code))
			doc.add(new Paragraph(row[0] + " : " + row[1]));
		doc.add(new Paragraph("Filters: default authenticated role scope"));
		doc.close();
		return out.toByteArray();
	}

	public byte[] excel(String code) throws Exception {
		try (Workbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sh = wb.createSheet("Report");
			Row meta = sh.createRow(0);
			meta.createCell(0).setCellValue("University ERP Management");
			meta.createCell(1).setCellValue(code);
			Row h = sh.createRow(2);
			h.createCell(0).setCellValue("Metric");
			h.createCell(1).setCellValue("Value");
			int i = 3;
			for (String[] row : data(code)) {
				if (i == 3 && row[0].equals("Metric"))
					continue;
				Row r = sh.createRow(i++);
				r.createCell(0).setCellValue(row[0]);
				r.createCell(1).setCellValue(row[1]);
			}
			sh.autoSizeColumn(0);
			sh.autoSizeColumn(1);
			wb.write(out);
			return out.toByteArray();
		}
	}
}
