package com.example.trx.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.trx.model.TableData;
import com.example.trx.service.TrxService;



/**
 * 
 * @author mchavarria
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/no_jpa_jta-applicationContext.xml" })
public class TrxNoJpaTest {
	
	//private static Log log = LogFactory.getLog(TrxControllerTest.class);

	@Autowired
	@Qualifier("trxNoJpaServiceImpl")
	private TrxService trxService;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS"); 

	@Test
	public void nonJpaTrxFailBothTest() throws Exception {
		Date timestamp = Calendar.getInstance().getTime();
		
		System.out.println("*** Non-JPA Fail on both ***");
		String uid = dateFormat.format(timestamp);
		
		try {
			this.trxService.createRecordFailAll(uid, "FailBoth");
		} catch (Exception e) {
			System.err.println("*Error on service layer -> rollback");
		}
		TableData dataInA = this.trxService.getTableA(uid);
		System.out.println("Data in A should be null. Result " + (dataInA == null ? "<null>" : dataInA.toString()));
		TableData dataInB = this.trxService.getTableB(uid);
		System.out.println("Data in B should be null. Result " + (dataInB == null ? "<null>" : dataInB.toString()));
		
		System.out.println("***  ***");
		if (dataInA != null) {
			Assert.fail();
		}

		if (dataInB != null) {
			Assert.fail();
		}	
	}

	@Test
	public void nonJpaTrxFailATest() throws Exception {
		Date timestamp = Calendar.getInstance().getTime();
		
		System.out.println("*** Non-JPA Fail on A ***");
		String uid = dateFormat.format(timestamp);
		
		try {
			this.trxService.createRecordFailOnA(uid, "FailOnA");
		} catch (Exception e) {
			System.err.println("*Error on service layer -> rollback");
		}
		TableData dataInA = this.trxService.getTableA(uid);
		System.out.println("Data in A should be null. Result " + (dataInA == null ? "<null>" : dataInA.toString()));
		TableData dataInB = this.trxService.getTableB(uid);
		System.out.println("Data in B should be null. Result " + (dataInB == null ? "<null>" : dataInB.toString()));
		System.out.println("***  ***");
		
		if (dataInA != null) {
			Assert.fail();
		}

		if (dataInB != null) {
			Assert.fail();
		}	
	}
	
	@Test
	public void nonJpaTrxFailBTest() throws Exception {
		Date timestamp = Calendar.getInstance().getTime();
		
		System.out.println("*** Non-JPA Fail on B ***");
		String uid = dateFormat.format(timestamp);
		
		try {
			this.trxService.createRecordFailOnB(uid, "FailOnB");
		} catch (Exception e) {
			System.err.println("*Error on service layer -> rollback");
		}
		TableData dataInA = this.trxService.getTableA(uid);
		System.out.println("Data in A should be null. Result " + (dataInA == null ? "<null>" : dataInA.toString()));
		TableData dataInB = this.trxService.getTableB(uid);
		System.out.println("Data in B should be null. Result " + (dataInB == null ? "<null>" : dataInB.toString()));
		System.out.println("***  ***");
		
		if (dataInA != null) {
			Assert.fail();
		}

		if (dataInB != null) {
			Assert.fail();
		}	
	}
	
	@Test
	public void nonJpaTrxNoFailTest() throws Exception {
		Date timestamp = Calendar.getInstance().getTime();
		
		System.out.println("*** Non-JPA Sucess on both ***");
		String uid = dateFormat.format(timestamp);
		
		try {
			this.trxService.createRecordNotFail(uid, "SucessBoth");
		} catch (Exception e) {
			System.err.println("*Error on service layer -> rollback");
		}
		TableData dataInA = this.trxService.getTableA(uid);
		System.out.println("Data in A should be NOT null. Result " + (dataInA == null ? "<null>" : dataInA.toString()));
		TableData dataInB = this.trxService.getTableB(uid);
		System.out.println("Data in B should be NOT null. Result " + (dataInB == null ? "<null>" : dataInB.toString()));
		
		System.out.println("***  ***");
		if (dataInA == null) {
			Assert.fail();
		}

		if (dataInB == null) {
			Assert.fail();
		}	
	}
}
