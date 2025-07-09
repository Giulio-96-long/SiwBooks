package com.example.demo.serviceImpl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Credentials;
import com.example.demo.entity.ErrorLog;
import com.example.demo.repository.ErrorLogRepository;
import com.example.demo.serviceImpl.service.CredentialsService;
import com.example.demo.serviceImpl.service.ErrorLogService;

@Service
public class ErrorLogServiceImpl implements ErrorLogService {

	@Autowired
	public ErrorLogRepository errorLogRepo;
	@Autowired
	private CredentialsService credentialsService;

	@Override
	public void saveErrorLog(String endpoint, Exception e) {
		ErrorLog log = new ErrorLog();
		log.setEndpoint(endpoint);
		log.setMessage(e.getMessage());
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		log.setStackTrace(sw.toString());
		log.setOccurredAt(LocalDateTime.now());
		Credentials me = credentialsService.getCurrentCredentials();
		if (me != null && me.getUser() != null) {
			log.setUser(me.getUser());
		} else {
			log.setUser(null);
		}
		errorLogRepo.save(log);
	}

}
