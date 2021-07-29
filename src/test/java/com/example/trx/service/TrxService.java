package com.example.trx.service;

import com.example.trx.model.TableData;

public interface TrxService {

	public void createRecordNotFail(String uid, String message) throws Exception;

	public void createRecordFailOnA(String uid, String message) throws Exception;
	
	public void createRecordFailOnB(String uid, String message) throws Exception;
	
	public void createRecordFailAll(String uid, String message) throws Exception;
	
	public TableData getTableA (String uid) throws Exception;

	public TableData getTableB (String uid) throws Exception;
	
}
