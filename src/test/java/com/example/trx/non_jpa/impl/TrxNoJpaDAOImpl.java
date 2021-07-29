package com.example.trx.non_jpa.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.trx.dao.TableDataRowMapper;
import com.example.trx.dao.TrxDAO;
import com.example.trx.model.TableData;

@Repository
public class TrxNoJpaDAOImpl implements TrxDAO {

	@Autowired
	protected NamedParameterJdbcTemplate namedJdbcTemplate;
	
	@Autowired
	protected JdbcTemplate jdbcTempate;
		
	private static final String SELECT_TABLE_A = "SELECT 'TABLE_A' AS ENTITY, UID_VALUE, MESSAGE FROM TMP_TABLE_A WHERE UID_VALUE = :UID_VALUE";
	private static final String SELECT_TABLE_B = "SELECT 'TABLE_B' AS ENTITY, UID_VALUE, MESSAGE FROM TMP_TABLE_B WHERE UID_VALUE = :UID_VALUE";
	
	private static final String INSERT_INTO_A = "INSERT INTO TMP_TABLE_A (UID_VALUE, MESSAGE) VALUES (:UID_VALUE, :MESSAGE)";
	private static final String INSERT_INTO_B = "INSERT INTO TMP_TABLE_B (UID_VALUE, MESSAGE) VALUES (:UID_VALUE, :MESSAGE)";
	
	private static final String INSERT_INTO_A_FAIL = "INSERT INTO TMP_TABLE_A_FAIL (UID_VALUE, MESSAGE) VALUES (:UID_VALUE, :MESSAGE, 1)";
	private static final String INSERT_INTO_B_FAIL = "INSERT INTO TMP_TABLE_B_FAIL (UID_VALUE, MESSAGE) VALUES (:UID_VALUE, :MESSAGE, 1)";


	//Parameter Names
	private static final String PARAMETER_UID_VALUE = "UID_VALUE";
	private static final String PARAMETER_MESSAGE = "MESSAGE";
	
	
	@PostConstruct
	public void loadDatabase() {
		executeStament("DROP TABLE TMP_TABLE_A");
		executeStament("DROP TABLE TMP_TABLE_B");
		executeStament("CREATE TABLE TMP_TABLE_A (UID_VALUE VARCHAR(20), MESSAGE VARCHAR(50), PRIMARY KEY(UID_VALUE))");
		executeStament("CREATE TABLE TMP_TABLE_B (UID_VALUE VARCHAR(20), MESSAGE VARCHAR(50), PRIMARY KEY(UID_VALUE))");
		
	}
	
	public void executeStament(String query) {
		Connection conn =  DataSourceUtils.getConnection(this.jdbcTempate.getDataSource());
		CallableStatement stmt = null;
		try {
			stmt = conn.prepareCall(query);
			stmt.execute();
		} catch (Exception e) {
			System.out.println("Error on query: " + query + " - " + e.getLocalizedMessage());
		} finally {
		
		}		
	}
	
	public void releaseAndCloseConnection(Connection conn, CallableStatement stmt) {
		DataSourceUtils.releaseConnection(conn, this.jdbcTempate.getDataSource());
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Error on closing the connection - " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * @see com.example.trx.dao.TrxDAO#insertIntoTableA(java.lang.String, java.lang.String, boolean)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertIntoTableA(String uid, String message, boolean fail) throws Exception {
		try {
			String query = fail ? INSERT_INTO_A_FAIL : INSERT_INTO_A;
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(PARAMETER_UID_VALUE, uid);
			paramMap.put(PARAMETER_MESSAGE, message);
			
			namedJdbcTemplate.update(query, paramMap);
		} catch (Exception e) {
			System.err.println("**Error on TRX insertIntoTableA");
			throw e;
		}
	}

	/**
	 * @see com.example.trx.dao.TrxDAO#insertIntoTableB(java.lang.String, java.lang.String, boolean)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertIntoTableB(String uid, String message, boolean fail) throws Exception {
		try {
			String query = fail ? INSERT_INTO_B_FAIL : INSERT_INTO_B;
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(PARAMETER_UID_VALUE, uid);
			paramMap.put(PARAMETER_MESSAGE, message);
			
			namedJdbcTemplate.update(query, paramMap);
		} catch (Exception e) {
			System.err.println("**Error on TRX insertIntoTableB");
			throw e;
		}			
	}

	/**
	 * @see com.example.trx.dao.TrxDAO#getTableA(java.lang.String)
	 */
	public TableData getTableA(String uid) throws Exception {
		String query = SELECT_TABLE_A;
		return getTableData(uid, query);
	}

	/**
	 * @see com.example.trx.dao.TrxDAO#getTableB(java.lang.String)
	 */
	public TableData getTableB(String uid) throws Exception {
		String query = SELECT_TABLE_B;
		return getTableData(uid, query);
	}
	
	private TableData getTableData(String uid, String query) {
		Map<String, Object> params = new HashMap<>();
		TableData tableData = new TableData();
	
		params.put(PARAMETER_UID_VALUE, uid);
		tableData = this.queryForObject(query, params, new TableDataRowMapper());
		return tableData;
	}

	/**
	 * Query for object, returns null if no value found.
	 * @param <T>
	 * @param sql
	 * @param paramMap
	 * @param rowMapper
	 * @return
	 * @throws DataAccessException
	 */
	private <T> T queryForObject(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) throws DataAccessException {
		T retVal = null;
		List<T> list = this.namedJdbcTemplate.query(sql, paramMap, rowMapper);
		if (list != null && !list.isEmpty()) {
			retVal = list.iterator().next();
		}
		return retVal;
	}
}
