package com.shorten.exceptions;

public class InvalidExpiryDateException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InvalidExpiryDateException(String message) {
		super(message);
	}

}
