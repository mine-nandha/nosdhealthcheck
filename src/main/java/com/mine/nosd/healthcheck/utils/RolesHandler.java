package com.mine.nosd.healthcheck.utils;

import com.mine.nosd.healthcheck.model.Roles;
import com.mine.nosd.healthcheck.repository.RolesMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesHandler {
    public RolesMongoRepository rolesMongoRepository;

    public RolesHandler(RolesMongoRepository rolesMongoRepository) {
        this.rolesMongoRepository = rolesMongoRepository;
    }

    public List<Roles> listAllRoles() {
        return rolesMongoRepository.findAll();
    }

    public void updateRole(Roles roles) {
        rolesMongoRepository.save(roles);
    }

    public Roles findRolesByName(String name) {
        return rolesMongoRepository.findById(name).orElse(null);
    }

    public void deleteRolesByName(String name) {
        rolesMongoRepository.deleteById(name);
    }


}
