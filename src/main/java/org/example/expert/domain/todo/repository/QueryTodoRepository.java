package org.example.expert.domain.todo.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
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

	Optional<Todo> findByIdWithUser(long todoId);

	Page<TodoSearchResponse> findByWithCondtions(
		String title,
		String date,
		int range,
		String nickName,
		Pageable pageable);
}
