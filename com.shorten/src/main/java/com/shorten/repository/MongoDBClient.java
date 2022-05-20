package com.shorten.repository;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shorten.model.URLObject;

@Component
public class MongoDBClient{
	
	@Autowired
	private URLRepository urlRepository;
	
	private static Logger LOGGER = LogManager.getLogger(MongoDBClient.class);
	
	public void insertIntoDatabase(URLObject urlObject) {
	    LOGGER.info("Inserting new URLObject into Database");
	    urlRepository.save(urlObject);
	}
	public boolean findIfExists(String shortUrlAlias) {
	    return urlRepository.existsById(shortUrlAlias);
	}
	
	public URLObject fetchURLObject(String shortUrlAlias) {
	    Optional<URLObject> urlObject = urlRepository.findById(shortUrlAlias);
	    if(!urlObject.isPresent()) {
		throw new IllegalArgumentException("No object could be retrieved with the alias " 
			    				+ shortUrlAlias);
	    }
	    return urlObject.get();
	}
}
