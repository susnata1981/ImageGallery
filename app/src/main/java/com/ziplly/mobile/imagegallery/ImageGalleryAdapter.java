package com.ziplly.mobile.imagegallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by susnata on 4/19/15.
 */
public class ImageGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = ImageGalleryAdapter.class.getCanonicalName();
    private final Context context;
    private final List<RowImageLayout> imageLayoutData;
    private final ImageManager imageManager;
    private int currentPosition;

    public ImageGalleryAdapter(
            Context context,
            ImageManager imageManager,
            RowLayoutAdapter rowLayoutAdapter) {
        this.context = context;
        this.imageManager = imageManager;
        imageLayoutData = rowLayoutAdapter.convert(imageManager.getImageDetails());
    }

    @Override
    public int getItemViewType(int position) {
        return imageLayoutData.get(position).getRowLayout().ordinal();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(context).inflate(
                        R.layout.row_layout_0,
                        viewGroup,
                        false);
                view.setTag(RowLayout.TYPE3);
                break;
            case 1:
                view = LayoutInflater.from(context).inflate(
                        R.layout.row_layout_1,
                        viewGroup,
                        false);
                view.setTag(RowLayout.TYPE3);
                break;
            case 2:
                view = LayoutInflater.from(context).inflate(
                        R.layout.row_layout_2,
                        viewGroup,
                        false);
                view.setTag(RowLayout.TYPE2);
                break;
            case 3:
                view = LayoutInflater.from(context).inflate(
                        R.layout.row_layout_3,
                        viewGroup,
                        false);
                view.setTag(RowLayout.TYPE3);
                break;
        }

//        Log.d(TAG, "Creating view +" + view.getTag() + " for viewType = " + viewType);
        RowLayoutViewHolder rowLayoutViewHolder = new RowLayoutViewHolder(view);
        return rowLayoutViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RowLayoutViewHolder rowLayoutViewHolder = (RowLayoutViewHolder) viewHolder;
        imageLayoutData.get(position).setCurrentPosition(position * 4);
        imageLayoutData.get(position).draw(rowLayoutViewHolder.itemView);
        Log.d(TAG, "Drawing layout for position: "+position);
        currentPosition = position;
    }

    @Override
    public int getItemCount() {
        return imageLayoutData.size();
    }

    public RowImageLayout getRowLayout() {
        return imageLayoutData.get(currentPosition);
    }

    public static class RowLayoutViewHolder extends RecyclerView.ViewHolder {
        public RowLayoutViewHolder(View itemView) {
            super(itemView);
        }
    }
}
