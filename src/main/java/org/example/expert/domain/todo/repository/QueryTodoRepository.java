package org.example.expert.domain.todo.repository;

import java.time.LocalDateTime;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface QueryTodoRepository {
	Page<Todo> findAllWithConditions(
		String weather,
		String start,
		String end,
		Pageable pageable
	);
}
