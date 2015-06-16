package com.ziplly.mobile.imagegallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.google.common.collect.BoundType;

import java.io.Serializable;

/**
 * Created by susnata on 4/29/15.
 */
public class ImageDetailsActivity extends ActionBarActivity {

    private static final String TAG = ImageDetailsActivity.class.getCanonicalName();
    public static final String IMAGE_DATA = "IMAGE_DATA";
    public static final String IMAGE_POSITION = "IMAGE_POSITION";
    public static final String IMAGE_BOUNDING_RECT = "BOUNDING_RECT";
    public static final String LEFT = "LEFT";
    public static final String TOP = "TOP";
    public static final String IMAGE_VIEW = "IMAGE_VIEW";
    public static final String IMAGE_MANAGER = "IMAGE_MANAGER";
    public static final String IMAGE_INDEX = "IMAGE_INDEX";
    public static final String SINGLE_IMAGE_INFO = "SINGLE_IMAGE_INFO";

    private ViewPager viewPager;
    private ImageManager imageManager;
    private int focusIndex;
    private Rect startingBounds;
    private SingleImageInfo singleImageInfo;

    private ImageView singleImageView;
    private ImageDetail imageDetail;
    private View imageContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.AppThemeActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_details);
//        viewPager = (ViewPager) findViewById(R.id.viewPager);
//
//        imageManager = (ImageManager) getIntent().getSerializableExtra(IMAGE_MANAGER);
//        startingBounds = getIntent().getParcelableExtra(IMAGE_BOUNDING_RECT);
        singleImageInfo = (SingleImageInfo) getIntent().getSerializableExtra(SINGLE_IMAGE_INFO);
        imageDetail = singleImageInfo.imageDetail;
//        focusIndex = currentImageInfo.imageDetail.getPosition();
//        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(
//                getSupportFragmentManager(),
//                imageManager,
//                currentImageInfo);
//        viewPager.setAdapter(pagerAdapter);
//        viewPager.setCurrentItem(focusIndex);
//        pagerAdapter.notifyDataSetChanged();
//
//        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                int [] location = new int[2];
//                viewPager.getLocationOnScreen(location);
//                Log.d(TAG, "View pager x=" + location[0] + " y=" + location[1]);
//            }
//        });

//        imageContainer = findViewById(R.id.imageContainer);
        singleImageView = (ImageView)findViewById(R.id.singleImageView);
        final View rootLayout = findViewById(R.id.rootLayout);
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                runOnEnterAnimation();
                startAnimation();
            }
        });
    }

    private void startAnimation() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap image = rotateIfNecessary(BitmapFactory.decodeFile(singleImageInfo.imageDetail.getPath()));
//        Bitmap scaledImage = Bitmap.createScaledBitmap(image, singleImageInfo.width, singleImageInfo.height, false);


//        Log.d(TAG, "W="+image.getWidth()+" H="+image.getHeight()+" " +
//                "Original W="+singleImageInfo.width+" H="+singleImageInfo.height);

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
        float mScaleX = singleImageInfo.width/finalWidth;
        float mScaleY = singleImageInfo.height/finalHeight;
        Log.d(TAG, "scaleX ="+mScaleX+" scaleY="+mScaleY+" final H="+finalHeight+" W="+finalWidth);
        singleImageView.setScaleX(mScaleX);
        singleImageView.setScaleY(mScaleY);
        singleImageView.setImageBitmap(image);

        int posX = singleImageInfo.posX;
        int posY = singleImageInfo.posY;
        int[] location = new int[2];
        singleImageView.getLocationOnScreen(location);
        singleImageView.setPivotX(0);
        singleImageView.setPivotY(0);

        singleImageView.setTranslationX(posX - location[0]);
        singleImageView.setTranslationY(posY - location[1]);
        Log.d(TAG, "Original image x=" + posX + " y=" + posY + " New x=" + singleImageView.getX() + " y=" + singleImageView.getY());

        singleImageView.animate()
                .translationX(0)
                .translationY(0)
                .scaleX(1)
                .scaleY(1)
                .setDuration(5000)
                .setStartDelay(500)
                .withLayer()
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    private void logDetails(ImageDetail imageDetail) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imageDetail.getPath(), options);
        int width = options.outWidth;
        int height = options.outHeight;
        String type = options.outMimeType;
        Log.d(TAG, "IMAGE W = " + imageDetail.getWidth() + " H = " + imageDetail.getHeight());
        Log.d(TAG, "IMAGE W = " + width + " H = " + height + " M = " + type);
    }

    private void hide() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportActionBar().hide();
            }
        }, 3000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
        }

        return true;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        getSupportActionBar().show();
