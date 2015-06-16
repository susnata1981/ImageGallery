package com.ziplly.mobile.imagegallery;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();
    public static final int IMAGE_DETAILS = 0;
    private List<String> imageUrl = new ArrayList<>();
    private RecyclerView imView;
    private ImageGalleryAdapter imageGalleryAdapter;
    private ImageLoader imageLoader;
    private ImageManager imageManager;
    private RowLayoutAdapter rowLayoutAdapter;
    private ImageDetail imageDetail;
    private Rect startingBounds;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imView = (RecyclerView) findViewById(R.id.imView);
        setupRecyclerView();
        rowLayoutAdapter = new RowLayoutAdapter(this);
        imageManager = new ImageManager(this, getLoaderManager());
        imageManager.loadImages(new ImageManager.Callback() {
            @Override
            public void onFinishedLoading() {
                Log.d(TAG, "Loaded images = " + imageManager.getImageDetails().size());
                rowLayoutAdapter = new RowLayoutAdapter(MainActivity.this);
                imageGalleryAdapter = new ImageGalleryAdapter(
                        MainActivity.this, imageManager, rowLayoutAdapter);
                imView.setAdapter(imageGalleryAdapter);
                imageGalleryAdapter.notifyDataSetChanged();
            }
        });

//        hideStatusBar();
    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LOW_PROFILE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_DETAILS) {
            if (resultCode == RESULT_OK) {
                int left = data.getIntExtra(ImageDetailsActivity.LEFT, 0);
                int top = data.getIntExtra(ImageDetailsActivity.TOP, 0);
                ImageDetail imageDetail = (ImageDetail) data.getSerializableExtra(
                        ImageDetailsActivity.IMAGE_DATA);
//                animateImage(imageData, left, top);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "OnPause called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "OnDestroy called");
    }

    @Override
    public void onResume() {
        super.onResume();
        BaseRowLayout.reverseAnimate();
    }

    @Override
    public void onBackPressed() {
        RowImageLayout rowLayout = imageGalleryAdapter.getRowLayout();
        if (rowLayout.onBackPresesed()) {
            return;
        }

        super.onBackPressed();
    }

    public ImageManager getImageManager() {
        return imageManager;
    }

    private void logDetails(List<RowImageLayout> imageDataList) {
        ConcurrentMap<RowLayout, AtomicInteger> countMap = new ConcurrentHashMap();
        int index = 0;
        for (RowImageLayout rowImageLayout : imageDataList) {
            countMap.putIfAbsent(rowImageLayout.getRowLayout(), new AtomicInteger(0));
            countMap.get(rowImageLayout.getRowLayout()).incrementAndGet();
        }

        StringBuilder sb = new StringBuilder();
        for (RowLayout rowLayout : countMap.keySet()) {
            sb.append("\nLayout = " + rowLayout.name() + " Count =" + countMap.get(rowLayout));
        }
        Log.d(TAG, sb.toString());
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        imView.setLayoutManager(layoutManager);
    }
}
