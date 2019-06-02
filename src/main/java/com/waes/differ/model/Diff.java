package com.waes.differ.model;

import lombok.Value;

/**
 * Holds information of a single diff. 
 * 
 * @Value is used for immutable entity.
 */
@Value
public class Diff {
	
	private int offset;
	
	private int length;
	
}
