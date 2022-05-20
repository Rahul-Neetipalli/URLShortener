package com.shorten.common;

import java.util.Date;

public class CommonUtils {
   
    public static boolean isCurrentDateGreaterThan(Date expiryDate) {
	if(expiryDate == null) {
	// This addresses NO Expiry Date scenario
	    return false; 
	}
	Date currentDate = new Date();
	// date1.compareTo(date2) > 0 => date1 is after/greater than date2
	if(currentDate.compareTo(expiryDate) > 0) {
	    return true; 
	}
	return false;
    }
    
    public static boolean isValidShortUrlAlias(String shortUrlAlias) {
	for(String invalidCharacter : Constants.invalidCharacters) {
	    if(shortUrlAlias.contains(invalidCharacter)) {
		return false;
	    }
	}
	if(shortUrlAlias.length()>12) {
	    return false;
	}
	return true;
    }
}
