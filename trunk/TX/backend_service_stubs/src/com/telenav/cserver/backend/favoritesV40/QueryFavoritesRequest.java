/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.favoritesV40;

/**
 * 
 * @author zhjdou
 * 2010-2-20
 */
public class QueryFavoritesRequest extends FavoriteBasicRequest{    
	/**
	 * for log
	 */
	public String toString() {
		StringBuffer sb=new StringBuffer();
		sb.append("QueryFavoritesRequest: ");
		sb.append("userId=");
		sb.append(this.getUserId());
		return sb.toString();
	}
}
