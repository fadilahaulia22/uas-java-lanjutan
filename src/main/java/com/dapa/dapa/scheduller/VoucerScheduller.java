package com.dapa.dapa.scheduller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dapa.dapa.entity.Customer;
import com.dapa.dapa.entity.Voucher;
import com.dapa.dapa.service.EmailService;
import com.dapa.dapa.service.customer.CustomerService;
import com.dapa.dapa.service.voucer.VoucerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VoucerScheduller {

    private final VoucerService voucherService;
    private final EmailService emailService;
    private final CustomerService customerService;

    @Autowired
    public VoucerScheduller(VoucerService voucherService, EmailService emailService, CustomerService customerService) {
        this.voucherService = voucherService;
        this.emailService = emailService;
        this.customerService = customerService;
    }

    @Scheduled(fixedRate = 120000) // Mengirim voucher setiap 2 menit (2 * 60 * 1000 milidetik)
    public void generateFreeShippingVouchersScheduledTask() {
        List<Voucher> vouchers = voucherService.generateFreeShippingVouchers();
        sendVouchersByEmail(vouchers);
    }

   
    // @Scheduled(cron = "0 0 23 * * ?") // Generate free shipping vouchers every day at midnight
    // public void generateFreeShippingVouchersScheduledTask() {
    //     List<Voucer> vouchers = voucherService.generateFreeShippingVouchers();
    //     sendVouchersByEmail(vouchers);
    // }

    private void sendVouchersByEmail(List<Voucher> vouchers) {
        List<Customer> customers = customerService.getAllCustomers();
        for (Customer customer : customers) {
            String recipient = customer.getEmail();
            if (recipient != null && !recipient.isEmpty()) {
                String subject = "Your Voucher";
                emailService.sendVoucherEmail(recipient, subject, vouchers);
            } else {
                log.warn("Recipient email is missing for customer with ID: {}", customer.getId());
            }
        }
    }

}

