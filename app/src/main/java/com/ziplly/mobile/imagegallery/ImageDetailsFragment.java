package com.ziplly.mobile.imagegallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by susnata on 5/5/15.
 */
public class ImageDetailsFragment extends Fragment implements ViewTreeObserver.OnGlobalFocusChangeListener {

    private static final String IMAGE_DETAIL = "IMAGE_DETAIL";
    private static final String BOUNDING_RECT = "BOUNDING_RECT";
    private static final String TAG = ImageDetailsFragment.class.getCanonicalName();
    private ImageView singleImageView;
    private ImageDetail imageDetail;
    private ImageDetailsActivity.SingleImageInfo singleImageInfo;

    public static Fragment newInstance(
            ImageDetail imageDetail,
            ImageDetailsActivity.SingleImageInfo singleImageInfo) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(IMAGE_DETAIL, imageDetail);
        bundle.putSerializable(ImageDetailsActivity.SINGLE_IMAGE_INFO, singleImageInfo);
        Fragment fragment = new ImageDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstance(ImageDetail imageDetail) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IMAGE_DETAIL, imageDetail);
        Fragment fragment = new ImageDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.singe_image_fragment, container, false);
        singleImageView = (ImageView) view.findViewById(R.id.singleImageView);
        imageDetail = (ImageDetail) getArguments().getSerializable(IMAGE_DETAIL);
        singleImageInfo = (ImageDetailsActivity.SingleImageInfo) getArguments().getSerializable(
                ImageDetailsActivity.SINGLE_IMAGE_INFO);

        if (singleImageInfo != null) {
            Log.d(TAG," Calling onCreateView for image "+singleImageInfo.imageDetail.getHeight());
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    runOnEnterAnimation();
                }
            });
        } else {
            Picasso.with(getActivity())
                    .load(new File(imageDetail.getPath()))
                    .resize((int) imageDetail.getWidth(), (int) imageDetail.getHeight())
                    .centerInside()
                    .into(singleImageView);
        }
        return view;
    }

    public void loadImage(ImageView imageView, ImageDetail imageDetail) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Log.d(TAG, "Loading image "+imageDetail.getPath());
        options.outWidth = (int) imageDetail.getWidth();
        options.outHeight = (int) imageDetail.getHeight();
        Bitmap originalBitmap = BitmapFactory.decodeFile(imageDetail.getPath(), options);
        Bitmap transformedBitmap = null;
        try {
            ExifInterface exif = new ExifInterface(imageDetail.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            transformedBitmap = Bitmap.createBitmap(
                    originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true); // rotating bitmap
        } catch (Exception e) {
        }

        imageView.clearAnimation();
        imageView.setImageBitmap(transformedBitmap);
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
    }

    private void runOnEnterAnimation() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.outHeight = getWindowSize().y;
        options.outWidth = getWindowSize().x;

        int [] vpLocation = new int[2];
//        View viewPager = getActivity().findViewById(R.id.viewPager);
//        viewPager.getLocationOnScreen(vpLocation);
//
//        Log.d(TAG, "View pager location: "+vpLocation[0]+","+vpLocation[1]);
        singleImageView.setX(0);
        singleImageView.setY(0);
        Bitmap bitmap = BitmapFactory.decodeFile(imageDetail.getPath(), options);
        singleImageView.setImageBitmap(rotateIfNecessary(bitmap));

        int [] location = new int[2];
        Point offset = new Point();
        Rect position = new Rect();
        singleImageView.getGlobalVisibleRect(position, offset);

        singleImageView.getLocationOnScreen(location);
        Log.d(TAG, "Image location: " + location[0] + "," + location[1]);
        int translateX = singleImageInfo.posX - location[0];
        int translateY = singleImageInfo.posY - location[1];

        Point size = getWindowSize();

        float finalHeight = 0;
        float finalWidth = 0;
        float ar = (float)imageDetail.getWidth()/imageDetail.getHeight();
        if (ar > 1) {
            finalWidth = size.x;
            finalHeight = finalWidth/ar;
        } else {
            finalHeight = size.y;
            finalWidth = finalHeight * ar;
        }

        float scaleX = (float)(singleImageInfo.width)/finalWidth;
        float scaleY = (float)(singleImageInfo.height)/finalHeight;

        singleImageView.setPivotX(0);
        singleImageView.setPivotY(0);
        singleImageView.setScaleX(scaleX);
        singleImageView.setScaleY(scaleY);
        singleImageView.setTranslationX(translateX);
        singleImageView.setTranslationY(translateY);

        String logStr = String.format(
                "ID=%d, Original X=%d,Y=%d, W=%d,H=%d, " +
                        "New X=%d,Y=%d, W=%d,H=%d " +
                        "Translation X=%d,Y=%d, ScaleX=%f,ScaleY=%f",
                singleImageInfo.imageDetail.getId(),
                singleImageInfo.posX, singleImageInfo.posY, singleImageInfo.width, singleImageInfo.height,
                location[0], location[1], singleImageView.getMeasuredWidth(), singleImageView.getMeasuredHeight(),
                translateX, translateY, scaleX, scaleY);

        Log.d(TAG, logStr);

        singleImageView.animate()
            .translationX(0)
            .translationY(0)
            .scaleX(1)
            .scaleY(1)
                .setStartDelay(2000)
            .setDuration(1000)
            .setInterpolator(new DecelerateInterpolator())
            .start();
    }

    private Bitmap rotateIfNecessary(Bitmap originalBitmap) {
        Bitmap transformedBitmap = originalBitmap;
        try {
            ExifInterface exif = new ExifInterface(imageDetail.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            transformedBitmap = Bitmap.createBitmap(
                    originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true); // rotating bitmap
        } catch (Exception e) {
            return null;
        }
        return transformedBitmap;
    }

    private Bitmap transform(String url) {
        Point size = getWindowSize();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outWidth = size.x;
        options.outHeight = size.y;
        Bitmap bitmap = BitmapFactory.decodeFile(url, options);

        Bitmap transformedBitmap = null;
        try {
            ExifInterface exif = new ExifInterface(imageDetail.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            transformedBitmap = Bitmap.createBitmap(
                    bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); // rotating bitmap
        } catch (Exception e) {
        }

        return transformedBitmap;
    }

    private Point getWindowSize() {
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }
}
