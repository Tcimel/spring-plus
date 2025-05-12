package org.example.expert.domain.todo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.QueryTodoRepository;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional //(readOnly = true)
public class TodoService {

	private final TodoRepository todoRepository;
	private final WeatherClient weatherClient;
    private final QueryTodoRepository queryTodoRepository;

	public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
		User user = User.fromAuthUser(authUser);

		String weather = weatherClient.getTodayWeather();

		Todo newTodo = new Todo(
			todoSaveRequest.getTitle(),
			todoSaveRequest.getContents(),
			weather,
			user
		);
		// Manager manager = new Manager(user, newTodo);
		// newTodo.getManagers().add(manager);
		Todo savedTodo = todoRepository.save(newTodo);

		return new TodoSaveResponse(
			savedTodo.getId(),
			savedTodo.getTitle(),
			savedTodo.getContents(),
			weather,
			new UserResponse(user.getId(), user.getEmail(), user.getNickName())
		);
	}

	public Page<TodoResponse> getTodos(int page, int size, String weather, String dateStart, String dateEnd) {
		Pageable pageable = PageRequest.of(page - 1, size);

		// LocalDateTime start =
		// 	(dateStart != null && !dateStart.isBlank()) ? LocalDate.parse(dateStart).atStartOfDay() : null;
		// LocalDateTime end =
		// 	(dateEnd != null && !dateEnd.isBlank()) ? LocalDate.parse(dateEnd).atTime(LocalTime.MAX) : null;

		// Page<Todo> todos = todoRepository.findAllWithCondition(weather, start, end, pageable);
		Page<Todo> todos = queryTodoRepository.findAllWithConditions(weather, dateStart, dateEnd, pageable);

		return todos.map(todo -> new TodoResponse(
			todo.getId(),
			todo.getTitle(),
			todo.getContents(),
			todo.getWeather(),
			new UserResponse(todo.getUser().getId(), todo.getUser().getEmail(), todo.getUser().getNickName()),
			todo.getCreatedAt(),
			todo.getModifiedAt()
		));
	}

	public TodoResponse getTodo(long todoId) {
		Todo todo = todoRepository.findByIdWithUser(todoId)
			.orElseThrow(() -> new InvalidRequestException("Todo not found"));

		User user = todo.getUser();

		return new TodoResponse(
			todo.getId(),
			todo.getTitle(),
			todo.getContents(),
			todo.getWeather(),
			new UserResponse(user.getId(), user.getEmail(), user.getNickName()),
			todo.getCreatedAt(),
			todo.getModifiedAt()
		);
	}
}
