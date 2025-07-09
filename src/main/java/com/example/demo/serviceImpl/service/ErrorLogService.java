package com.example.demo.serviceImpl.service;

public interface ErrorLogService {

	void saveErrorLog(String endpoint, Exception e);

}
