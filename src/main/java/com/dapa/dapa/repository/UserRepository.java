package com.dapa.dapa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dapa.dapa.entity.Roles;
import com.dapa.dapa.entity.Users;


public interface UserRepository extends JpaRepository<Users, String> {
    Optional <Users> findByUsername(String username);
    Users findUsersByUsername(String username);
    Users findByRoles(Roles roles);
}
