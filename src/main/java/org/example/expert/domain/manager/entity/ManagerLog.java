package org.example.expert.domain.manager.entity;

import org.example.expert.domain.common.entity.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name = "manager_log")
@NoArgsConstructor
public class ManagerLog extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long managerId;

	@Column(nullable = false)
	private String message;

	public ManagerLog(Long managerId, String message) {
		this.managerId = managerId;
		this.message = message;
	}
}
