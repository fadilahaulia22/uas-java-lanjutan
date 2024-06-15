package com.dapa.dapa.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.dapa.dapa.entity.Roles;

public interface RolesRepository extends JpaRepository<Roles, String> {
    Roles findByRoleName(String roleName);
}
