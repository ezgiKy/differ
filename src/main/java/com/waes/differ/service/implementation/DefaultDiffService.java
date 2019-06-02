package com.waes.differ.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.waes.differ.entity.DiffEntity;
import com.waes.differ.exception.DataNotFoundException;
import com.waes.differ.exception.InvalidDiffDataException;
import com.waes.differ.model.Diff;
import com.waes.differ.repository.DiffRepository;
import com.waes.differ.service.DiffService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultDiffService implements DiffService {

	private final DiffRepository diffRepository;

	@Override
	public void createLeftData(String id, byte[] left) {

		DiffEntity diffEntity = diffRepository.findById(id).orElse(new DiffEntity(id));
		diffEntity.setLeft(left);
		diffRepository.save(diffEntity);
		
		log.info("DiffEntity id: {} is updated with left data", diffEntity.getId());
	}

	@Override
	public void createRightData(String id, byte[] right) {

		DiffEntity diffEntity = diffRepository.findById(id).orElse(new DiffEntity(id));
		diffEntity.setRight(right);
		diffRepository.save(diffEntity);
		
		log.info("DiffEntity id: {} is updated with right data", diffEntity.getId());
	}

	@Override
	public List<Diff> retrieveDiff(String id) {

		DiffEntity diffEntity = getEntity(id);

		validateDiff(diffEntity);

		return findDiffs(diffEntity);
	}

	/**
	 * In order to detect possible differences, both arrays are iterated until a
	 * difference is found. When a difference found, both arrays are iterated until
	 * difference or arrays end.
	 * 
	 * @param diffEntity
	 * @return list of differences
	 */
	private List<Diff> findDiffs(DiffEntity diffEntity) {

		final List<Diff> differences = new ArrayList<>();

		byte[] left = diffEntity.getLeft();
		byte[] right = diffEntity.getRight();

		int index = 0;
		int length = left.length;

		while (index < length) {

			if (left[index] == right[index]) {
				index++;
				continue;
			}

			int offset = index;
			while (index < length && left[index] != right[index]) {
				index++;
			}

			Diff diff = new Diff(offset, index - offset);
			differences.add(diff);
		}
		
		log.info("Differences found for id: {}, as {}", diffEntity.getId(), differences);
		return differences;
	}

	private void validateDiff(DiffEntity diffEntity) {

		byte[] left = diffEntity.getLeft();
		byte[] right = diffEntity.getRight();

		if (left == null || right == null) {
			throw new InvalidDiffDataException("Left or Right Data could not be null.");
		}

		if (left.length != right.length) {
			throw new InvalidDiffDataException("Left and Right Data should have the same length.");
		}
	}

	private DiffEntity getEntity(String id) {
		return diffRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("Could not found data with given id"));
	}

}
