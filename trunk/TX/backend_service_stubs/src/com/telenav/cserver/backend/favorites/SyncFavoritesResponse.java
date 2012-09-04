/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.favorites;

import com.telenav.cserver.backend.datatypes.favorites.Favorite;
import com.telenav.cserver.backend.datatypes.favorites.FavoriteCategory;
import com.telenav.cserver.backend.datatypes.favorites.FavoriteMapping;

/**
 * 
 * @author zhjdou
 * 2010-2-21
 */
public class SyncFavoritesResponse extends FavoriteBasicResponse{
	private Favorite[] addFavorites;
	private Favorite[] deleteFavorites;
	private FavoriteCategory[] addCategory;
	private FavoriteCategory[] deleteCategory; 
	private FavoriteMapping[] mapping;
	
	/**
	 * @return the addFavorites
	 */
	public Favorite[] getAddFavorites() {
		return addFavorites;
	}
	/**
	 * @return the deleteFavorites
	 */
	public Favorite[] getDeleteFavorites() {
		return deleteFavorites;
	}
	/**
	 * @return the addCategory
	 */
	public FavoriteCategory[] getAddCategory() {
		return addCategory;
	}
	/**
	 * @return the deleteCategory
	 */
	public FavoriteCategory[] getDeleteCategory() {
		return deleteCategory;
	}
	/**
	 * @return the mapping
	 */
	public FavoriteMapping[] getMapping() {
		return mapping;
	}
	
	/**
	 * @param addFavorites the addFavorites to set
	 */
	public void setAddFavorites(Favorite[] addFavorites) {
		this.addFavorites = addFavorites;
	}
	/**
	 * @param deleteFavorites the deleteFavorites to set
	 */
	public void setDeleteFavorites(Favorite[] deleteFavorites) {
		this.deleteFavorites = deleteFavorites;
	}
	/**
	 * @param addCategory the addCategory to set
	 */
	public void setAddCategory(FavoriteCategory[] addCategory) {
		this.addCategory = addCategory;
	}
	/**
	 * @param deleteCategory the deleteCategory to set
	 */
	public void setDeleteCategory(FavoriteCategory[] deleteCategory) {
		this.deleteCategory = deleteCategory;
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
		sb.append("SyncFavoritesResponse: ");
		sb.append("&statusCode=").append(this.getStatus_code());
		sb.append("&statusMsg=").append(this.getStatus_message());
		sb.append("addFav=");
		if(this.addFavorites!=null) {
		   sb.append(this.addFavorites.length);
		}
		sb.append("&deleteFav=");
		if(this.deleteFavorites!=null) {
		   sb.append(this.deleteFavorites.length);
		}
		sb.append("addCate=");
		if(this.addCategory!=null) {
		   sb.append(this.addCategory.length);
		}
		sb.append("&delCate=");
		if(this.deleteCategory!=null) {
		   sb.append(this.deleteCategory.length);
		}
		return sb.toString();
    }
}
