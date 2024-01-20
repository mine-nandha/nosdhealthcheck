package com.mine.nosd.healthcheck.repository;

import com.mine.nosd.healthcheck.model.Excel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcelMongoRepository extends MongoRepository<Excel, String> {

}
