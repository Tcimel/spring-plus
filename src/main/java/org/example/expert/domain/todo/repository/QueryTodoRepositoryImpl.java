package org.example.expert.domain.todo.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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

		if(StringUtils.hasText(weather)){
			conditions.add("t.weather = :weather");
		}
		if(StringUtils.hasText(start)){
			conditions.add("t.modifiedAt >= :start");
		}
		if(StringUtils.hasText(end)){
			conditions.add("t.modifiedAt <= :end");
		}
		if(!conditions.isEmpty()){
			jpql += " WHERE " + String.join(" AND ", conditions);
		}

		jpql += "ORDER BY t.modifiedAt DESC";
		TypedQuery<Todo> query = entityManager.createQuery(jpql, Todo.class);

		if(StringUtils.hasText(weather)){
			query.setParameter("weather", weather);
		}
		if(StringUtils.hasText(start)){
			query.setParameter("start", LocalDateTime.parse(start));
		}
		if(StringUtils.hasText(end)){
			query.setParameter("end", LocalDateTime.parse(end));
		}

		query.setFirstResult((int)pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());

		List<Todo> todos = query.getResultList();

		String countJpql = "SELECT COUNT(t) FROM Todo t";
		if(!conditions.isEmpty()){
			countJpql += " WHERE " + String.join(" AND ", conditions);
		}

		TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
		if(StringUtils.hasText(weather)){
			countQuery.setParameter("weather", weather);
		}
		if(StringUtils.hasText(start)){
			countQuery.setParameter("start", LocalDateTime.parse(start));
		}
		if(StringUtils.hasText(end)){
			countQuery.setParameter("end", LocalDateTime.parse(end));
		}

		long total = countQuery.getSingleResult();

		// todos : 현재 페이지 데이터(pageable로 제한된 목록)
		// total : 전체 데이터 개수
		return new PageImpl<>(todos, pageable, total);
	}
}
