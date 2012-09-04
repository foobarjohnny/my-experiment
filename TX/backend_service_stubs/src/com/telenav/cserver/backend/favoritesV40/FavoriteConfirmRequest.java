/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.favoritesV40;

import com.telenav.cserver.backend.datatypes.favoritesV40.Favorite;
import com.telenav.cserver.backend.datatypes.favoritesV40.FavoriteCategory;

/**
 * 
 * @author zhjdou
 * 2010-3-1
 */
public class FavoriteConfirmRequest extends FavoriteBasicRequest
{   
    private Favorite[] addFavorites;
    private Favorite[] deleteFavorites;
    private FavoriteCategory[] addCategory;
    private FavoriteCategory[] deleteCategory;

    /**
     * @return the addFavorites
     */
    public Favorite[] getAddFavorites()
    {
        return addFavorites;
    }
    /**
     * @return the deleteFavorites
     */
    public Favorite[] getDeleteFavorites()
    {
        return deleteFavorites;
    }
    /**
     * @return the addCategory
     */
    public FavoriteCategory[] getAddCategory()
    {
        return addCategory;
    }
    /**
     * @return the deleteCategory
     */
    public FavoriteCategory[] getDeleteCategory()
    {
        return deleteCategory;
    }
    /**
     * @param addFavorites the addFavorites to set
     */
    public void setAddFavorites(Favorite[] addFavorites)
    {
        this.addFavorites = addFavorites;
    }
    /**
     * @param deleteFavorites the deleteFavorites to set
     */
    public void setDeleteFavorites(Favorite[] deleteFavorites)
    {
        this.deleteFavorites = deleteFavorites;
    }
    /**
     * @param addCategory the addCategory to set
     */
    public void setAddCategory(FavoriteCategory[] addCategory)
    {
        this.addCategory = addCategory;
    }
    /**
     * @param deleteCategory the deleteCategory to set
     */
    public void setDeleteCategory(FavoriteCategory[] deleteCategory)
    {
        this.deleteCategory = deleteCategory;
    }
    
    /**
     * the string record the query request details
     */
    public String toString() {
        StringBuffer bf=new StringBuffer();
        bf.append("ConfirmRequest[");
        bf.append("UserId=");
        bf.append(this.getUserId());
        if(this.addFavorites!=null) {
            bf.append(",AddFavorite=");
            for(int i=0;i<this.addFavorites.length;i++) {
                bf.append(this.addFavorites[i].getFavoriteId()+"\n");
            }
        }
        if(this.deleteFavorites!=null) {
            bf.append(", DeleteFav=");
            for(int i=0;i<this.deleteFavorites.length;i++) {
                bf.append(this.deleteFavorites[i].getFavoriteId()+"\n");
            }
        }
        if(this.addCategory!=null) {
            bf.append(",AddCategory=");
            for(int i=0;i<this.addCategory.length;i++) {
                bf.append(this.addCategory[i].getCategoryName()+"\n");
            }
        }
        if(this.deleteCategory!=null) {
            bf.append(", DeleteCategory=");
            for(int i=0;i<this.deleteCategory.length;i++) {
                bf.append(this.deleteCategory[i].getCategoryName()+"\n");
            }
        }
        bf.append(", contextString=");
        bf.append(this.getContextString());
        bf.append("]");
        return bf.toString();
    }  
    
}
