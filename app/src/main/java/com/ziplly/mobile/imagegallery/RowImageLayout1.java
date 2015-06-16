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
public class RowImageLayout1 extends BaseRowLayout {

    private static final String TAG = RowImageLayout1.class.getCanonicalName();

    public RowImageLayout1(Context context, List<ImageDetail> imageDetailList) {
        super(context, imageDetailList);
    }

    @Override
    public RowLayout getRowLayout() {
        return RowLayout.TYPE1;
    }

    @Override
    public void draw(View view) {
        List<ImageDetail> images = new ArrayList<>(imageDetailList);
        Preconditions.checkArgument(images.size() == RowLayoutAdapter.IMAGE_GROUPING_SIZE);

        ImageView imageView1 = (ImageView) view.findViewById(R.id.image1);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.image2);
        ImageView imageView3 = (ImageView) view.findViewById(R.id.image3);
        ImageView imageView4 = (ImageView) view.findViewById(R.id.image4);

//        ImageDetail image1 = getImageWithLowestAspectRatio(images);
        ImageDetail image1 = images.remove(0);
        ImageDetail image2 = images.remove(0);
        ImageDetail image3 = images.remove(0);
        ImageDetail image4 = images.remove(0);
        double a1 = ImageUtil.aspectRatio(image1);
        double a2 = ImageUtil.aspectRatio(image2);
        double a3 = ImageUtil.aspectRatio(image3);
        double a4 = ImageUtil.aspectRatio(image4);

        double a5 = ImageUtil.aspectRatioForVerticalImages(image2, image3, image4);
        double a6 = a1 + a5;

        double h1 = windowWidth/a6;
        double w1 = h1 * a1;

        double w2 = windowWidth - w1;
        double h2 = w2/a2;
        double h3 = w2/a3;
        double h4 = w2/a4;
        layoutImage(image1, imageView1, w1, h1);
        layoutImage(image2, imageView2, w2, h2);
        layoutImage(image3, imageView3, w2, h3);
        layoutImage(image4, imageView4, w2, h4);
    }
}
