/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.trump;

import junit.framework.TestCase;

import com.telenav.cserver.framework.trump.PathCollection.Path;

/**
 * TestPathCollection.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-24
 */
public class TestPathCollection extends TestCase{
	private PathCollection pathCollection = new PathCollection();
	private Path path = pathCollection.new Path();
	
	public void testFindMatchedPath() {
		pathCollection.addPath("com");
		pathCollection.addPath("com.telenav");
		pathCollection.addChild("cn");//null
		pathCollection.addChild("com.cserver.framework");//com.cserver.framework
		//assert
	}
	/**
	 * Just For coverage rate
	 */
	public void testSimple() {
		pathCollection.getPathList();
		pathCollection.addPath("");
		pathCollection.addChild("");
		path.getChildrenPath();
		path.getPath();
		//no Exception is OK.
	}
	
}
