package com.example.trx.non_jpa.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.trx.dao.TrxDAO;
import com.example.trx.model.TableData;
import com.example.trx.service.TrxService;

@Service
public class TrxNoJpaServiceImpl implements TrxService {

	@Autowired
	@Qualifier("trxNoJpaDAOImpl")
	private TrxDAO trxNoJpaDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void createRecordNotFail(String uid, String message) throws Exception {
		try {
			System.out.println("*Service insert into A");
			this.trxNoJpaDAO.insertIntoTableA(uid, message, false);

			System.out.println("*Service insert into B");
			this.trxNoJpaDAO.insertIntoTableB(uid, message, false);
		} catch (Exception e) {
			System.err.println("*Errors on service layer -> rollback");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void createRecordFailOnA(String uid, String message) throws Exception {
		try {
			System.out.println("*Service insert into A");
			this.trxNoJpaDAO.insertIntoTableA(uid, message, true);

			System.out.println("*Service insert into B");
			this.trxNoJpaDAO.insertIntoTableB(uid, message, false);
		} catch (Exception e) {
			System.err.println("*Errors on DAO layer -> rollback");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void createRecordFailOnB(String uid, String message) throws Exception {
		try {
			System.out.println("*Service insert into A");
			this.trxNoJpaDAO.insertIntoTableA(uid, message, false);

			System.out.println("*Service insert into B");
			this.trxNoJpaDAO.insertIntoTableB(uid, message, true);
		} catch (Exception e) {
			System.err.println("*Errors on DAO layer -> rollback");
		}
	}

	/**
	 * @see com.example.trx.service.TrxService#createRecordFailAll(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void createRecordFailAll(String uid, String message) throws Exception {
		try {
			System.out.println("*Service insert into A");
			this.trxNoJpaDAO.insertIntoTableA(uid, message, true);

			System.out.println("*Service insert into B");
			this.trxNoJpaDAO.insertIntoTableB(uid, message, true);
		} catch (Exception e) {
			System.err.println("*Errors on DAO layer -> rollback");
		}
	}

	@Override
	public TableData getTableA(String uid) throws Exception {
		return this.trxNoJpaDAO.getTableA(uid);
	}

	@Override
	public TableData getTableB(String uid) throws Exception {
		return this.trxNoJpaDAO.getTableB(uid);
	}
}
