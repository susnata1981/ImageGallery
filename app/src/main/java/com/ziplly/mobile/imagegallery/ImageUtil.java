package com.ziplly.mobile.imagegallery;

import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.List;

/**
 * Created by susnata on 4/19/15.
 */
public class ImageUtil {
    public static boolean isPortrait(ImageDetail imageDetail) {
        return imageDetail.getHeight() > imageDetail.getWidth();
    }

    public static boolean isLandscape(ImageDetail imageDetail) {
        return imageDetail.getWidth() > imageDetail.getHeight();
    }

    public static double aspectRatio(ImageDetail imageDetail) {
        // HACK HACK HACK
        if (imageDetail.getHeight() == 0) {
            return .1;
        }

        return (double) imageDetail.getWidth()/(double) imageDetail.getHeight();
    }

    public static ImageDetail getImageWithHigestAspectRatio(List<ImageDetail> images) {
        Log.d("APP", "total images = "+images.size());
        ImageDetail result = null;
        double ar = 0;
        for (ImageDetail imageDetail : images) {
            Log.d("APP", "image data = "+ imageDetail);
            double temp = aspectRatio(imageDetail);
            if (temp > ar) {
                ar = temp;
                result = imageDetail;
            }
        }
        return result;
    }

    public static ImageDetail getImageWithLowestAspectRatio(List<ImageDetail> images) {
        ImageDetail result = null;
        double ar = Integer.MAX_VALUE;
        for (ImageDetail imageDetail : images) {
            double temp = aspectRatio(imageDetail);
            if (temp < ar) {
                ar = temp;
                result = imageDetail;
            }
        }
        return result;
    }

    public static double aspectRatioForVerticalImages(ImageDetail image1, ImageDetail image2) {
        double a1 = ImageUtil.aspectRatio(image1);
        double a2 = ImageUtil.aspectRatio(image2);
        return 1/((1/a1)+(1/a2));
    }

    public static double aspectRatioForVerticalImages(double a1, double a2) {
        return 1/((1/a1)+(1/a2));
    }

    public static double aspectRatioForVerticalImages(
            ImageDetail image1,
            ImageDetail image2,
            ImageDetail image3) {

        double a1 = ImageUtil.aspectRatio(image1);
        double a2 = ImageUtil.aspectRatio(image2);
        double a3 = ImageUtil.aspectRatio(image3);
        double a4 = aspectRatioForVerticalImages(image1, image2);
        return 1/((1/a4)+(1/a3));
    }

    public static double aspectRatioForHorizontalImages(
            ImageDetail image1,
            ImageDetail image2,
            ImageDetail image3) {

        double a3 = ImageUtil.aspectRatio(image3);
        double a4 = aspectRatioForHorizontalImages(image1, image2);
        return a3 + a4;
    }

    public static double aspectRatioForHorizontalImages(ImageDetail image1, ImageDetail image2) {
        double a1 = ImageUtil.aspectRatio(image1);
        double a2 = ImageUtil.aspectRatio(image2);
        return a1 + a2;
    }
}
