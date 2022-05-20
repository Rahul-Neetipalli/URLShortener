package com.shorten.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.shorten.model.URLObject;

@Repository
public interface URLRepository extends MongoRepository<URLObject, String>{

}
