package com.shorten.service;

import org.springframework.stereotype.Service;

import com.shorten.exceptions.AliasException;
import com.shorten.exceptions.ExpiredURLException;
import com.shorten.exceptions.InvalidExpiryDateException;
import com.shorten.model.URLObject;


@Service
public interface URLService {

	public String generateShortUrl(URLObject urlObject) 
			throws AliasException,InvalidExpiryDateException, InterruptedException;
	
	public String getLongUrl(String shortUrlAlias) 
			throws IllegalArgumentException, AliasException, 
				ExpiredURLException;
	
}
