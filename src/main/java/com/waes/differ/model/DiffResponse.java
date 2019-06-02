package com.waes.differ.model;

import java.util.List;

import lombok.Value;

/**
 * Holds response entity for differences.
 * 
 * @Value is used for immutable entity.
 *
 */
@Value
public class DiffResponse {

	private List<Diff> diffs;

	private String message;

}
