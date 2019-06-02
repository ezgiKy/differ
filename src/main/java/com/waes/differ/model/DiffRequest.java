package com.waes.differ.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * Holds left or right data in Base64 String
 *
 */
@Data
public class DiffRequest {
	
	private static final int MAX_SIZE = 1024 * 1024; //Assumed that input size might be 1M at most.

	@NotNull(message = "Data cannot be null")
	@NotBlank(message = "Data cannot be empty")
	@Size(max = MAX_SIZE, message = "Data size must be less than 1M")
	private String data;

}
