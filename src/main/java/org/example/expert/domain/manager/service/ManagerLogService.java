package org.example.expert.domain.manager.service;

import org.example.expert.domain.manager.entity.ManagerLog;
import org.example.expert.domain.manager.repository.ManagerLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManagerLogService {

	private final ManagerLogRepository managerLogRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void writeLog(Long managerId, String message){
		try{
			ManagerLog managerLog = new ManagerLog(managerId,message);
			managerLogRepository.save(managerLog);
		}catch (Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}
}
