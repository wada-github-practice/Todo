package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Todo;

import jakarta.transaction.Transactional;

@Transactional
public interface TodoRepository extends JpaRepository<Todo, Integer> {
	List<Todo> findAllByUsername(String username);

	List<Todo> findAllByStateAndUsername(String state, String username);
	
	Page<Todo> findAllByUsername(String username,Pageable pageable);

	Todo findById(int id);
}