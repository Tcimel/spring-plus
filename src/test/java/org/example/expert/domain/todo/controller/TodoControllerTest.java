package org.example.expert.domain.todo.controller;

import org.example.expert.config.AuthUserArgumentResolver;
import org.example.expert.config.GlobalExceptionHandler;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.service.TodoService;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TodoController.class)
// @ExtendWith(MockitoExtension.class)
// @Import(GlobalExceptionHandler.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    // @InjectMocks
    // private TodoController todoController;
    //
    // private final ObjectMapper objectMapper = new ObjectMapper();

    /*@BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(todoController)
            .setCustomArgumentResolvers(new TestAuthUserArgumentResolver())
            .build();
    }

    @Test
    @DisplayName("todo 저장 성공 테스트")
    void todo_저장_성공() throws Exception {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com",UserRole.USER,"user");
        User user = User.fromAuthUser(authUser);

        TodoSaveRequest request = new TodoSaveRequest("title","content");
        TodoSaveResponse response = new TodoSaveResponse(1L,"title","content","Sunny",new UserResponse(user.getId(),user.getEmail(),user.getNickName()));

        // when
        when(todoService.saveTodo(any(AuthUser.class),any(TodoSaveRequest.class))).thenReturn(response);

        // then
        mockMvc.perform(post("/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("title"));;
    }*/

    @Test
    void todo_단건_조회에_성공한다() throws Exception {
        // given
        long todoId = 1L;
        String title = "title";
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER,"user");
        User user = User.fromAuthUser(authUser);
        UserResponse userResponse = new UserResponse(user.getId(), user.getEmail(),user.getNickName());
        TodoResponse response = new TodoResponse(
                todoId,
                title,
                "contents",
                "Sunny",
                userResponse,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when
        when(todoService.getTodo(todoId)).thenReturn(response);

        // then
        mockMvc.perform(get("/todos/{todoId}", todoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todoId))
                .andExpect(jsonPath("$.title").value(title));
    }

    @Test
    void todo_단건_조회_시_todo가_존재하지_않아_예외가_발생한다() throws Exception {
        // given
        long todoId = 1L;

        // when
        when(todoService.getTodo(todoId))
                .thenThrow(new InvalidRequestException("Todo not found"));

        // then
        mockMvc.perform(get("/todos/{todoId}", todoId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Todo not found"));
    }

    /*public class TestAuthUserArgumentResolver implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.hasParameterAnnotation(Auth.class);  // 너희 @Auth 조건
        }

        @Override
        public Object resolveArgument(MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
            return new AuthUser(1L, "test@example.com", UserRole.USER, "user"); // Dummy 유저
        }
    }*/
}
