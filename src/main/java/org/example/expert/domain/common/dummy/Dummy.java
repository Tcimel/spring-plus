/*
package org.example.expert.domain.common.dummy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.IntStream;

import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("local")
@Transactional
public class Dummy implements ApplicationRunner {

	private final UserRepository userRepository;
	private final TodoRepository todoRepository;
	private final CommentRepository commentRepository;
	private final PasswordEncoder passwordEncoder;
	private final Random random = new Random();
	private final String[] weathers = {"Sunny","Rainy","Cloudy","Snowy","Windy"};

	public Dummy(UserRepository userRepository, TodoRepository todoRepository, CommentRepository commentRepository, PasswordEncoder passwordEncoder){
		this.userRepository = userRepository;
		this.todoRepository = todoRepository;
		this.commentRepository = commentRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		String encodedPassword = passwordEncoder.encode("test1234");
		//User 100명 생성
		IntStream.rangeClosed(1, 10).forEach(i->{
			User user = new User(
				"user"+i+"@example.com",
				encodedPassword,
				UserRole.USER,
				"User"+i
			);
			userRepository.save(user);

			IntStream.rangeClosed(1, 20).forEach(j->{
				String weather = weathers[random.nextInt(weathers.length)];
				LocalDateTime randomDate = LocalDateTime.now().minusDays(random.nextInt(20));  // 0 ~ 19 사이 랜덤 일수를 뺀 날짜

				Todo todo = new Todo(
					"User" + i + "의 Todo" + j,
					"내용",
					weather,
					user,
					randomDate,
					randomDate.plusHours(random.nextInt(24))
				);

				Todo savedTodo = todoRepository.save(todo);

				// todo.setCreatedAt(randomDate);
				// todo.setModifiedAt(randomDate.plusHours(random.nextInt(24)));
				LocalDateTime randomModifiedAt = randomDate.plusHours(random.nextInt(24));

				todoRepository.updateTimestamps(savedTodo.getId(), randomDate, randomModifiedAt);

				int commentCount = 3 + random.nextInt(3);
				IntStream.rangeClosed(1, commentCount).forEach(k -> {
					User randomUser = userRepository.findById((long)1+random.nextInt(10)).orElse(user);
					Comment comment = new Comment(
						"Comment " + k + "for Todo " + j,
						randomUser,
						savedTodo
					);
					commentRepository.save(comment);
				});

			});
		});
	}
}
*/
