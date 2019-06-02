package com.waes.differ.util;

import java.util.Base64;

import com.waes.differ.exception.InvalidBase64InputException;

public class Base64Utils {

	/**
	 * Converts Base64 String to byte array. Custom exception is thrown when the
	 * given format is invalid. This exception is handled by GlobalExceptionHandler.
	 * 
	 * @param base64 Base64 String
	 * @return Byte array of the given Base64 String
	 */
	public static byte[] decode(String base64) {

		try {
			return Base64.getDecoder().decode(base64);
		} catch (IllegalArgumentException e) {
			throw new InvalidBase64InputException("Invalid Base64 parameter given as input");
		}
	}
}
