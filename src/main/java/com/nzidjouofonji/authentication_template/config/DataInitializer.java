package com.nzidjouofonji.authentication_template.config;


import com.nzidjouofonji.authentication_template.models.EntitiesRoleName;
import com.nzidjouofonji.authentication_template.models.Role;
import com.nzidjouofonji.authentication_template.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;


    @Autowired
    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @PostConstruct
    public void init() {
        // Initialiser les rôles si nécessaires
        if (roleRepository.findByName(EntitiesRoleName.ROLE_USER).isEmpty()) {
            roleRepository.save(new Role(EntitiesRoleName.ROLE_USER));
        }

        if (roleRepository.findByName(EntitiesRoleName.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(EntitiesRoleName.ROLE_ADMIN));
        }
    }
}
