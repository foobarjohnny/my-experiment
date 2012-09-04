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
 * @author zhjdou 2010-2-21
 */
public class SyncFavoritesRequest extends FavoriteBasicRequest{
	private boolean needPoi;
	private Favorite[] addFavorites;
	private Favorite[] updateFavorites;
	private Favorite[] deleteFavorites;
	private FavoriteCategory[] addCategory;
	private FavoriteCategory[] updateCategory;
	private FavoriteCategory[] deleteCategory;
	private FavoriteMapping[] mapping;

	/**
	 * @return the needPoi
	 */
	public boolean isNeedPoi() {
		return needPoi;
	}

	/**
	 * @param needPoi the needPoi to set
	 */
	public void setNeedPoi(boolean needPoi) {
		this.needPoi = needPoi;
	}

	/**
	 * @return the mapping
	 */
	public FavoriteMapping[] getMapping() {
		return mapping;
	}

	/**
	 * @param mapping the mapping to set
	 */
	public void setMapping(FavoriteMapping[] mapping) {
		this.mapping = mapping;
	}

	/**
	 * @return the addFavorites
	 */
	public Favorite[] getAddFavorites() {
		return addFavorites;
	}

	/**
	 * @return the updateFavorites
	 */
	public Favorite[] getUpdateFavorites() {
		return updateFavorites;
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
	 * @return the updateCategory
	 */
	public FavoriteCategory[] getUpdateCategory() {
		return updateCategory;
	}

	/**
	 * @return the deleteCategory
	 */
	public FavoriteCategory[] getDeleteCategory() {
		return deleteCategory;
	}

	/**
	 * @param addFavorites
	 *            the addFavorites to set
	 */
	public void setAddFavorites(Favorite[] addFavorites) {
		this.addFavorites = addFavorites;
	}

	/**
	 * @param updateFavorites
	 *            the updateFavorites to set
	 */
	public void setUpdateFavorites(Favorite[] updateFavorites) {
		this.updateFavorites = updateFavorites;
	}

	/**
	 * @param deleteFavorites
	 *            the deleteFavorites to set
	 */
	public void setDeleteFavorites(Favorite[] deleteFavorites) {
		this.deleteFavorites = deleteFavorites;
	}

	/**
	 * @param addCategory
	 *            the addCategory to set
	 */
	public void setAddCategory(FavoriteCategory[] addCategory) {
		this.addCategory = addCategory;
	}

	/**
	 * @param updateCategory
	 *            the updateCategory to set
	 */
	public void setUpdateCategory(FavoriteCategory[] updateCategory) {
		this.updateCategory = updateCategory;
	}

	/**
	 * @param deleteCategory
	 *            the deleteCategory to set
	 */
	public void setDeleteCategory(FavoriteCategory[] deleteCategory) {
		this.deleteCategory = deleteCategory;
	}

	/**
	 * for log
	 */
	public String toString() {
		StringBuffer sb=new StringBuffer();
		sb.append("SyncFavReq: ");
		sb.append("tnContext=");
		sb.append(this.getContextString());
		sb.append("addFav=");
		if(this.addFavorites!=null) {
		   sb.append(this.addFavorites.length);
		}
		sb.append("&updateFav=");
		if(this.updateFavorites!=null) {
		   sb.append(this.updateFavorites.length);
		}
		sb.append("&deleteFav=");
		if(this.deleteFavorites!=null) {
		   sb.append(this.deleteFavorites.length);
		}
		sb.append("addCate=");
		if(this.addCategory!=null) {
		   sb.append(this.addCategory.length);
		}
		sb.append("&updateCate=");
		if(this.updateCategory!=null) {
		   sb.append(this.updateCategory.length);
		}
		sb.append("&delCate=");
		if(this.deleteCategory!=null) {
		   sb.append(this.deleteCategory.length);
		}
		return sb.toString();
	}
}
