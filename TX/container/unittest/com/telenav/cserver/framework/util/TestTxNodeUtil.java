/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.util;

import junit.framework.TestCase;

import com.telenav.j2me.datatypes.TxNode;

/**
 * TestTxNodeUtil.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-25
 */
public class TestTxNodeUtil extends TestCase{
	private TxNodeUtil txNodeUtil = new TxNodeUtil();//for coverage rate
	public void testClone(){
		assertNull(TxNodeUtil.clone(null));
		
      TxNode node1 = new TxNode();
      node1.addValue(1);
      node1.addValue(2);
      node1.addValue(333);
      
      node1.addMsg("111");
      node1.addMsg("222");
      
      node1.setBinData(new byte[]{1,2});
      
      
      TxNode childNode = new TxNode();
      childNode.addValue(10001);
      childNode.addValue(10002);
      childNode.addValue(10003);
      childNode.addMsg("childNode msg");
      childNode.setBinData(new byte[]{1,2});
      node1.addChild(childNode);
      
      TxNode copy = TxNodeUtil.clone(node1);
      
      assertEquals(1,copy.childrenSize());
      assertEquals("111",copy.msgAt(0));
      assertEquals(1,copy.valueAt(0));
      assertEquals("childNode msg",copy.childAt(0).msgAt(0));
      assertEquals(10001,copy.childAt(0).valueAt(0));
	}

}
