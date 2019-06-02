package com.waes.differ.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "differences")
public class DiffEntity {

	@Id
	private String id;

	@Lob
	private byte[] left;

	@Lob
	private byte[] right;

	public DiffEntity(String id) {
		this.id = id;
	}
}
