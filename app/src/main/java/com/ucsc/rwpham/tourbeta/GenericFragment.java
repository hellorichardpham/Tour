package com.ucsc.rwpham.tourbeta;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GenericFragment extends SlideFragment {

    public static GenericFragment newInstance() {
        return new GenericFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.intro, container, false);


        return root;
    }

    @Override
    public void onDestroy() {
        //loginHandler.removeCallbacks(loginRunnable);
        super.onDestroy();
    }

    @Override
    public boolean canGoForward() {
        return true;
        //return loggedIn;
    }
}