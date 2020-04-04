package com.bignerdranch.android.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import androidx.fragment.app.Fragment;


public class SunsetFragment extends Fragment {

    private View mSceneView;
    private View mSunView;
    private View mSkyView;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    private boolean mSunset = true;

    public static SunsetFragment newInstance() {
        return new SunsetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset, container, false);

        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mSkyView = view.findViewById(R.id.sky);

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation(mSunset);
                mSunset = !mSunset;
            }
        });

        return view;
    }

    private void startAnimation(boolean sunset) {
        float sunYStart = sunset ? mSunView.getTop() : mSkyView.getHeight();
        float sunYEnd = sunset ? mSkyView.getHeight() : mSunView.getTop();
        int lightSkyColorStart = sunset ? mBlueSkyColor : mSunsetSkyColor;
        int lightSkyColorEnd = sunset ? mSunsetSkyColor : mBlueSkyColor;
        int darkSkyColorStart = sunset ? mSunsetSkyColor : mNightSkyColor;
        int darkSkyColorEnd = sunset ? mNightSkyColor : mSunsetSkyColor;

        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", lightSkyColorStart, lightSkyColorEnd)
                .setDuration(3000);
        sunSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", darkSkyColorStart, darkSkyColorEnd)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        if(sunset) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet
                    .play(heightAnimator)
                    .with(sunSkyAnimator)
                    .before(nightSkyAnimator);
            animatorSet.start();
        } else {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet
                    .play(nightSkyAnimator)
                    .before(heightAnimator);
            animatorSet
                    .play(heightAnimator)
                    .with(sunSkyAnimator);
            animatorSet.start();
        }
    }
}
