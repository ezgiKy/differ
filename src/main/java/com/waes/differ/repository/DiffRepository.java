package com.waes.differ.repository;

import org.springframework.data.repository.CrudRepository;

import com.waes.differ.entity.DiffEntity;

public interface DiffRepository extends CrudRepository<DiffEntity, String> {
}
