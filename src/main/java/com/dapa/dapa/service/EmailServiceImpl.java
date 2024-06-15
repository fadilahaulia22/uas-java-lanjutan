package com.dapa.dapa.service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import com.dapa.dapa.entity.Voucher;
import com.dapa.dapa.repository.VoucerRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    JavaMailSender emailSender;

    @Autowired
    ThymeleafService thymeleafService;

    @Autowired
    VoucerRepository voucerRepository;

    @Override
    public void sendSimpleMessage(String to,String subject,String text){
        try {
            SimpleMailMessage messsage = new SimpleMailMessage();
            messsage.setFrom("noreply@dapa.com");
            messsage.setTo(to);
            messsage.setSubject(subject);
            messsage.setText(text);
            emailSender.send(messsage);
        } catch (MailException  me) {
            me.printStackTrace();
        }
    }

    @Override
    public void sendMailTest() {

        try {
            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
                
            helper.setFrom("muthmauleaaa@gmail.com", "Admin DAPA STORE");
            helper.setTo("fadilahaulia@gmail.com");

            Map<String, Object> variables = new HashMap<>();

            variables.put("name", "DAPA");
            helper.setText(thymeleafService.createContext("send-mail-test.html", variables),true);
            helper.setSubject("Mail Test");
            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void sendEmail(String to)
    {
    try {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
        message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
                
        helper.setFrom("muthmauleaaa@gmail.com", "Admin DAPA STORE");
        helper.setTo(to);
        helper.setSubject("Customer Registration");
        
        String htmlContent = thymeleafService.createContext("register-mail.html",null);
        helper.setText(htmlContent, true);
        
        emailSender.send(message);
        
    } catch (Exception e) {
        e.printStackTrace();
    }
}

 @Override
    public void sendHtmlMessage(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("noreply@dapa.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            emailSender.send(message);
        } catch (MessagingException | MailException ex) {
            ex.printStackTrace();
        }
    }




    @Override
    public void sendOTPEmail(String userName, String OTP) {
        try {
            
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            
        helper.setFrom("muthmauleaaa@gmail.com", "Admin DAPA STORE");
        helper.setTo(userName);

        Map<String, Object> variables = new HashMap<>();

        variables.put("email", userName);
        variables.put("otp", OTP);
        // helper.setText(html, true);
        helper.setText(thymeleafService.createContext("email-reset-pw.html", variables),true);
        helper.setSubject("Reset Password | OTP");
        emailSender.send(message);
        } catch (Exception e) {

        }
    }


    @Override
    public void sendHtmlMessageWithAttachment(String to, String subject, String text, String attachmentName, byte[] attachment) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("noreply@dapa.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            helper.addAttachment(attachmentName, new ByteArrayResource(attachment));
            emailSender.send(message);
        } catch (MessagingException | MailException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void sendVoucherEmail(String recipient, String subject, List<Voucher> vouchers) {
        try {
            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            helper.setFrom("muthmauleaaa@gmail.com", "Admin DAPA STORE");
            helper.setTo(recipient);
            helper.setSubject(subject);

            Map<String, Object> variables = new HashMap<>();
            variables.put("vouchers", vouchers); 
            helper.setText(thymeleafService.createContext("voucer.html", variables), true);

            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Voucher voucher : vouchers) {
            voucerRepository.save(voucher);
        }
    }

}
