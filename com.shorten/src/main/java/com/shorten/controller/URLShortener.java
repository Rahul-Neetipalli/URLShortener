package com.shorten.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.shorten.common.Constants;
import com.shorten.exceptions.AliasException;
import com.shorten.exceptions.ExpiredURLException;
import com.shorten.exceptions.InvalidExpiryDateException;
import com.shorten.model.URLObject;
import com.shorten.service.URLService;

@RestController
@EnableMongoRepositories ("com.shorten.repository")
public class URLShortener {
	
	@Autowired
	URLService urlService;
	
	private static Logger LOGGER = LogManager.getLogger(URLShortener.class);
	
	/*
	 * 
	 * Takes in a json object with 3 fields shortUrlAlias, longUrl, expiryDate
	 * longUrl is a mandatory field
	 *
	 * 
	 * @param shortUrlAlias in case user provides a custom url alias
	 * @param longUrl       the long Url user wants to shorten
	 * @param expiryDate    yyyy-MM-dd format if user enters an expiry date for the shortUrl
	 * 
	 * @returns shortUrl
	 * 
	 */
	
	@PostMapping("/v1/shortUrl")
	@ResponseBody
	public ResponseEntity<String> generateShortUrl(@RequestBody URLObject urlObject) {
		try {
			return ResponseEntity.ok().body(urlService.generateShortUrl(urlObject));
		} catch (InvalidExpiryDateException | IllegalArgumentException e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (AliasException e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.unprocessableEntity().body(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
	
	/*
	 * 
	 * Picks the shortUrlAlias from the path
	 * 
	 * @returns RedirectView Automatically redirects to the corresponding longUrl
	 * 
	 * @param shortUrlAlias 
	 * 
	 */
	
	@RequestMapping("/{shortUrlAlias}")
	public RedirectView redirectToLongUrl(@PathVariable String shortUrlAlias) {
		try {
			return new RedirectView(urlService.getLongUrl(shortUrlAlias));
		} catch (IllegalArgumentException e) {
			LOGGER.error(e.getMessage());
			return new RedirectView(Constants.urlDomain + "redirect?message=" + e.getMessage());
		}catch(AliasException | ExpiredURLException e) {
			LOGGER.error(e.getMessage());
			return new RedirectView(Constants.urlDomain + "redirect?message=" + e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new RedirectView(Constants.urlDomain + "redirect?message=" + e.getMessage());
		}
		
	}
	
	/*
	 * Displays the error message whenever redirecting to long url fails
	 */
        @RequestMapping("/redirect")
        public String greeting(@RequestParam(value="message", defaultValue="Internal Error Occured") String message) {
            return "Your request failed with the following error: " + message;
        }
}
