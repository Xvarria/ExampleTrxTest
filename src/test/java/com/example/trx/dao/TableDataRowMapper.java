package com.example.trx.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.trx.model.TableData;

public class TableDataRowMapper implements RowMapper<TableData>{
	
	private static final String UID_VALUE = "UID_VALUE";
	private static final String MESSAGE = "MESSAGE";
	private static final String ENTITY = "ENTITY";

	/**
	 * Constructs and returns a CparHeader object from the values in the ResultSet 
	 * for the specified rowNum.
	 * 
	 * @param resultSet The ResultSet object containing the query results.
	 * @param rowNum The rowNum to be retrieved.
	 * 
	 * @return A CpearHeader object with the values from the ResultSet for the current row.
	 */
	@Override
	public TableData mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		TableData retObject = new TableData();
		
		retObject.setUid(resultSet.getString(UID_VALUE));
		retObject.setEntityName(resultSet.getString(ENTITY));
		retObject.setData(resultSet.getString(MESSAGE));
		
		return retObject;
	}
}
