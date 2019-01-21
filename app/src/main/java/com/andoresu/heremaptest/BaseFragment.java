package com.andoresu.heremaptest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannedString;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.Unbinder;

public class BaseFragment extends Fragment {

    String TAG = "TESTTAG_" + BaseFragment.class.getSimpleName();

    private Unbinder unbinder;

    private Bundle fragmentBundle;

    private boolean isFragmentCreated;

    public Unbinder getUnbinder() {
        return unbinder;
    }

    public void setUnbinder(Unbinder unbinder) {
        this.unbinder = unbinder;
    }

    public Bundle getFragmentBundle() {
        return fragmentBundle;
    }

    public void setFragmentBundle(Bundle fragmentBundle) {
        this.fragmentBundle = fragmentBundle;
    }

    public boolean isFragmentCreated() {
        return isFragmentCreated;
    }

    public void setFragmentCreated(boolean fragmentCreated) {
        isFragmentCreated = fragmentCreated;
    }


    @Override
    public void onResume() {
        super.onResume();
        setFragmentCreated(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}