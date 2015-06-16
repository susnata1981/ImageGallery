package com.ziplly.mobile.imagegallery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;


public class ImageActivity extends ActionBarActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mImageView = (ImageView) findViewById(R.id.dummyImageView);
        mImageView.setImageResource(R.drawable.alicia);
        mImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                animate();

            }
        });
    }

    private void animate() {
        mImageView.setScaleX(.7f);
        mImageView.setScaleY(.7f);

        mImageView.animate()
                .scaleX(1.5f)
                .scaleY(1.5f)
                .setStartDelay(500)
                .setDuration(3000)
                .setInterpolator(new BounceInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ImageActivity.this, "Finished animating", Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
