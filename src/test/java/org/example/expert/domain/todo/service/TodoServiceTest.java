package org.example.expert.domain.todo.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class TodoServiceTest {

	@Autowired
	private TodoService todoService;

	@MockBean
	private WeatherClient weatherClient;

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("Todo 저장 시 실패")
	void saveTodo() {
		AuthUser authUser = new AuthUser(1L, "test@example.com", UserRole.USER, "user");
		User user = User.fromAuthUser(authUser);
		userRepository.save(user);

		TodoSaveRequest request = new TodoSaveRequest("test title", "test contents");

		// WeatherClient는 Mock -> 항상 "Sunny" 리턴
		given(weatherClient.getTodayWeather()).willReturn("Sunny");

		TodoSaveResponse response = todoService.saveTodo(authUser, request);

		assertThat(response).isNotNull();
		assertThat(response.getId()).isNotNull(); // DB에 저장되어야 id가 있음
		assertThat(response.getTitle()).isEqualTo("test title");
		assertThat(response.getWeather()).isEqualTo("Sunny");
		assertThat(response.getUser().getEmail()).isEqualTo("test@example.com");
	}

	@Test
	void getTodos() {
	}

	@Test
	void getTodo() {
	}
}