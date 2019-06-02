package com.waes.differ.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidBase64InputException extends RuntimeException {

	private static final long serialVersionUID = 7008473542566992555L;

	public InvalidBase64InputException() {
		super();
	}

	public InvalidBase64InputException(String arg0) {
		super(arg0);
	}

}