//        hide();
//        return super.onTouchEvent(event);
//    }

    private void runOnEnterAnimation() {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = false;
//        options.outHeight = getWindowSize().y;
//        options.outWidth = getWindowSize().x;

//        int [] vpLocation = new int[2];
//        View viewPager = getActivity().findViewById(R.id.viewPager);
//        viewPager.getLocationOnScreen(vpLocation);
//        Log.d(TAG, "View pager location: "+vpLocation[0]+","+vpLocation[1]);

        singleImageView.setPivotX(0);
        singleImageView.setPivotY(0);

        singleImageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.d(TAG, "Layout change Image W ="+v.getWidth()+", H="+v.getHeight());
            }
        });
//        singleImageView.setImageBitmap(ImageCache.bitmap);
//        singleImageView.setX(0);
//        singleImageView.setY(0);

        int [] location = new int[2];
        singleImageView.getLocationOnScreen(location);
        Log.d(TAG, "Image location before move: " + location[0] + "," + location[1]);

//        int translateX = singleImageInfo.posX - location[0];
//        int translateY = singleImageInfo.posY - location[1];

        Point size = getWindowSize();
        Log.d(TAG, "WW="+size.x+" WH="+size.y);

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

        int finalX = (int) (size.x - location[0] - finalWidth)/2;
        int finalY = (int) (size.y - location[1] - finalHeight)/2;
        int translateX = singleImageInfo.posX - location[0];
        int translateY = singleImageInfo.posY - location[1];

        int finalSize = (int) Math.max(finalHeight, finalWidth);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
//        options.inSampleSize = finalSize;

        Bitmap bitmap = BitmapFactory.decodeFile(imageDetail.getPath(), options);
        singleImageView.setImageBitmap(rotateIfNecessary(bitmap));

        Log.d(TAG, "Final W="+finalWidth+" H="+finalHeight+" A="+(finalWidth/finalHeight));
        Log.d(TAG, "Original W="+imageDetail.getWidth()+" H="+imageDetail.getHeight()+" A="+ar);

        float scaleX = (float) (singleImageInfo.width)/finalWidth;
        float scaleY = (float)(singleImageInfo.height)/finalHeight;

//        singleImageView.setScaleX(scaleX);
//        singleImageView.setScaleY(scaleY);
        singleImageView.setTranslationX(translateX);
        singleImageView.setTranslationY(translateY);
        singleImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//        singleImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String logStr = String.format(
                "IID=%d, Original X=%d,Y=%d, W=%d,H=%d, New X=%d,Y=%d, W=%d,H=%d " +
                        "Translation X=%d,Y=%d, ScaleX=%f,ScaleY=%f",
                singleImageInfo.imageDetail.getId(),
                singleImageInfo.posX, singleImageInfo.posY,
                singleImageInfo.width, singleImageInfo.height,
                location[0], location[1],
                singleImageView.getMeasuredWidth(), singleImageView.getMeasuredHeight(),
                translateX, translateY,
                scaleX, scaleY);

        Log.d(TAG, logStr);

        singleImageView.animate()
                .translationX(0)
                .translationY(0)
//                .x(finalX)
//                .y(finalY)
                .scaleX(1 / scaleX)
                .scaleY(1 / scaleY)
                .setStartDelay(2000)
                .setDuration(1000)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "END Image W ="+singleImageView.getWidth()+", H="+singleImageView.getHeight());
                    }
                })
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
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private final ImageManager imageManager;
        private final SingleImageInfo singleImageInfo;

        public ScreenSlidePagerAdapter(
                FragmentManager fm,
                ImageManager imageManager,
                SingleImageInfo singleImageInfo) {
            super(fm);
            this.imageManager = imageManager;
            this.singleImageInfo = singleImageInfo;
        }

        @Override
        public Fragment getItem(int position) {
            if (focusIndex == position) {
                return ImageDetailsFragment.newInstance(
                        imageManager.getImageDetails().get(position),
                        singleImageInfo);
            } else {
                return ImageDetailsFragment.newInstance(
                        imageManager.getImageDetails().get(position));
            }
        }

        @Override
        public int getCount() {
            return imageManager.getImageDetails().size();
        }
    }

    public static class SingleImageInfo implements Serializable {
        public int width;
        public int height;
        public int posX;
        public int posY;
        public ImageDetail imageDetail;

        public SingleImageInfo(int width, int height, int posX, int posY, ImageDetail imageDetail) {
            this.width = width;
            this.height = height;
            this.posX = posX;
            this.posY = posY;
            this.imageDetail = imageDetail;
        }
    }
}
