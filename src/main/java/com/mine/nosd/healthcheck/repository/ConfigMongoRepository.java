package com.mine.nosd.healthcheck.repository;

import com.mine.nosd.healthcheck.model.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigMongoRepository extends MongoRepository<Configuration, String> {

}

