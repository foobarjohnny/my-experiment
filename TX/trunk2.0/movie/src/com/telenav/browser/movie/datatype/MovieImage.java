/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Nov 26, 2008
 * File name: movieImage.java
 * Package name: com.telenav.j2me.browser.datatype
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 2:27:42 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.datatype;

/**
 * @author dysong (dysong@telenav.cn) 2:27:42 PM, Nov 26, 2008
 */
public class MovieImage {

    /** Fields. */
    private String key;
    private byte[] data;
    private int height;
    private int width;
    private boolean big;

    /**
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param data
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("MovieImage================START\n");

        buffer.append("key: " + getKey() + "\n");
        buffer.append("height: " + getHeight() + "\n");
        buffer.append("width: " + getWidth() + "\n");
        buffer.append("data: ");
        if (data == null || data.length < 1) {
            buffer.append("");
        } else {
            for (int i = 0; i < data.length; i++) {
                buffer.append(data[i]);
            }
        }
        buffer.append("\n");
        buffer.append("MovieImage================OVER\n");

        return buffer.toString();

    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the big
     */
    public boolean isBig() {
        return big;
    }

    /**
     * @param big the big to set
     */
    public void setBig(boolean big) {
        this.big = big;
    }

}
