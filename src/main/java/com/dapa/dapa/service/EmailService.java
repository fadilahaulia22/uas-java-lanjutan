package com.dapa.dapa.service;

import java.util.List;

import com.dapa.dapa.entity.Voucher;

public interface EmailService {
    void sendEmail(String to);

    void sendSimpleMessage(String to, String subject,String text);

    void sendHtmlMessage(String to, String subject, String htmlContent);

    void sendMailTest();

    void sendOTPEmail(String userName, String OTP);

    void sendHtmlMessageWithAttachment(String to, String subject, String text, String attachmentName, byte[] attachment);

    void sendVoucherEmail(String recipient, String subject, List<Voucher> vouchers);
}
