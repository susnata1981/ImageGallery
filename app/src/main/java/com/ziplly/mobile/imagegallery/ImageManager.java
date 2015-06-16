package com.ziplly.mobile.imagegallery;

import android.app.LoaderManager;
import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.util.Log;

import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.List;

/**
 * Created by susnata on 5/5/15.
 */
public class ImageManager implements Serializable {
    private final transient Context context;
    private final transient LoaderManager loaderManager;
    private transient ImageLoader imageLoader;
    private List<ImageDetail> imageDetails;

    ImageManager(Context context, LoaderManager loaderManager) {
        this.context = context;
        this.loaderManager = loaderManager;
    }

    public void loadImages(final Callback callback) {
        imageLoader = new ImageLoader(context, new ImageLoader.Callback() {
            @Override
            public void onLoadFinished(List<ImageDetail> imageDetails) {
                ImageManager.this.imageDetails = imageDetails;
                if (callback != null) {
                    callback.onFinishedLoading();
                }
            }
        });

        loaderManager.initLoader(0, null, imageLoader);
    }

    public List<ImageDetail> getImageDetails() {
        return ImmutableList.copyOf(imageDetails);
    }

    public interface Callback {
        public void onFinishedLoading();
    }
}
