package com.waes.differ.service;

import java.util.List;

import com.waes.differ.model.Diff;

/**
 * Service for saving and calculating operations.   
 *
 */
public interface DiffService {

	/**
	 * Save or update left data with given id.
	 *  
	 * @param id ID
	 * @param left byte array for left data
	 */
	void createLeftData(String id, byte[] left);

	/**
	 * Save or update right data with given id.
	 *  
	 * @param id ID
	 * @param right byte array for left data
	 */
	void createRightData(String id, byte[] right);

	/**
	 * Calculates differences between left and right data for given id.
	 * 
	 * @param id ID
	 * @return list of differences
	 */
	List<Diff> retrieveDiff(String id);

}
