package org.example.expert.domain.todo.dto.response;

import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TodoSearchResponse {

	private final String title;
	private final String nickName;
	private final int managerCount;
	private final int CommentCount;
	private final LocalDateTime createdAt;

}
