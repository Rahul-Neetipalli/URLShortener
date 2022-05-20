package com.shorten.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(value="URL")
public class URLObject {

	private String longUrl;
	@Id
	private String shortUrlAlias;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date expiryDate;
	
	public URLObject(String longUrl, String shortUrlAlias, Date expiryDate) {
		this.longUrl = longUrl;
		this.shortUrlAlias = shortUrlAlias;
		this.expiryDate = expiryDate;
	}
	
	public String getLongUrl() {
		return longUrl;
	}
	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}
	
	public String getShortUrlAlias() {
		return shortUrlAlias;
	}

	public void setShortUrlAlias(String shortUrlAlias) {
		this.shortUrlAlias = shortUrlAlias;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
}
