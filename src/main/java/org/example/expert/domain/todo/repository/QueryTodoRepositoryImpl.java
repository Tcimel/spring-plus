package org.example.expert.domain.todo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QueryTodoRepositoryImpl implements QueryTodoRepository {
	private final EntityManager entityManager;

	@Override
	public Page<Todo> findAllWithConditions(String weather, String start, String end, Pageable pageable) {
		String jpql = "SELECT t FROM Todo t LEFT JOIN FETCH t.user u";
		List<String> conditions = new ArrayList<>();

		if (StringUtils.hasText(weather)) {
			conditions.add("t.weather = :weather");
		}
		if (StringUtils.hasText(start)) {
			conditions.add("t.modifiedAt >= :start");
		}
		if (StringUtils.hasText(end)) {
			conditions.add("t.modifiedAt <= :end");
		}
		if (!conditions.isEmpty()) {
			jpql += " WHERE " + String.join(" AND ", conditions);
		}

		jpql += " ORDER BY t.modifiedAt DESC";
		TypedQuery<Todo> query = entityManager.createQuery(jpql, Todo.class);

		if (StringUtils.hasText(weather)) {
			query.setParameter("weather", weather);
		}
		if (StringUtils.hasText(start)) {
			query.setParameter("start", LocalDate.parse(start).atStartOfDay());
		}
		if (StringUtils.hasText(end)) {
			query.setParameter("end", LocalDate.parse(end).atTime(LocalTime.MAX));
		}

		query.setFirstResult((int)pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());

		List<Todo> todos = query.getResultList();

		String countJpql = "SELECT COUNT(t) FROM Todo t";
		if (!conditions.isEmpty()) {
			countJpql += " WHERE " + String.join(" AND ", conditions);
		}

		TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
		if (StringUtils.hasText(weather)) {
			countQuery.setParameter("weather", weather);
		}
		if (StringUtils.hasText(start)) {
			countQuery.setParameter("start", LocalDate.parse(start).atStartOfDay());
		}
		if (StringUtils.hasText(end)) {
			countQuery.setParameter("end", LocalDate.parse(end).atTime(LocalTime.MAX));
		}

		long total = countQuery.getSingleResult();

		// todos : 현재 페이지 데이터(pageable로 제한된 목록)
		// total : 전체 데이터 개수
		return new PageImpl<>(todos, pageable, total);
	}

	@Override
	public Optional<Todo> findByIdWithUser(long todoId) {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QTodo todo = QTodo.todo;
		QUser user = QUser.user;

		Todo result = queryFactory
			.selectFrom(todo)
			.leftJoin(todo.user, user).fetchJoin()
			.where(todo.id.eq(todoId))
			.fetchOne(); // 단건 조회

		return Optional.ofNullable(result);
	}

	@Override
	public Page<TodoSearchResponse> findByWithCondtions(String title, String date, int range, String nickName,
		Pageable pageable) {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QTodo todo = QTodo.todo;
		QManager manager = QManager.manager;
		QComment comment = QComment.comment;
		QUser user = QUser.user;

		//OR 조합을 쓰려면, BooleanBuilder를 사용해야한다고 함
		BooleanBuilder builder = new BooleanBuilder();

		BooleanBuilder orBuilder = new BooleanBuilder();
		if(title!=null){
			orBuilder.or(todo.title.contains(title));
		}
		if(nickName!=null){
			orBuilder.or(todo.user.nickName.contains(nickName));
		}

		builder.and(orBuilder);

		if(date!=null){
			builder.and(todo.createdAt.between(
				LocalDate.parse(date).minusDays(range).atStartOfDay(),
				LocalDate.parse(date).atTime(LocalTime.MAX)
			));
		}

		List<TodoSearchResponse> list = queryFactory
			.select(Projections.constructor(TodoSearchResponse.class,
				todo.title,
				todo.user.nickName,
				manager.id.countDistinct().intValue(),
				comment.id.countDistinct().intValue(),
				todo.createdAt))
			.from(todo)
			.leftJoin(todo.managers, manager)
			.leftJoin(todo.comments, comment)
			.leftJoin(todo.user, user)
			.where(builder)
			.groupBy(todo.id)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(todo.createdAt.asc())
			.fetch();

		Long countResult = queryFactory
			.select(todo.countDistinct())
			.from(todo)
			.leftJoin(todo.user, user)
			.where(builder).fetchOne();

		long total = (countResult != null) ? countResult : 0;

		// todos : 현재 페이지 데이터(pageable로 제한된 목록)
		// total : 전체 데이터 개수
		return new PageImpl<>(list, pageable, total);
	}
}
