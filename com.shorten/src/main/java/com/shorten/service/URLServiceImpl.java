package com.shorten.service;

import java.util.Date;
import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.shorten.common.CommonUtils;
import com.shorten.common.Constants;
import com.shorten.exceptions.AliasException;
import com.shorten.exceptions.ExpiredURLException;
import com.shorten.exceptions.InvalidExpiryDateException;
import com.shorten.model.URLObject;
import com.shorten.repository.MongoDBClient;

@Service
public class URLServiceImpl implements URLService{
    
    @Autowired
    MongoDBClient mongoClient;
    
    private static Semaphore mutex = new Semaphore(1);
    
    private static Logger LOGGER = LogManager.getLogger(URLServiceImpl.class);
    
    public String generateShortUrl(URLObject urlObject) throws InterruptedException{
	String longUrl = urlObject.getLongUrl();
	String customShortUrlAlias = urlObject.getShortUrlAlias();
	Date expiryDate = urlObject.getExpiryDate();
	
	boolean customShortUrlVerified = false;
	if(longUrl == null) {
	    // Enforcing longUrl mandatory condition
	    throw new IllegalArgumentException("Long URL is not provided.");
	}
	if(CommonUtils.isCurrentDateGreaterThan(expiryDate)) {
		throw new InvalidExpiryDateException("Expiry Date is not valid.");
	}
	try {
	    mutex.acquire();
	    if(customShortUrlAlias != null) {
		LOGGER.debug("Custom Short URL Alias is provided by the user. Verifing availability.");
		
		if(!CommonUtils.isValidShortUrlAlias(customShortUrlAlias)) {
		    throw new IllegalArgumentException("URL Alias cannot contain . / or \\"
		    	+ " and cannot be longer than 12 characters.");
		}
		
		if(!mongoClient.findIfExists(customShortUrlAlias)) {
		    customShortUrlVerified = true;
		} else {
		    throw new AliasException("URL Alias entered : " + customShortUrlAlias + " is already taken.");
		}
	    }
	    if(!customShortUrlVerified) {
		LOGGER.debug("Generating a unique short URL Alias.");
		String shortUrlAlias =  generateShortUrlAlias(longUrl);	
		urlObject.setShortUrlAlias(shortUrlAlias);
	    }
	    mongoClient.insertIntoDatabase(urlObject);
	    return Constants.urlDomain + urlObject.getShortUrlAlias();
	} finally {
	    mutex.release();
	}
    }
	
    public String getLongUrl(String shortUrlAlias) {
	if(shortUrlAlias == null) {
	    throw new IllegalArgumentException("URL Alias not provided.");
	}
	if(!mongoClient.findIfExists(shortUrlAlias)) {
	    throw new AliasException("The URL you entered is not configured in the Database.");
	}
	URLObject urlObject = mongoClient.fetchURLObject(shortUrlAlias);
	if(CommonUtils.isCurrentDateGreaterThan(urlObject.getExpiryDate())) {
	    throw new ExpiredURLException("This URL has expired.");
	}
	return urlObject.getLongUrl();
    }



    private String generateShortUrlAlias(String longUrl) {
	/*
	 * Uses md5 algorithm 
	 */
        String shortUrlAlias = DigestUtils.md5DigestAsHex(
        			(longUrl + new Date().toInstant()).getBytes());
        if(!mongoClient.findIfExists(shortUrlAlias)) {
            return shortUrlAlias;
        }
    	return generateShortUrlAlias(longUrl);
    }
}
