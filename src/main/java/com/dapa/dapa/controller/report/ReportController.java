package com.dapa.dapa.controller.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dapa.dapa.constant.MessageConstant;
import com.dapa.dapa.dto.GenericResponse;
import com.dapa.dapa.service.report.ReportService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="Sales Report")
@Slf4j
public class ReportController {
    @Autowired
    ReportService reportService;

    @GetMapping("/report")
    @PreAuthorize("hasAnyRole('MANAGER', 'ACCOUNTANT')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> report(HttpServletResponse response){
        try{
            response.setHeader("Content-Disposition","attachment; filename=sales-report.xlsx");
            return ResponseEntity.ok(reportService.generateReport());
        }catch (Exception e){
            log.info(e.getMessage());
            return ResponseEntity.internalServerError()
                .body(GenericResponse.error(MessageConstant.ERROR_500));
        }
    }
}

