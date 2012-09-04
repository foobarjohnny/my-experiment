/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.favoritesV40;

/**
 * @author zhjdou 2010-2-20
 */
public class FavoriteMapping {
	private Favorite favorite;
	private FavoriteCategory[] categories;

	/**
	 * @return the favorite
	 */
	public Favorite getFavorite() {
		return favorite;
	}

	/**
	 * @return the categories
	 */
	public FavoriteCategory[] getCategories() {
		return categories;
	}

	/**
	 * @param favorite
	 *            the favorite to set
	 */
	public void setFavorite(Favorite favorite) {
		this.favorite = favorite;
	}

	/**
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(FavoriteCategory[] categories) {
		this.categories = categories;
	}

}
