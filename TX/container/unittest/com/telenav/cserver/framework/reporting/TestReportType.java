/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.reporting;

import junit.framework.TestCase;

/**
 * TestReportType.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-23
 */
public class TestReportType extends TestCase{
	private ReportType reportType = new ReportType("SERVER_MIS_LOG");
	
	public void testEquals(){
		//1. this == anObject
		assertTrue(reportType.equals(reportType));
		//2. this != anObject
		//   (anObject instanceof ReportType) == false
		assertFalse(reportType.equals(new String("")));
		//3. this != anObject
		//   (anObject instanceof ReportType) == true
		//			anotherReportType.getType() != null
		//			(anotherReportType.getType().equals(this.type))==false
		ReportType anObject = new ReportType("anObject");
		assertFalse(reportType.equals(anObject));
		/*4. this != anObject
		     (anObject instanceof ReportType) == true
					anotherReportType.getType() != null
					(anotherReportType.getType().equals(this.type))==true
		*/
		anObject.setType("SERVER_MIS_LOG");
		assertTrue(reportType.equals(anObject));
		/*5. this != anObject
	     (anObject instanceof ReportType) == true
				anotherReportType.getType() == null
				this.type != null
	   */
		anObject.setType(null);
		assertFalse(reportType.equals(anObject));
		/*6. this != anObject
	     (anObject instanceof ReportType) == true
				anotherReportType.getType() == null
				this.type == null
	   */
		reportType.setType(null);
		assertTrue(reportType.equals(anObject));
	}
	
	public void testSimple(){
		reportType.toString();
	}
}
