package com.waes.differ.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.waes.differ.model.Diff;
import com.waes.differ.model.DiffRequest;
import com.waes.differ.model.DiffResponse;
import com.waes.differ.service.DiffService;
import com.waes.differ.util.Base64Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DiffRestController {

	private final DiffService diffService;

	
	/**
	 * PUT Base64 data to left field 
	 * @param id ID
	 * @param request Base64 String
	 * @return
	 */
	@PutMapping(value = "/v1/diff/{id}/left", consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<?> sendLeftData(@PathVariable String id,
			@RequestBody(required = true) @Valid DiffRequest request) {

		byte[] decodedLeftData = Base64Utils.decode(request.getData());

		diffService.createLeftData(id, decodedLeftData);
		
		log.info("Left data created for id: {}", id);
		
		return ok().build();
	}

	/**
	 * PUT Base64 data to right field 
	 * @param id ID
	 * @param request Base64 String
	 * @return
	 */
	@PutMapping(value = "/v1/diff/{id}/right", consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<?> sendRightData(@PathVariable String id,
			@RequestBody(required = true) @Valid DiffRequest request) {

		byte[] decodedRightData = Base64Utils.decode(request.getData());

		diffService.createRightData(id, decodedRightData);
		
		log.info("Right data created for id: {}", id);
		
		return ok().build();
	}

	/**
	 * GET differences between left and right data
	 * @param id ID
	 * @return list of differences and total differences as a message
	 */
	@GetMapping(value = "/v1/diff/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<DiffResponse> retrieveDiff(@PathVariable String id) {

		final List<Diff> differences = diffService.retrieveDiff(id);
		
		log.info("Calculated diffs for id: {}, total diffs: {}", id, differences.size());

		return ok(new DiffResponse(differences, "Total differences: " + differences.size()));
	}

}
