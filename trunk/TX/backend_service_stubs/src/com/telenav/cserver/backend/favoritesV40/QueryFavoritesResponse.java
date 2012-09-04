/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.favoritesV40;

import com.telenav.cserver.backend.datatypes.favoritesV40.Favorite;
import com.telenav.cserver.backend.datatypes.favoritesV40.FavoriteCategory;
import com.telenav.cserver.backend.datatypes.favoritesV40.FavoriteMapping;

/**
 * 
 * @author zhjdou
 * 2010-2-20
 */
public class QueryFavoritesResponse extends FavoriteBasicResponse{
    private Favorite[] favorites;
    private FavoriteCategory[] categories;
    private FavoriteMapping[] mapping;
    
	/**
	 * @return the favorites
	 */
	public Favorite[] getFavorites() {
		return favorites;
	}
	/**
	 * @return the categories
	 */
	public FavoriteCategory[] getCategories() {
		return categories;
	}
	/**
	 * @return the mapping
	 */
	public FavoriteMapping[] getMapping() {
		return mapping;
	}
	/**
	 * @param favorites the favorites to set
	 */
	public void setFavorites(Favorite[] favorites) {
		this.favorites = favorites;
	}
	/**
	 * @param categories the categories to set
	 */
	public void setCategories(FavoriteCategory[] categories) {
		this.categories = categories;
	}
	/**
	 * @param mapping the mapping to set
	 */
	public void setMapping(FavoriteMapping[] mapping) {
		this.mapping = mapping;
	}
    
	/**
	 * For record log
	 */
    public String toString(){
    	StringBuffer sb=new StringBuffer();
		sb.append("QueryFavoritesResponse: ");
		sb.append("&statusCode=").append(this.getStatus_code());
		sb.append("&statusMsg=").append(this.getStatus_message());
		sb.append("&favorites_lens=");
		if(this.favorites!=null){
			sb.append(this.favorites.length);
		}
		sb.append("&category_lens=");
		if(this.categories!=null){
			sb.append(this.categories.length);
		}
		return sb.toString();
    }
}
