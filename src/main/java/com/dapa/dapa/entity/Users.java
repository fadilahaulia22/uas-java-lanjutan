package com.dapa.dapa.entity;

import java.util.Date;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    private static final long OTP_VALID_DURATION = 5 * 60 * 1000; //just for 5 mnt

    @Id
    @UuidGenerator
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "otp", length = 100)
    private String otp;

    @Column(name = "otp_request_time",length = 100)
    private Date otpReqTime;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Roles roles;

    public boolean isOtpRequired(){
        if (this.getOtp() == null) return false; 
        long currentTimeInMilis = System.currentTimeMillis();
        long otpRequestTimeInMilis = this.otpReqTime.getTime();

        if (otpRequestTimeInMilis + OTP_VALID_DURATION < currentTimeInMilis) return false;
            
        return true;
        
    }

}
