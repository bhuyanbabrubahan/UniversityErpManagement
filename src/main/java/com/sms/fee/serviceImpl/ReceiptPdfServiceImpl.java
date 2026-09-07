package com.sms.fee.serviceImpl;


import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.sms.enums.RecordStatus;
import com.sms.exception.BusinessException;
import com.sms.fee.entity.StudentFeeReceipt;
import com.sms.fee.repository.StudentFeeReceiptRepository;
import com.sms.fee.service.ReceiptPdfService;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class ReceiptPdfServiceImpl
        implements ReceiptPdfService {

	private final Font sectionFont =
	        FontFactory.getFont(
	                FontFactory.HELVETICA_BOLD,
	                13
	        );

	private final Font labelFont =
	        FontFactory.getFont(
	                FontFactory.HELVETICA_BOLD,
	                10
	        );
	
	private final Font valueFont =
	        FontFactory.getFont(
	                FontFactory.HELVETICA,
	                10
	        );
	

	private final StudentFeeReceiptRepository repository;

    @Override
    public byte[] generateReceiptPdf(StudentFeeReceipt receipt){


        try {


			ByteArrayOutputStream output = new ByteArrayOutputStream();

			Document document =
			        new Document(
			                com.lowagie.text.PageSize.A4,
			                25,
			                25,
			                20,
			                20
			        );

			PdfWriter.getInstance(document, output);


            document.open();

            

         // =====================================================
         // HEADER
         // =====================================================

         Font universityFont =
                 FontFactory.getFont(
                         FontFactory.HELVETICA_BOLD,
                         18,
                         new Color(0, 51, 153)
                 );

         Font addressFont =
                 FontFactory.getFont(
                         FontFactory.HELVETICA,
                         10,
                         Color.DARK_GRAY
                 );

         Font receiptTitleFont =
                 FontFactory.getFont(
                         FontFactory.HELVETICA_BOLD,
                         14,
                         Color.BLACK
                 );

         Paragraph university =
                 new Paragraph(
                         "UNIVERSITY ADMISSION MANAGEMENT SYSTEM",
                         universityFont
                 );

         university.setAlignment(Element.ALIGN_CENTER);

         document.add(university);

         Paragraph address =
                 new Paragraph(
                         "Bhubaneswar, Odisha - India",
                         addressFont
                 );

         address.setAlignment(Element.ALIGN_CENTER);

         document.add(address);

         Paragraph title =
                 new Paragraph(
                         "STUDENT FEE RECEIPT",
                         receiptTitleFont
         );

         title.setAlignment(Element.ALIGN_CENTER);

         title.setSpacingBefore(6f);

         title.setSpacingAfter(8f);

         document.add(title);

         LineSeparator line = new LineSeparator();

         line.setLineWidth(1f);

         document.add(line); 




	      // =====================================================
	      // Receipt Information
	      // =====================================================
	
	      PdfPTable receiptTable = new PdfPTable(4);
	
	      receiptTable.setWidthPercentage(100);
	
	      receiptTable.setWidths(new float[]{20, 30, 20, 30});
	
	      receiptTable.setSpacingBefore(8f);
	
	      receiptTable.setSpacingAfter(10f);
	
	      // Receipt Number Label
	      receiptTable.addCell(createLabelCell("Receipt No"));
	
	      // Receipt Number Value
	      receiptTable.addCell(createValueCell(receipt.getReceiptNumber()));
	
	      // Receipt Date Label
	      receiptTable.addCell(createLabelCell("Receipt Date"));
	
	      // Receipt Date Value
	      receiptTable.addCell(
	              createValueCell(
	                      formatDate(receipt.getReceiptDate())
	              )
	      );
	
	      document.add(receiptTable); 




            
            
            
         // =====================================================
         // Student Information
         // =====================================================

	      Paragraph studentTitle =
      	        new Paragraph(
      	                "STUDENT INFORMATION",
      	                sectionFont
      	        );

      	studentTitle.setSpacingBefore(5f);
      	studentTitle.setSpacingAfter(3f);

      	document.add(studentTitle);

         PdfPTable studentTable =
                 new PdfPTable(2);

         studentTable.setWidthPercentage(100);

         studentTable.setSpacingBefore(5f);
         studentTable.setSpacingAfter(8f);
         
         studentTable.setKeepTogether(true);
         studentTable.setSplitRows(false);
         studentTable.setSplitLate(false);

         studentTable.setWidths(
        		 new float[]{42,58}
         );
         
         
         studentTable.addCell(
        	        createSectionHeader(
        	                "Student Information"
        	        )
        	);
         
         studentTable.addCell(
        	        createLabelCell(
        	                "Student Name"
        	        )
        	);

        	studentTable.addCell(
        	        createValueCell(
        	                receipt.getStudentName()
        	        )
        	);



        	studentTable.addCell(
        	        createLabelCell(
        	                "Roll Number"
        	        )
        	);

        	studentTable.addCell(
        	        createValueCell(
        	                receipt.getRollNumber()
        	        )
        	);



        	studentTable.addCell(
        	        createLabelCell(
        	                "Admission Number"
        	        )
        	);

        	studentTable.addCell(
        	        createValueCell(
        	                receipt.getAdmissionNumber()
        	        )
        	);



        	studentTable.addCell(
        	        createLabelCell(
        	                "Academic Year"
        	        )
        	);

        	studentTable.addCell(
        	        createValueCell(
        	                receipt.getAcademicYear()
        	        )
        	);



        	studentTable.addCell(
        	        createLabelCell(
        	                "Course"
        	        )
        	);

        	studentTable.addCell(
        	        createValueCell(
        	                receipt.getCourseName()
        	        )
        	);



        	studentTable.addCell(
        	        createLabelCell(
        	                "Department"
        	        )
        	);

        	studentTable.addCell(
        	        createValueCell(
        	                receipt.getDepartmentName()
        	        )
        	);



        	studentTable.addCell(
        	        createLabelCell(
        	                "Semester"
        	        )
        	);

        	studentTable.addCell(
        	        createValueCell(
        	                receipt.getSemesterName()
        	        )
        	);
        	
        	
        	document.add(studentTable);
        	
        	
        	
        	
        	
        	
        	// =====================================================
        	// Fee Information
        	// =====================================================

        	Paragraph feeTitle =
        	        new Paragraph(
        	                "FEE INFORMATION",
        	                sectionFont
        	        );

        	feeTitle.setSpacingBefore(5f);
        	feeTitle.setSpacingAfter(3f);

        	document.add(feeTitle);

        	PdfPTable feeTable =
        	        new PdfPTable(2);

        	feeTable.setWidthPercentage(100);

        	feeTable.setWidths(
        			 new float[]{42,58}
        	);

        	feeTable.setSpacingBefore(5f);
        	feeTable.setSpacingAfter(8f);
        	feeTable.setKeepTogether(true);
        	feeTable.setSplitRows(false);
        	feeTable.setSplitLate(false);
        	
        	// Prevent table breaking between pages
        	feeTable.setSplitLate(true);

        	feeTable.setSplitRows(false);
        	
        	
        	feeTable.addCell(
        	        createSectionHeader(
        	                "Fee Information"
        	        )
        	);
        	
        	feeTable.addCell(
        	        createLabelCell(
        	                "Total Fee"
        	        )
        	);

        	feeTable.addCell(
        	        createValueCell(
        	        		formatAmount(
        	                        receipt.getTotalFee()
        	                )
        	        )
        	);



        	feeTable.addCell(
        	        createLabelCell(
        	                "Scholarship Amount"
        	        )
        	);

        	feeTable.addCell(
        	        createValueCell(
        	                formatAmount(
        	                        receipt.getScholarshipAmount()
        	                )
        	        )
        	);



        	feeTable.addCell(
        	        createLabelCell(
        	                "Final Payable"
        	        )
        	);

        	feeTable.addCell(
        	        createValueCell(
        	        		formatAmount(
        	                        receipt.getFinalPayableAmount()
        	                )
        	        )
        	);



        	feeTable.addCell(
        	        createLabelCell(
        	                "Paid Amount"
        	        )
        	);

        	feeTable.addCell(
        	        createValueCell(
        	        		formatAmount(
        	                        receipt.getPaidAmount()
        	                )
        	        )
        	);



        	feeTable.addCell(
        	        createLabelCell(
        	                "Due Amount"
        	        )
        	);

        	feeTable.addCell(
        	        createValueCell(
        	        		formatAmount(
        	                        receipt.getDueAmount()
        	                )
        	        )
        	);
        	
        	
        	document.add(feeTable);
        	
        	
        	
        	// =====================================================
        	// Payment Information
        	// =====================================================

        	Paragraph paymentTitle =
        	        new Paragraph(
        	                "PAYMENT INFORMATION",
        	                sectionFont
        	        );

        	paymentTitle.setSpacingBefore(5f);
        	paymentTitle.setSpacingAfter(3f);

        	document.add(paymentTitle);

        	PdfPTable paymentTable =
        	        new PdfPTable(2);

        	

        	paymentTable.setSpacingBefore(5f);

        	paymentTable.setSpacingAfter(8f);

        	paymentTable.setKeepTogether(true);
        	paymentTable.setSplitRows(false);
        	paymentTable.setSplitLate(false);

        	paymentTable.setWidthPercentage(100);

        	paymentTable.setWidths(
        			new float[]{42,58}
        	);
        	
        	
        	paymentTable.addCell(
        	        createSectionHeader(
        	                "Payment Information"
        	        )
        	);

        	paymentTable.setSpacingBefore(5f);

        	paymentTable.setSpacingAfter(4f);

        	paymentTable.setKeepTogether(true);
        	paymentTable.setSplitRows(false);
        	paymentTable.setSplitLate(false);
        	
        	paymentTable.addCell(createLabelCell("Transaction Reference"));
        	paymentTable.addCell(createValueCell(receipt.getTransactionReference()));

        	paymentTable.addCell(createLabelCell("Payment Status"));
        	paymentTable.addCell(createValueCell(
        	        receipt.getPaymentStatus() == null
        	                ? "-"
        	                : receipt.getPaymentStatus().toString()
        	));

        	paymentTable.addCell(createLabelCell("Payment Mode"));
        	paymentTable.addCell(createValueCell(
        	        receipt.getPaymentMode() == null
        	                ? "-"
        	                : receipt.getPaymentMode().toString()
        	));

        	paymentTable.addCell(createLabelCell("Payment Date"));
        	paymentTable.addCell(createValueCell(
        	        formatDate(receipt.getPaymentDate())
        	));

        	paymentTable.addCell(createLabelCell("Transaction ID"));
        	paymentTable.addCell(createValueCell(
        	        receipt.getTransactionReference()
        	));



        	
        	
        	
        	document.add(paymentTable);
        	
        	
        	// =====================================================
        	// Footer
        	// =====================================================

        	LineSeparator separator = new LineSeparator();

        	document.add(separator);
        	
        	separator.setOffset(-2);
        	
        	
        	Paragraph thankYou = new Paragraph(

        	        "Thank you for your payment.",

        	        FontFactory.getFont(
        	                FontFactory.HELVETICA_BOLD,
        	                12
        	        )
        	);

        	thankYou.setAlignment(Element.ALIGN_CENTER);

        	thankYou.setSpacingBefore(40f);
        	thankYou.setSpacingAfter(25f);

        	document.add(thankYou);
        	
        	
        	
        	
        	Paragraph note = new Paragraph(

        	        "This is a computer generated fee receipt and does not require a signature.",

        	        FontFactory.getFont(
        	                FontFactory.HELVETICA,
        	                9,
        	                Color.DARK_GRAY
        	        )
        	);

        	note.setAlignment(Element.ALIGN_CENTER);

        	document.add(note);
        	
        	
        	
        	Paragraph generatedOn = new Paragraph(

        	        "Generated On : "
        	        + LocalDateTime.now().format(
        	                DateTimeFormatter.ofPattern(
        	                        "dd-MM-yyyy HH:mm:ss"
        	                )
        	        ),

        	        FontFactory.getFont(
        	                FontFactory.HELVETICA,
        	                9
        	        )
        	);


        	generatedOn.setSpacingBefore(2f);

        	generatedOn.setAlignment(Element.ALIGN_RIGHT);

        	document.add(generatedOn);
        	
        	
        	
        	

        	PdfPTable signTable = new PdfPTable(1);
        	signTable.setWidthPercentage(100);

        	PdfPCell signCell = new PdfPCell(
        	        new Phrase(
        	                "Authorized Signature",
        	                FontFactory.getFont(
        	                        FontFactory.HELVETICA_BOLD,
        	                        10
        	                )
        	        )
        	);

        	signCell.setBorder(PdfPCell.NO_BORDER);
        	signCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        	signCell.setPaddingTop(2f);
        	signCell.setPaddingBottom(0f);

        	signTable.addCell(signCell);

        	document.add(signTable);

        	document.close();
        	

        	return output.toByteArray();

            

        }
        
        
        
        catch(Exception e){

            throw new BusinessException(
                    "Unable to generate receipt PDF : " + e.getMessage()
            );
        }
    
    
    
    
	 
	    
    }
    
    
     // =====================================================
 	 // Download Receipt PDF
 	 // =====================================================
 	
 	 
 	 @Transactional(readOnly = true)
 	 public byte[] downloadReceipt(Long receiptId) {
 	
 	     // =====================================================
 	     // Get Receipt
 	     // =====================================================
 	
 	     StudentFeeReceipt receipt =
 	             repository
 	                     .findByIdAndStatusNot(
 	                             receiptId,
 	                             RecordStatus.DELETED
 	                     )
 	                     .orElseThrow(
 	                             () -> new BusinessException(
 	                                     "Receipt not found with ID : "
 	                                             + receiptId
 	                             )
 	                     );
 	
 	     // =====================================================
 	     // Generate PDF
 	     // =====================================================
 	
 	     return generateReceiptPdf(receipt);
 	
 	 }
 	 
 	 
 	 
 	 
 	 
    
    
	 	private PdfPCell createLabelCell(String text){
	
	 	    PdfPCell cell =
	 	            new PdfPCell(
	 	                    new Phrase(
	 	                            text,
	 	                            labelFont
	 	                    )
	 	            );
	
	 	    cell.setPadding(5);
	 	    cell.setBorderWidth(0.75f);
	 	    cell.setBackgroundColor(new Color(242,242,242));
	
	 	    return cell;
	 	}
	
	
	
	 	private PdfPCell createValueCell(String text){

	 	    PdfPCell cell =
	 	            new PdfPCell(
	 	                    new Phrase(
	 	                            text,
	 	                            valueFont
	 	                    )
	 	            );

	 	    cell.setPadding(5);
	 	    cell.setBorderWidth(0.75f);

	 	    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	 	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

	 	    return cell;
	 	}
	    
	    
	    
	    
	 // =====================================================
	 // Section Header Cell
	 // =====================================================

	 private PdfPCell createSectionHeader(
	         String title
	 ) {

	     PdfPCell cell =
	             new PdfPCell(
	                     new Phrase(
	                             title,
	                             FontFactory.getFont(
	                                     FontFactory.HELVETICA_BOLD,
	                                     12,
	                                     Color.WHITE
	                             )
	                     )
	             );

	     cell.setColspan(2);

	     cell.setHorizontalAlignment(
	             Element.ALIGN_CENTER
	     );

	     cell.setBackgroundColor(
	    		 new Color(25,70,160)
	     );

	     cell.setPadding(6f);

	     cell.setBorderWidth(0);

	     return cell;
	 }
	 
	 
	 
	// =====================================================
	// Currency Formatter
	// =====================================================

	 private String formatAmount(BigDecimal amount){

		    if(amount == null){
		        return "Rs. 0.00";
		    }

		    DecimalFormat formatter =
		            new DecimalFormat("#,##0.00");

		    return "Rs. " + formatter.format(amount);
		}
	
	
	// =====================================================
	// Date Formatter
	// =====================================================

	private String formatDate(LocalDate date) {

	    if(date == null){

	        return "-";

	    }

	    return date.format(

	            DateTimeFormatter.ofPattern(
	                    "dd-MMM-yyyy"
	            )

	    );

	}
	 
	 
		
	
	
	
	    
}