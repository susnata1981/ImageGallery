package com.ziplly.mobile.imagegallery;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by susnata on 4/19/15.
 */
public class ImageLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = ImageLoader.class.getCanonicalName();
    private final Context context;
    private final Callback callback;
    private List<ImageDetail> imageDetails = new ArrayList<>();

    ImageLoader(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                context,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int index = 0;
        int id = data.getColumnIndex(MediaStore.Images.ImageColumns._ID);
        int imagePath = data.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        int width = data.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH);
        int height = data.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT);
        int orientation = data.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION);
        Uri thumbnailUri = Uri.withAppendedPath(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, Integer.toString(id));

        while (data.moveToNext()) {
            ImageDetail imageDetail = new ImageDetail();
            imageDetail.setId(data.getLong(id));
            imageDetail.setPath(data.getString(imagePath));
            imageDetail.setPosition(index);
            imageDetail.setThumbnailUri(Uri.withAppendedPath(
                    MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, Integer.toString(id)).getPath());
            int orientationValue = data.getInt(orientation);
            imageDetail.setOrientation(orientationValue);
            switch (orientationValue) {
                case 0:
                case 180:
                    imageDetail.setHeight(data.getInt(height));
                    imageDetail.setWidth(data.getInt(width));
                    break;
                case 90:
                case 270:
                    imageDetail.setHeight(data.getInt(width));
                    imageDetail.setWidth(data.getInt(height));
                    break;
            }
            imageDetails.add(imageDetail);
            Log.d(TAG, "image("+index+") = " + imageDetail);
            index++;
        }

        if (callback != null) {
            callback.onLoadFinished(imageDetails);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public interface Callback {
        public void onLoadFinished(List<ImageDetail> imageDetails);
    }
}
