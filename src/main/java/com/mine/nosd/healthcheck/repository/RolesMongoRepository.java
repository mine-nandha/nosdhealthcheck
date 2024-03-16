package com.mine.nosd.healthcheck.repository;

import com.mine.nosd.healthcheck.model.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesMongoRepository extends MongoRepository<Roles, String> {

}
