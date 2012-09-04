/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import junit.framework.TestCase;

import org.powermock.reflect.Whitebox;

import com.telenav.cserver.common.resource.holder.impl.BillingConfHolder;
import com.telenav.cserver.common.resource.message.MessagesHolder;
import com.telenav.j2me.datatypes.NewBizCategory;

/**
 * TestHTMLDisplayFormat.java
 * 
 * xljiang@telenav.cn
 * 
 * @version 1.0 2011-5-12
 */
public class TestHTMLDisplayFormat extends TestCase {

	private final static String COLUMN_END = "</td>";
	private final static String COLUMN_START = "<td align=right>";
	private final static String LINE_END = "</tr>";
	private final static String LINE_START = "<tr>";
	private final static String TABLE_END = "</table>";
	private final static String TABLE_START = "<table border=1 align=center>";
	private final static String holderName = "BillingConfHolder";
	private final static String holderType = "Bill";
	private final static String holderKey = "key_bill";
	
	private BillingConfHolder bholder = new BillingConfHolder();
	
	private ResourceCacheManagement cacheManagement = ResourceCacheManagement.getInstance();
	private HTMLDisplayFormat htmlDisplayFormat = new HTMLDisplayFormat(cacheManagement);

	@Override
	protected void setUp() throws Exception {
		
	}
	private void initHolder(){
		cacheManagement.clear();
		bholder.setName(holderName);
		bholder.setType(holderType);
		cacheManagement.addHolder(bholder);
	}
	public void testAddLine() throws Exception {
		initHolder();
		StringBuffer sb = new StringBuffer();
		List<Object> obj = new ArrayList<Object>();
		obj.add("<img src='pic/1.jpg'>");
		obj.add("<font>aaa</font>");

		Whitebox.invokeMethod(htmlDisplayFormat, "addLine", sb, obj);
		assertEquals(LINE_START + COLUMN_START + "<img src='pic/1.jpg'>"
				+ COLUMN_END + COLUMN_START + "<font>aaa</font>" + COLUMN_END
				+ LINE_END, sb.toString());
	}

	public void testStatistic() {
		initHolder();
		String result = htmlDisplayFormat.statistic();
		assertEquals("<table border=1 align=center><tr><td align=right>Statistic</td></tr><tr><td align=right>Holder Type:</td><td align=right>1</td></tr><tr><td align=right>Holders:</td><td align=right>1</td></tr><tr><td align=right>Counter Of Object</td><td align=right>0</td></tr><tr><td align=right>Memory:</td><td align=right>0Byte</td></tr><tr></tr><tr><td align=right>Holder</td></tr><tr><td align=right>Holder Name</td><td align=right>Holder Type</td><td align=right>Counter Of Object</td><td align=right>Memory</td></tr><tr><td align=right>BillingConfHolder</td><td align=right>BillingConfHolder</td><td align=right>0</td><td align=right>0Byte</td><td align=right><table><form name=\"invokeBean\" action=\"invokeBean?class=com.telenav.cserver.common.resource.ResourceCacheManagement;operation=details\" method=\"POST\"/><tr><td align=right><input type=\"hidden\" name=\"holderName#java.lang.String\" value=\"BillingConfHolder\"></input><input  type=\"submit\" name=\"op_submit\" value=\"details\"></input></td></tr></form></table></td></tr></table>"
					,result);
	}
	
	public void testDetails(){
		initHolder();
		String result = htmlDisplayFormat.details();
		assertEquals("<table border=1 align=center><tr><td align=right>Statistic</td></tr><tr><td align=right>Holder Type:</td><td align=right>1</td></tr><tr><td align=right>Holders:</td><td align=right>1</td></tr><tr><td align=right>Counter Of Object</td><td align=right>0</td></tr><tr><td align=right>Memory:</td><td align=right>0Byte</td></tr><tr></tr><tr><td align=right>Holder</td></tr><tr><td align=right>Holder Name</td><td align=right>Holder Type</td><td align=right>Counter Of Object</td><td align=right>Memory</td><td align=right>Details</td></tr><tr><td align=right>BillingConfHolder</td><td align=right>BillingConfHolder</td><td align=right>0</td><td align=right>0Byte</td><td align=right><table></table></td></tr></table>"
					 ,result);
	}
	
