package com.ziplly.mobile.imagegallery;

/**
 * Created by susnata on 4/20/15.
 */
public class ImageDimension {
    private long height;
    private long width;

    public ImageDimension(long height, long width) {
        this.height = height;
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public long getWidth() {
        return width;
    }
}
