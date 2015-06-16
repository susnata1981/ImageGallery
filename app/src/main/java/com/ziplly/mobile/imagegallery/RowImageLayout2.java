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
public class RowImageLayout2 extends BaseRowLayout {

    public RowImageLayout2(Context context, List<ImageDetail> imageDetailList) {
        super(context, imageDetailList);
    }

    @Override
    public RowLayout getRowLayout() {
        return RowLayout.TYPE2;
    }

//    @Override
//    public void draw(View view) {
//        List<ImageDetail> images = new ArrayList<>(imageDetailList);
//        Preconditions.checkArgument(images.size() == RowLayoutAdapter.IMAGE_GROUPING_SIZE);
//
//        ImageView imageView1 = (ImageView) view.findViewById(R.id.image1);
//        ImageView imageView2 = (ImageView) view.findViewById(R.id.image2);
//        ImageView imageView3 = (ImageView) view.findViewById(R.id.image3);
//        ImageView imageView4 = (ImageView) view.findViewById(R.id.image4);
//
//        ImageDetail image1 = getImageWithHigestAspectRatio(images);
//        double aspectRatio = getAspectRatio(image1);
//        double width1 = windowWidth;
//        double height1 = width1 / aspectRatio;
//        layoutImage(image1, imageView1, width1, height1);
//
//        ImageDetail image2 = getNextImage(images);
//        aspectRatio = getAspectRatio(image2);
//        double width2 = windowWidth / 3;
//        double height2 = width2 / aspectRatio;
//
//        layoutImage(image2, imageView2, width2, height2);
//        layoutImage(getNextImage(images), imageView3, width2, height2);
//        layoutImage(getNextImage(images), imageView4, width2, height2);
//    }

    @Override
    public void draw(View view) {
        List<ImageDetail> images = new ArrayList<>(imageDetailList);
        Preconditions.checkArgument(images.size() == RowLayoutAdapter.IMAGE_GROUPING_SIZE);

        ImageView imageView1 = (ImageView) view.findViewById(R.id.image1);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.image2);
        ImageView imageView3 = (ImageView) view.findViewById(R.id.image3);
        ImageView imageView4 = (ImageView) view.findViewById(R.id.image4);

        ImageDetail image1 = images.get(0); // getImageWithLowestAspectRatio(images);
        ImageDetail image2 = images.remove(0);
        ImageDetail image3 = images.remove(0);
        ImageDetail image4 = images.remove(0);
        double a1 = ImageUtil.aspectRatio(image1);
        double a2 = ImageUtil.aspectRatio(image2);
        double a3 = ImageUtil.aspectRatio(image3);
        double a4 = ImageUtil.aspectRatio(image4);

        double a5 = ImageUtil.aspectRatioForHorizontalImages(image2, image3, image4);
        double a6 = ImageUtil.aspectRatioForVerticalImages(a1, a5);

        double w1 = windowWidth;
        double h1 = w1/a1;

        double h2 = (w1/a6 - h1);
        double w2 = a2 * h2;
        double w3 = a3 * h2;
        double w4 = a4 * h2;

        layoutImage(image1, imageView1, w1, h1);
        layoutImage(image2, imageView2, w2, h2);
        layoutImage(image3, imageView3, w3, h2);
        layoutImage(image4, imageView4, w4, h2);
    }
}
