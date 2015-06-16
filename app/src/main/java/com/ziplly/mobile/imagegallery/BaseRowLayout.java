package com.ziplly.mobile.imagegallery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.List;

/**
 * Created by susnata on 4/28/15.
 */
public abstract class BaseRowLayout implements RowImageLayout {

    private static final String TAG = BaseRowLayout.class.getCanonicalName();
    protected final Context context;
    protected final List<ImageDetail> imageDetailList;
    private final RecyclerView recyclerView;
    private final View rootContainer;
    protected int windowHeight;
    protected int windowWidth;
    protected static boolean displayZoomedImage;
    private Rect startingBounds = new Rect();
    private Rect finalBounds = new Rect();
    private ImageView selectedImage;
    protected int currentPosition;

    public BaseRowLayout(Context context, List<ImageDetail> imageDetailList) {
        this.context = context;
        this.imageDetailList = imageDetailList;
        recyclerView = (RecyclerView) ((MainActivity) context).findViewById(R.id.imView);
        rootContainer = ((MainActivity) context).findViewById(R.id.main_container);
        setWindowDimension();
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    protected void setWindowDimension() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        this.windowHeight = displayMetrics.heightPixels;
        this.windowWidth = displayMetrics.widthPixels;
    }

    protected ImageDetail getImageWithHigestAspectRatio(List<ImageDetail> images) {
        ImageDetail result = null;
        double ar = 0;
        for (ImageDetail imageDetail : images) {
            double temp = getAspectRatio(imageDetail);
            if (temp > ar) {
                ar = temp;
                result = imageDetail;
            }
        }

        images.remove(result);
        return result;
    }

    protected ImageDetail getImageWithLowestAspectRatio(List<ImageDetail> images) {
        ImageDetail result = null;
        double ar = Integer.MAX_VALUE;
        for (ImageDetail imageDetail : images) {
            double temp = getAspectRatio(imageDetail);
            if (temp < ar) {
                ar = temp;
                result = imageDetail;
            }
        }

        images.remove(result);
        return result;
    }

