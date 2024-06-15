package com.dapa.dapa.service.voucer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dapa.dapa.entity.Voucher;

@Service
public class VoucerServiceImpl implements VoucerService{

    public String generateRandomVoucherCode() {
        return UUID.randomUUID().toString().substring(0, 8); // Menggunakan UUID untuk membuat kode acak
    }

    @Override
    public List<Voucher> generateFreeShippingVouchers() {
        List<Voucher> vouchers = new ArrayList<>();
        // for (int i = 0; i < 10; i++) {
            Voucher voucher = new Voucher();
            voucher.setCode(generateRandomVoucherCode()); // Gunakan metode generateRandomVoucherCode() untuk kode acak
            voucher.setType("FreeShipping");
            voucher.setExpirationDate(calculateExpirationDate());
            vouchers.add(voucher);
        // }
        return vouchers;
    }

    private LocalDate calculateExpirationDate() {
        return LocalDate.now().plusDays(30);
    }
}
