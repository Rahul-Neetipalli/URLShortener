package com.shorten.exceptions;

public class ExpiredURLException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	
	public ExpiredURLException(String message) {
		super(message);
	}

}