	public void testDetailsWithHolderName(){
		initHolder();
		//found == 1
		String result = htmlDisplayFormat.details(holderName);
		assertEquals("<table border=1 align=center><tr><td align=right>Holder Name</td><td align=right>Holder Type</td><td align=right>Counter Of Object</td><td align=right>Memory</td><td align=right>Details</td></tr><tr><td align=right>BillingConfHolder</td><td align=right>BillingConfHolder</td><td align=right>0</td><td align=right>0Byte</td><td align=right><table></table></td></tr></table>"
					 ,result);
		//found == 0
		result = htmlDisplayFormat.details("_testName");
		assertEquals("THERE AREN'T ANY Holder OF THE NAME OF _testName",result);
		//found == 2 or other else
		BillingConfHolder newHolder = new BillingConfHolder();
		newHolder.setName(holderName);
		cacheManagement.addHolder(newHolder);
		
		result = htmlDisplayFormat.details(holderName);
		assertEquals("THERE ARE MORE THAN ONE HOLDER OF THE NAME OF "+holderName,result);
	}
	public void testContents(){
		//[1]holder not found
		initHolder();
		String result = htmlDisplayFormat.contents("123", null);
		//assertEquals("123 is not found!",result);
		assertEquals("can't find null = [null]!",result);
		//[2]more than one holder is found!
		BillingConfHolder newHolder = new BillingConfHolder();
		newHolder.setName(holderName);
		cacheManagement.addHolder(newHolder);
		result = htmlDisplayFormat.contents(holderName, null);
		assertEquals("more than one " + holderName + " is not found!",result);
		//[3]size of holder map is 0
		initHolder();
		result = htmlDisplayFormat.contents(holderName, "testKey");
		assertEquals(holderName + "[testKey]" + " is not found!",result);
		
		
		//================================size of holder map more than 0========================
		bholder.put(holderKey, "value of "+holderKey);
		//[4]key not found
		result = htmlDisplayFormat.contents(holderName, "testKey");
		assertEquals(holderName + "[testKey]" + " is not found!",result);
		//[5]found a key with value of String 
		result = htmlDisplayFormat.contents(holderName, holderKey);
		assertEquals(holderName + "["+holderKey+"]" + " is not found!",result);
		
		//[6]found a key with value of ResourceContent named rc
		//   [6.1]rc.getProps is null or its size = 0
		ResourceContent rc = new ResourceContent();
		bholder.put(holderKey, rc);
		//       [6.1.1] rc.getObject == null
		result = htmlDisplayFormat.contents(holderName, holderKey);
		assertEquals(holderName + "["+holderKey+"]" + " is not found!",result);
		//       [6.1.2] rc.getObject != null 
		//             [6.1.2.1]rc.getObject != null and rc.getObject instanceof NewBizCategory
		NewBizCategory newBizCategory = new NewBizCategory();
		Vector<NewBizCategory> v = new Vector<NewBizCategory>();
		v.add(new NewBizCategory());
		v.add(new NewBizCategory());
		newBizCategory.children = v;
		newBizCategory.name = "newBizCategory_name";
		rc.setObject(newBizCategory);
		result = htmlDisplayFormat.contents(holderName, holderKey);
		assertEquals("newBizCategory_name</br>&nbsp;&nbsp;&nbsp;</br>&nbsp;&nbsp;&nbsp;",result);
		//             [6.1.2.2] rc.getObject != null and NOT instanceof NewBizCategory
		rc = new ResourceContent();
		bholder.put(holderKey, rc);
		rc.setObject("A String Object.");
		result = htmlDisplayFormat.contents(holderName, holderKey);
		assertEquals("A String Object.",result);
		//   [6.2]rc.getProps !=null && its size > 0
		//       [6.2.1] rc.getProps().entrySet().iterator().next() = null;
		Map<String,String> rc_props_map = new HashMap<String,String>();
		rc_props_map.put("1","one");
		rc.setProps(rc_props_map);
		result = htmlDisplayFormat.contents(holderName, holderKey);
		assertEquals(rc_props_map.entrySet().iterator().next().toString(),result);
	}
	
	public void testAddCacheDetailLine() throws Exception{
		initHolder();
		StringBuffer sb = new StringBuffer();
		List<Object> objects = new ArrayList<Object>();
		objects.add("1_one");
		objects.add("2_two");
		List<String> innerList = new ArrayList<String>();
		innerList.add("innerList_first");
		innerList.add("innerList_second");
		innerList.add("innerList_third");
		
		objects.add(innerList);
		Whitebox.invokeMethod(htmlDisplayFormat, "addCacheDetailLine", sb,objects);
		assertEquals("<tr><td align=right>1_one</td><td align=right>2_two</td><td align=right><table><tr><td align=right>innerList_first</td></tr><tr><td align=right>innerList_second</td></tr><tr><td align=right>innerList_third</td></tr></table></td></tr>"
					 ,sb.toString());
	}
	
	public void testAddHolderInfo() throws Exception{
		initHolder();
		StringBuffer sb = new StringBuffer();
		MessagesHolder newHolder = new MessagesHolder();
		Whitebox.invokeMethod(htmlDisplayFormat,"addHolderInfo",sb, newHolder);
		assertEquals("<tr><td align=right>null</td><td align=right>MessagesHolder</td><td align=right>0</td><td align=right>0Byte</td><td align=right><table></table></td></tr>"
					,sb.toString());
	}
}
