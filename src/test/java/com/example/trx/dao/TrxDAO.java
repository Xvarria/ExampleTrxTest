package com.example.trx.dao;

import com.example.trx.model.TableData;

public interface TrxDAO {

	public void insertIntoTableA (String uid, String message, boolean fail) throws Exception;

	public void insertIntoTableB (String uid, String message, boolean fail) throws Exception;

	
	public TableData getTableA (String uid) throws Exception;

	public TableData getTableB (String uid) throws Exception;	
}
