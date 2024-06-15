package com.dapa.dapa.entity;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Roles {
    @Id
    @UuidGenerator
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @Column(name = "role_name", length = 100)
    private String roleName;

    @Column(name = "role_desc", length = 255)
    private String roleDesc;
}
