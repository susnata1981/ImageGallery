package com.ziplly.mobile.imagegallery;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by susnata on 4/28/15.
 */
public class RowImageLayout0 extends BaseRowLayout {

    private static final String TAG = RowImageLayout0.class.getCanonicalName();

    public RowImageLayout0(Context context, List<ImageDetail> imageDetailList) {
        super(context, imageDetailList);
    }

    @Override
    public RowLayout getRowLayout() {
        return RowLayout.TYPE0;
    }

    @Override
    public void draw(View view) {
        List<ImageDetail> images = new ArrayList<>(imageDetailList);
        Preconditions.checkArgument(images.size() == RowLayoutAdapter.IMAGE_GROUPING_SIZE,
                "Invalid image count ="+images.size());

        ImageView imageView1 = (ImageView) view.findViewById(R.id.image1);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.image2);
        ImageView imageView3 = (ImageView) view.findViewById(R.id.image3);
        ImageView imageView4 = (ImageView) view.findViewById(R.id.image4);
        ImageDetail image1 = images.remove(0);
        ImageDetail image2 = images.remove(0);
        ImageDetail image3 = images.remove(0);
        ImageDetail image4 = images.remove(0);
        double a1 = ImageUtil.aspectRatio(image1);

        double a5 = ImageUtil.aspectRatioForHorizontalImages(image1, image2);
        double h1 = a5 * windowWidth;

        double w1 = a1 * h1;
        double w2 = windowWidth - w1;

        double a6 = ImageUtil.aspectRatioForHorizontalImages(image3, image4);
        double h3 = a6 * windowWidth;
        double w3 = a1 * h3;
        double w4 = windowWidth - w3;

        layoutImage(image1, imageView1, w1, h1);
        layoutImage(image2, imageView2, w2, h1);
        layoutImage(image3, imageView3, w3, h3);
        layoutImage(image4, imageView4, w4, h3);
    }
}
