package com.waes.differ.service.implementation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.waes.differ.entity.DiffEntity;
import com.waes.differ.exception.DataNotFoundException;
import com.waes.differ.exception.InvalidDiffDataException;
import com.waes.differ.model.Diff;
import com.waes.differ.repository.DiffRepository;
import com.waes.differ.service.implementation.DefaultDiffService;

@ExtendWith(MockitoExtension.class)
public class DefaultDiffServiceTest {

	private static final String ID = "1";

	@InjectMocks
	DefaultDiffService defaultDiffService;

	@Mock
	DiffRepository diffRepository;

	@Test
	@DisplayName("Test CreateLeftData For New Id")
	public void testCreateLeftDataNew() {

		// given
		byte[] left = "YWFhYmJiY2NjZGRkMjIy".getBytes();

		doReturn(Optional.empty()).when(diffRepository).findById(ID);

		// when
		defaultDiffService.createLeftData(ID, left);

		// then
		DiffEntity expected = new DiffEntity(ID);
		expected.setLeft(left);

		verify(diffRepository).save(expected);
	}

	@Test
	@DisplayName("Test CreateLeftData with existing Right Data")
	public void testCreateLeftDataWhenRightExists() {

		// given
		byte[] right = "YWEzMzNiY2NjZHFxcXFx".getBytes();
		DiffEntity existing = new DiffEntity(ID);
		existing.setRight(right);

		doReturn(Optional.of(existing)).when(diffRepository).findById(ID);

		// when
		byte[] left = "YWFhYmJiY2NjZGRkMjIy".getBytes();
		defaultDiffService.createLeftData(ID, left);

		// then
		DiffEntity expected = new DiffEntity(ID);
		expected.setLeft(left);
		expected.setRight(right);

		verify(diffRepository).save(expected);
	}

	@Test
	@DisplayName("Test CreateLeftData to overwrite existing Left Data")
	public void testCreateLeftDataOverwriteExisting() {

		// given
		DiffEntity existing = new DiffEntity(ID);
		existing.setLeft("YWEzMzNiY2NjZHFxcXFx".getBytes());

		doReturn(Optional.of(existing)).when(diffRepository).findById(ID);

		// when
		byte[] left = "YWFhYmJiY2NjZGRkMjIy".getBytes();
		defaultDiffService.createLeftData(ID, left);

		// then
		DiffEntity expected = new DiffEntity(ID);
		expected.setLeft(left);

		verify(diffRepository).save(expected);
	}

	@Test
	@DisplayName("Test CreateRightData For New Id")
	public void testCreateRightDataNew() {

		// given
		byte[] right = "YWFhYmJiY2NjZGRkMjIy".getBytes();

		doReturn(Optional.empty()).when(diffRepository).findById(ID);

		// when
		defaultDiffService.createRightData(ID, right);

		// then
		DiffEntity expected = new DiffEntity(ID);
		expected.setRight(right);

		verify(diffRepository).save(expected);
	}

	@Test
	@DisplayName("Test CreateRightData with existing Left Data")
	public void testCreateRightDataWhenLeftExists() {

		// given
		byte[] left = "YWEzMzNiY2NjZHFxcXFx".getBytes();
		DiffEntity existing = new DiffEntity(ID);
		existing.setLeft(left);

		doReturn(Optional.of(existing)).when(diffRepository).findById(ID);

		// when
		byte[] right = "YWFhYmJiY2NjZGRkMjIy".getBytes();
		defaultDiffService.createRightData(ID, right);

		// then
		DiffEntity expected = new DiffEntity(ID);
		expected.setRight(right);
		expected.setLeft(left);

		verify(diffRepository).save(expected);
	}

	@Test
	@DisplayName("Test CreateRightData to overwrite existing Right Data")
	public void testCreateRightDataOverwriteExisting() {

		// given
		DiffEntity existing = new DiffEntity(ID);
		existing.setRight("YWEzMzNiY2NjZHFxcXFx".getBytes());

		doReturn(Optional.of(existing)).when(diffRepository).findById(ID);

		// when
		byte[] right = "YWFhYmJiY2NjZGRkMjIy".getBytes();
		defaultDiffService.createRightData(ID, right);

		// then
		DiffEntity expected = new DiffEntity(ID);
		expected.setRight(right);

		verify(diffRepository).save(expected);
	}

	@Test
	@DisplayName("Test RetrieveDiff For Non-Existing Id")
	public void testRetrieveDiffThrowsExceptionWhenIdNotFound() {

		// given
		doReturn(Optional.empty()).when(diffRepository).findById(ID);

		// when, then
		assertThrows(DataNotFoundException.class, () -> {
			defaultDiffService.retrieveDiff(ID);
		});
	}

