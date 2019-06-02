package com.waes.differ.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDiffDataException extends RuntimeException {

	private static final long serialVersionUID = -3601114928583142725L;

	public InvalidDiffDataException() {
		super();
	}

	public InvalidDiffDataException(String arg0) {
		super(arg0);
	}

}
