package com.ziplly.mobile.imagegallery;

import java.util.List;

/**
 * Created by susnata on 4/19/15.
 */
public class ImageLayoutData {
    private RowLayout rowLayout;
    private List<ImageDetail> rowImageDetail;

    public ImageLayoutData(RowLayout rowLayout, List<ImageDetail> imageDetails) {
        this.rowLayout = rowLayout;
        this.rowImageDetail = imageDetails;
    }

    public RowLayout getRowLayout() {
        return rowLayout;
    }

    public List<ImageDetail> getImageData() {
        return rowImageDetail;
    }
}
