package com.ziplly.mobile.imagegallery;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by susnata on 4/28/15.
 */
public class RowImageLayout3 extends BaseRowLayout {

    public RowImageLayout3(Context context, List<ImageDetail> imageDetailList) {
        super(context, imageDetailList);
    }

    @Override
    public RowLayout getRowLayout() {
        return RowLayout.TYPE3;
    }

    @Override
    public void draw(View view) {
        List<ImageDetail> images = new ArrayList<>(imageDetailList);
        Preconditions.checkArgument(images.size() == RowLayoutAdapter.IMAGE_GROUPING_SIZE);

        ImageView imageView1 = (ImageView) view.findViewById(R.id.image1);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.image2);
        ImageView imageView3 = (ImageView) view.findViewById(R.id.image3);
        ImageView imageView4 = (ImageView) view.findViewById(R.id.image4);

        ImageDetail image1 = images.get(0); //getImageWithLowestAspectRatio(images);
        ImageDetail image2 = images.remove(0);
        ImageDetail image3 = images.remove(0);
        ImageDetail image4 = images.remove(0);

        double a1 = ImageUtil.aspectRatio(image1);
        double a2 = ImageUtil.aspectRatio(image2);
        double a3 = ImageUtil.aspectRatio(image3);
        double a4 = ImageUtil.aspectRatio(image4);

        double h1 = windowWidth/a1;
        double w1 = windowWidth;

        double a5 = ImageUtil.aspectRatioForHorizontalImages(image2, image3);
        double h2 = windowWidth/a5;
        double w2 = a2 * h2;
        double w3 = a3 * h2;
        double h4 = windowWidth/a4;

        layoutImage(image1, imageView1, w1, h1);
        layoutImage(image2, imageView2, w2, h2);
        layoutImage(image3, imageView3, w3, h2);
        layoutImage(image4, imageView4, w1, h4);
    }
}
