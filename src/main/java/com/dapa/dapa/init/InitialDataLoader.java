package com.dapa.dapa.init;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.dapa.dapa.constant.RolesConstant;
import com.dapa.dapa.entity.Roles;
import com.dapa.dapa.repository.RolesRepository;



@Component //komponen spring yang general
public class InitialDataLoader implements ApplicationRunner{
    @Autowired
    RolesRepository rolesRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Roles> roles = rolesRepository.findAll();

        if(roles.isEmpty()){
            Roles accountant = new Roles(null, RolesConstant.ACCOUNTANT_ROLE, "Role as Accountant in Application");
            Roles customer = new Roles(null, RolesConstant.CUSTOMER_ROLE, "Role as Customer in Application");
            Roles warehouse = new Roles(null, RolesConstant.WAREHOUSE_ROLE, "Role as Warehouse in Application");

            rolesRepository.saveAll(List.of(accountant, customer, warehouse));
        }
    }
    
}

