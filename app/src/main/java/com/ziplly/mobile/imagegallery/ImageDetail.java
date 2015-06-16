package com.ziplly.mobile.imagegallery;

import android.net.Uri;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by susnata on 4/19/15.
 */
public class ImageDetail implements Serializable {
    private long id;
    private int position;
    private String thumbnailUri;
    private String path;
    private long width;
    private long height;
    private int orientation;
    private Date dateAdded;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbnailUri() {
        return thumbnailUri;
    }

    public void setThumbnailUri(String thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = new Date(dateAdded);
    }

    @Override
    public String toString() {
        return "ImageData{" +
                "path='" + path + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageDetail imageDetail = (ImageDetail) o;

        if (height != imageDetail.height) return false;
        if (width != imageDetail.width) return false;
        if (!path.equals(imageDetail.path)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = path.hashCode();
        result = 31 * result + (int) (width ^ (width >>> 32));
        result = 31 * result + (int) (height ^ (height >>> 32));
        return result;
    }
}
