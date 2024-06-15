package com.dapa.dapa.service.voucer;
import java.util.List;

import com.dapa.dapa.entity.Voucher;

public interface VoucerService {
    List<Voucher> generateFreeShippingVouchers();
}
