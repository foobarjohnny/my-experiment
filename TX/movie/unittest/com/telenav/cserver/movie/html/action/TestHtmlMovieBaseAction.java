/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.movie.html.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.powermock.reflect.Whitebox;

import junit.framework.TestCase;

/**
 * TestHtmlMovieBaseAction.java
 * @TODO
 * @author  xljiang@telenav.cn
 * @version 1.0 2011-9-27
 */

public class TestHtmlMovieBaseAction extends TestCase{
	
	public void testGetCliApplicationID(){
		TestedAction action = Whitebox.newInstance(TestedAction.class);
		
		assertEquals("bs_movie",action.getCliApplicationID());
	}
	
	class TestedAction extends HtmlMovieBaseAction{

		@Override
		protected ActionForward doAction(ActionMapping arg0,
				HttpServletRequest arg1, HttpServletResponse arg2)
				throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
