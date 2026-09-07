package com.sms.fee.service;

import com.sms.fee.entity.StudentFeeReceipt;

public interface ReceiptPdfService {


    byte[] generateReceiptPdf(
            StudentFeeReceipt receipt
    );

}