	@Test
	@DisplayName("Test RetrieveDiff For Different Size of Right and Left Data")
	public void testRetrieveDiffThrowsExceptionWhenDataLenghtsDiffer() {

		// given
		DiffEntity existing = new DiffEntity(ID);
		existing.setLeft("YWEzMzNiY2NjZHFxcXFxYWEzMzNiY2NjZHFxcXFx".getBytes());
		existing.setRight("YWEzMzNiY2NjZHFxcXFx".getBytes());

		doReturn(Optional.of(existing)).when(diffRepository).findById(ID);

		// when, then
		assertThrows(InvalidDiffDataException.class, () -> {
			defaultDiffService.retrieveDiff(ID);
		});
	}

	@Test
	@DisplayName("Test RetrieveDiff For Left Data is Null")
	public void testRetrieveDiffThrowsExceptionWhenLeftDataIsNull() {

		// given
		DiffEntity existing = new DiffEntity(ID);
		existing.setRight("YWEzMzNiY2NjZHFxcXFx".getBytes());

		doReturn(Optional.of(existing)).when(diffRepository).findById(ID);

		// when, then
		assertThrows(InvalidDiffDataException.class, () -> {
			defaultDiffService.retrieveDiff(ID);
		});
	}

	@Test
	@DisplayName("Test RetrieveDiff For Right Data is Null")
	public void testRetrieveDiffThrowsExceptionWhenRightDataIsNull() {

		// given
		DiffEntity existing = new DiffEntity(ID);
		existing.setLeft("YWEzMzNiY2NjZHFxcXFx".getBytes());

		doReturn(Optional.of(existing)).when(diffRepository).findById(ID);

		// when, then
		assertThrows(InvalidDiffDataException.class, () -> {
			defaultDiffService.retrieveDiff(ID);
		});
	}

	@Test
	@DisplayName("Test RetrieveDiff For the same Right and Left Data")
	public void testRetrieveDiffWhenLeftAndRightEquals() {

		// given
		DiffEntity existing = new DiffEntity(ID);
		existing.setLeft("YWFhYmJiY2NjZGRkMjIy".getBytes());
		existing.setRight("YWFhYmJiY2NjZGRkMjIy".getBytes());

		doReturn(Optional.of(existing)).when(diffRepository).findById(ID);

		// when
		List<Diff> diffs = defaultDiffService.retrieveDiff(ID);

		// then
		assertThat(diffs, empty());
	}

	@Test
	@DisplayName("Test RetrieveDiff For Right and Left Data have only one byte differ")
	public void testRetrieveDiffWhenLeftAndRightDiffersOneByte() {

		// given
		DiffEntity existing = new DiffEntity(ID);
		existing.setLeft("YWFhYmJiY2NjZGRkMjIy".getBytes());
		existing.setRight("YWFhYmJiY2NjZGRkMjIz".getBytes());

		doReturn(Optional.of(existing)).when(diffRepository).findById(ID);

		// when
		List<Diff> diffs = defaultDiffService.retrieveDiff(ID);

		// then
		assertThat(diffs, Matchers.hasSize(1));
		assertThat(diffs.get(0), equalTo(new Diff(19, 1)));
	}

	@Test
	@DisplayName("Test RetrieveDiff For Right and Left Data are completely different")
	public void testRetrieveDiffWhenLeftAndRightDiffersAllBytes() {

		// given
		DiffEntity existing = new DiffEntity(ID);
		existing.setLeft("MTEx".getBytes()); // 111
		existing.setRight("d3d3".getBytes()); // www

		doReturn(Optional.of(existing)).when(diffRepository).findById(ID);

		// when
		List<Diff> diffs = defaultDiffService.retrieveDiff(ID);

		// then
		assertThat(diffs, Matchers.hasSize(1));
		assertThat(diffs.get(0), equalTo(new Diff(0, 4)));
	}
	
	@Test
	@DisplayName("Test RetrieveDiff For Right and Left Data have more than one differ")
	public void testRetrieveDiffWhenLeftAndRightDiffersMultipleBytes() {

		// given
		DiffEntity existing = new DiffEntity(ID);
		existing.setLeft("MTExMQ==".getBytes()); // 1111
		existing.setRight("MjIyMg==".getBytes()); // 2222

		doReturn(Optional.of(existing)).when(diffRepository).findById(ID);

		// when
		List<Diff> diffs = defaultDiffService.retrieveDiff(ID);

		// then
		assertThat(diffs, Matchers.hasSize(2));
		assertThat(diffs.get(0), equalTo(new Diff(1, 3)));
		assertThat(diffs.get(1), equalTo(new Diff(5, 1)));
	}

}