    protected void loadImage(final ImageDetail image, final ImageView imageView) {

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        if (layoutParams.height <= 0 || layoutParams.width <= 0) {
            return;
        }

        File file = new File(image.getPath());
        Picasso.with(context)
                .load(file)
                .resize(imageView.getLayoutParams().width, imageView.getLayoutParams().height)
                .transform(new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {
                        int boundingHeight = imageView.getLayoutParams().height;
                        int boundingWidth = imageView.getLayoutParams().width;

                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                                source, boundingWidth, boundingHeight, false);
                        if (scaledBitmap != source) {
                            source.recycle();
                        }

                        return scaledBitmap;
                    }

                    @Override
                    public String key() {
                        return image.getPath();
                    }
                })
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
//                        Log.d("RowImageLayout0", "HEIGHT = " + imageView.getHeight());
                        addOnClickHandler(imageView, image);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    protected void addOnClickHandler(final ImageView imageView, final ImageDetail imageDetail) {
        selectedImage = imageView;
        Log.d(TAG, "Selected image ="+imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location = new int[2];
                selectedImage = imageView;
//                imageView.getLocationInWindow(location);
                imageView.getLocationOnScreen(location);
                imageView.getGlobalVisibleRect(startingBounds, new Point());
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ImageCache.bitmap = bitmap;

                Log.d(TAG, "ID = " + imageDetail.getId() + " X=" + location[0] + " Y=" + location[1]
                        + " W=" + imageView.getWidth() + " H=" + imageView.getHeight()
                        +" Starting bound x = "+startingBounds.left+" y="+startingBounds.top);


                Intent intent = new Intent(context, ImageDetailsActivity.class);
                ImageManager imageManager = ((MainActivity) context).getImageManager();
                Bundle bundle = new Bundle();
                ImageDetailsActivity.SingleImageInfo singleImageInfo = new ImageDetailsActivity.SingleImageInfo(
                        imageView.getWidth(),
                        imageView.getHeight(),
                        location[0],
                        location[1],
                        imageDetail);

                String tag = (String) selectedImage.getTag();
//                bundle.putParcelable(ImageDetailsActivity.IMAGE_BOUNDING_RECT, BaseRowLayout.this.startingBounds);
                bundle.putSerializable(ImageDetailsActivity.IMAGE_MANAGER, imageManager);
//                bundle.putInt(ImageDetailsActivity.IMAGE_INDEX, currentPosition + Integer.parseInt(tag) - 1);
                bundle.putSerializable(ImageDetailsActivity.SINGLE_IMAGE_INFO, singleImageInfo);
                intent.putExtras(bundle);
                context.startActivity(intent);
                ((MainActivity) context).overridePendingTransition(0, 0);
            }
        });
    }

    private void animate(ImageView imageView, ImageDetail imageDetail, Rect startingBounds) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outWidth = (int) imageDetail.getWidth();
        options.outHeight = (int) imageDetail.getHeight();
        Bitmap originalBitmap = BitmapFactory.decodeFile(imageDetail.getPath(), options);
        Bitmap transformedBitmap = null;
        try {
            ExifInterface exif = new ExifInterface(imageDetail.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
//            Log.d("EXIF", "Exif: " + orientation);
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
        imageView.setVisibility(View.VISIBLE);
//        imageView.setImageURI(Uri.parse(imageData.getPath()));

//        recyclerView.setVisibility(View.INVISIBLE);
        imageView.setX(startingBounds.left);
        imageView.setY(startingBounds.top);
        rootContainer.getGlobalVisibleRect(finalBounds);

        float scaleX = (float) selectedImage.getWidth() / (float) imageDetail.getWidth();
        float scaleY = (float) selectedImage.getHeight() / (float) imageDetail.getHeight();

        float scale = Math.min(scaleX, scaleY);
        Log.d(TAG, "original W = " + selectedImage.getWidth() + " H =" + selectedImage.getHeight() + " " +
                "SX = " + scaleX + " SY = " + scaleY + " X =" + startingBounds.left + " Y = " + startingBounds.top);

        imageView.setPivotY(0);
        imageView.setPivotX(0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(imageView, View.X, startingBounds.left, finalBounds.left),
                ObjectAnimator.ofFloat(imageView, View.Y, startingBounds.top, finalBounds.top),
                ObjectAnimator.ofFloat(imageView, View.SCALE_X, scale, 1),
                ObjectAnimator.ofFloat(imageView, View.SCALE_Y, scale, 1));

        set.setDuration(1000);
        set.setStartDelay(1000);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                Intent intent = new Intent(context, ImageDetailsActivity.class);
//                ImageManager imageManager = ((MainActivity)context).getImageManager();
//                Bundle bundle = new Bundle();
//                String tag = (String) selectedImage.getTag();
//                bundle.putParcelable(ImageDetailsActivity.IMAGE_BOUNDING_RECT, BaseRowLayout.this.startingBounds);
//                bundle.putSerializable(ImageDetailsActivity.IMAGE_MANAGER, imageManager);
//                bundle.putInt(ImageDetailsActivity.IMAGE_INDEX, currentPosition + Integer.parseInt(tag) -1);
//                intent.putExtras(bundle);
//                context.startActivity(intent);
            }
        });
        set.start();
        displayZoomedImage = true;
    }

    @Override
    public boolean onBackPresesed() {
        return true;
//        if (displayZoomedImage) {
//            displayZoomedImage = false;
//            if (fullImageView != null) {
//                Log.d(TAG, "FullImageView animator = " + fullImageView.animate());
//                fullImageView.animate()
//                        .x(startingBounds.left)
//                        .y(startingBounds.top)
//                        .scaleX((int) (selectedImage.getWidth() / fullImageView.getWidth()))
//                        .scaleY((int) (selectedImage.getHeight() / fullImageView.getHeight()))
//                        .setListener(new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                super.onAnimationEnd(animation);
//                                fullImageView.setVisibility(View.GONE);
//                            }
//                        })
//                        .start();
//                fullImageView.setVisibility(View.INVISIBLE);
//                RecyclerView recyclerView = (RecyclerView) ((MainActivity) context).findViewById(R.id.imView);
//                recyclerView.setVisibility(View.VISIBLE);
//                return true;
//            }
//        }
//
//        return false;
    }

    protected double getAspectRatio(ImageDetail image) {
//        Bitmap _bitmapPreScale = BitmapFactory.decodeFile(image.getPath());
//        double oldWidth = _bitmapPreScale.getWidth();
//        double oldHeight = _bitmapPreScale.getHeight();
//        return oldWidth/oldHeight;
        return (double) image.getWidth() / (double) image.getHeight();
    }

    protected int minWidth() {
        return (int) Math.round(windowWidth * .3);
    }

    protected int maxWidth() {
        return (int) Math.round(windowWidth * .6);
    }

    protected int maxHeight() {
        return Math.round(windowHeight);
    }

    protected int minHeight() {
        return (int) Math.round(windowHeight * .4);
    }

    protected ImageDetail getNextImage(List<ImageDetail> images) {
        return images.remove(0);
    }

    protected void layoutImage(ImageDetail image, ImageView imageView, double width, double height) {
//        Log.d(TAG, "IMAGE1 H = "+height1+" W = "+nwidth1+" A = "+aspectRatio);
        ImageDim dim = new ImageDim();
        dim.width = (int) width;
        dim.height = (int) height;
        applyDimensionConstraint(dim);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = dim.width;
        layoutParams.height = dim.height;
        Log.d(TAG, "Loading image w = "+dim.width+" h = "+dim.height);

        loadImage(image, imageView);
    }

    public static void reverseAnimate() {

    }

    protected void applyDimensionConstraint(ImageDim imageDim) {
        double ar = imageDim.width/imageDim.height;
        if (imageDim.height > windowHeight) {
            imageDim.height = (int) (windowHeight * .5);
            imageDim.width = (int) (ar * imageDim.height);
        } else if (imageDim.width > windowWidth) {
            imageDim.width = (int) (windowWidth);
            imageDim.height = (int)(imageDim.width/ar);
        }
    }

    public static class ImageDim {
        public int height;
        public int width;
    }
}
