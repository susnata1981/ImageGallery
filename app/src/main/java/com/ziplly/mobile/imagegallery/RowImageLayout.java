package com.ziplly.mobile.imagegallery;

import android.view.View;

import java.util.List;

/**
 * Created by susnata on 4/20/15.
 */
public interface RowImageLayout {

    public RowLayout getRowLayout();

    public void draw(View view);

    boolean onBackPresesed();

    void setCurrentPosition(int position);
}
