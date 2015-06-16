package com.ziplly.mobile.imagegallery;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by susnata on 4/29/15.
 */
public class RowLayoutAdapter {
    private static final String TAG = RowLayoutAdapter.class.getCanonicalName();
    public static final int IMAGE_GROUPING_SIZE = 4;
    private final Context context;
    private RowImageLayout lastRowImageLayout;

    public RowLayoutAdapter(Context context) {
        this.context = context;
    }

    public List<RowImageLayout> convert(List<ImageDetail> images) {
        List<RowImageLayout> result = new ArrayList<>();
        int totalImages = images.size();
        int i = 0;
        int index = 0;

        while (i < totalImages) {
            List<ImageDetail> rowImageDetail = new ArrayList<>();
            for (int k = 0; k < IMAGE_GROUPING_SIZE; k++) {
                if (k + i == totalImages) {
//                    result.add(new RowImageLayout3(context, rowImageData));
                    return result;
                }

                rowImageDetail.add(images.get(k + i));
            }

            RowImageLayout rowImageLayout = createRowLayout(index++, rowImageDetail);
            lastRowImageLayout = rowImageLayout;
            result.add(rowImageLayout);
            i += IMAGE_GROUPING_SIZE;
        }

        return result;
    }

    private RowImageLayout createRowLayout(int index, List<ImageDetail> rowImageDetail) {
//        ImageDetail imageDetail = ImageUtil.getImageWithLowestAspectRatio(rowImageDetail);
//        if (ImageUtil.aspectRatio(imageDetail) <= .8) {
//            return new RowImageLayout1(context, rowImageDetail);
//        }
//
//        imageDetail = ImageUtil.getImageWithHigestAspectRatio(rowImageDetail);
//        if (ImageUtil.aspectRatio(imageDetail) >= 1.2 &&
//                lastRowImageLayout != null && lastRowImageLayout.getRowLayout() != RowLayout.TYPE2) {
//            return new RowImageLayout2(context, rowImageDetail);
//        }
//
//        if (ImageUtil.aspectRatio(imageDetail) >= 1.4 &&
//                lastRowImageLayout != null && lastRowImageLayout.getRowLayout() != RowLayout.TYPE3) {
//            return new RowImageLayout3(context, rowImageDetail);
//        }

        return new RowImageLayout1(context, rowImageDetail);
    }
}
