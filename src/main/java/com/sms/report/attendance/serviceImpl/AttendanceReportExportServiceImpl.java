package com.sms.report.attendance.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
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
import com.sms.entity.Attendance;
import com.sms.enums.RecordStatus;
import com.sms.report.attendance.service.AttendanceReportExportService;
import com.sms.repository.AttendanceRepository;
import com.sms.repository.AttendanceSessionRepository;
import com.sms.repository.FacultyRepository;
import com.sms.specification.AttendanceSpecification;
import org.springframework.data.jpa.domain.Specification;
import com.sms.entity.AttendanceSession;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceReportExportServiceImpl implements AttendanceReportExportService {
    private final AttendanceRepository repository;
    private final AttendanceSessionRepository sessionRepository;
    private final FacultyRepository facultyRepository;

    @Override
    public byte[] export(String format) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"ID","Student","Roll No","Attendance Date","Status","Marked At","Remarks"});

        Specification<Attendance> spec =
                AttendanceSpecification.statusNot(RecordStatus.DELETED);

        if ("FACULTY".equals(SecurityUtils.getCurrentRole())) {
            Long userId = SecurityUtils.getCurrentUserId();
            Long facultyId = userId == null ? null :
                    facultyRepository.findByUserIdAndStatus(
                            userId, com.sms.enums.RecordStatus.ACTIVE)
                            .map(f -> f.getId())
                            .orElseThrow(() ->
                                    new BusinessException("Faculty account not found."));
            spec = spec.and(AttendanceSpecification.hasFaculty(facultyId));
        }

        for (Attendance a : repository.findAll(
                spec, PageRequest.of(0, 10000)).getContent()) {
            rows.add(new String[]{str(a.getId()),a.getStudent().getFullName(),a.getStudent().getRollNumber(),str(a.getAttendanceSession().getAttendanceDate()),str(a.getAttendanceStatus()),str(a.getMarkedAt()),str(a.getRemarks())});
        }
        return "pdf".equalsIgnoreCase(format) ? pdf(rows) : xlsx(rows);
    }

    @Override
    public byte[] exportSession(Long sessionId, String format) {
        AttendanceSession session = sessionRepository.findByIdAndStatusNot(sessionId, RecordStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance session not found with id : " + sessionId));
        String role = SecurityUtils.getCurrentRole();
        if (!"ADMIN".equals(role) && !"SUPER_ADMIN".equals(role) && !"FACULTY".equals(role)) {
            throw new BusinessException("You are not authorized to export attendance session details.");
        }
        if ("FACULTY".equals(role)) {
            Long facultyId = session.getFacultySubjectMapping() != null && session.getFacultySubjectMapping().getFaculty() != null
                    ? session.getFacultySubjectMapping().getFaculty().getId() : null;
            Long currentUserId = SecurityUtils.getCurrentUserId();
            Long currentFacultyId = currentUserId == null ? null : facultyRepository.findByUserIdAndStatus(currentUserId, RecordStatus.ACTIVE).map(f -> f.getId()).orElse(null);
            if (facultyId == null || currentFacultyId == null || !facultyId.equals(currentFacultyId)) {
                throw new ResourceNotFoundException("Attendance session is not assigned to the current faculty.");
            }
        }
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"Session ID",str(session.getId()),"Date",str(session.getAttendanceDate()),"Faculty",str(session.getFacultySubjectMapping()!=null && session.getFacultySubjectMapping().getFaculty()!=null ? session.getFacultySubjectMapping().getFaculty().getFirstName()+" "+session.getFacultySubjectMapping().getFaculty().getLastName() : ""),"Subject",str(session.getFacultySubjectMapping()!=null && session.getFacultySubjectMapping().getSubject()!=null ? session.getFacultySubjectMapping().getSubject().getSubjectName() : "")});
        rows.add(new String[]{"Student ID","Student","Roll No","Status","Marked At","Remarks"});
        for (Attendance a : repository.findByAttendanceSessionId(sessionId, RecordStatus.DELETED)) {
            rows.add(new String[]{str(a.getStudent().getId()),a.getStudent().getFullName(),a.getStudent().getRollNumber(),str(a.getAttendanceStatus()),str(a.getMarkedAt()),str(a.getRemarks())});
        }
        return "pdf".equalsIgnoreCase(format) ? pdfSession(rows, session) : xlsx(rows);
    }

    private byte[] pdfSession(List<String[]> rows, AttendanceSession session) {
        try(ByteArrayOutputStream out=new ByteArrayOutputStream()){
            Document document=new Document(); PdfWriter.getInstance(document,out); document.open();
            Font font=new Font(Font.HELVETICA,15,Font.BOLD); Paragraph title=new Paragraph("University ERP - Attendance Session #"+session.getId(),font); title.setAlignment(Element.ALIGN_CENTER); document.add(title);
            document.add(new Paragraph("Date: "+str(session.getAttendanceDate())));
            document.add(new Paragraph("Session Status: "+str(session.getSessionStatus())));
            if(session.getFacultySubjectMapping()!=null && session.getFacultySubjectMapping().getFaculty()!=null) document.add(new Paragraph("Faculty: "+session.getFacultySubjectMapping().getFaculty().getFirstName()+" "+session.getFacultySubjectMapping().getFaculty().getLastName()));
            if(session.getFacultySubjectMapping()!=null && session.getFacultySubjectMapping().getSubject()!=null) document.add(new Paragraph("Subject: "+str(session.getFacultySubjectMapping().getSubject().getSubjectName())));
            if(rows.size()>1){ PdfPTable table=new PdfPTable(6); table.setWidthPercentage(100); for(int r=1;r<rows.size();r++){ for(String value:rows.get(r)) table.addCell(new PdfPCell(new Phrase(value==null?"":value))); } document.add(table); }
            document.close(); return out.toByteArray();
        } catch(Exception ex){ throw new IllegalStateException("Unable to create attendance session PDF report",ex); }
    }

    @Override
    public String contentType(String format) { return "pdf".equalsIgnoreCase(format) ? "application/pdf" : "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"; }

    private byte[] xlsx(List<String[]> rows) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            var sheet = workbook.createSheet("Attendance");
            for (int r=0;r<rows.size();r++){ Row row=sheet.createRow(r); String[] values=rows.get(r); for(int c=0;c<values.length;c++) row.createCell(c).setCellValue(values[c]); }
            workbook.write(out); return out.toByteArray();
        } catch(Exception ex){ throw new IllegalStateException("Unable to create attendance Excel report",ex); }
    }

    private byte[] pdf(List<String[]> rows) {
        try(ByteArrayOutputStream out=new ByteArrayOutputStream()){
            Document document=new Document(); PdfWriter.getInstance(document,out); document.open();
            Font font=new Font(Font.HELVETICA,15,Font.BOLD); Paragraph title=new Paragraph("University ERP - Attendance Report",font); title.setAlignment(Element.ALIGN_CENTER); document.add(title);
            PdfPTable table=new PdfPTable(rows.get(0).length); table.setWidthPercentage(100);
            for(String[] values:rows) for(String value:values) table.addCell(new PdfPCell(new Phrase(value==null?"":value)));
            document.add(table); document.close(); return out.toByteArray();
        } catch(Exception ex){ throw new IllegalStateException("Unable to create attendance PDF report",ex); }
    }
    private static String str(Object value){return value==null?"":String.valueOf(value);}
}
