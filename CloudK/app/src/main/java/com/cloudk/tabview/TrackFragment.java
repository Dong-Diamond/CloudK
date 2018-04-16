package com.cloudk.tabview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudk.R;


/**
 * Created by 世东 on 18/2/3.
 */
public class TrackFragment extends BaseFragment implements ITabClickListener {
    private static final String TAG = "YunKong";
    private static final String URL = "http://www.thxyno1.xin:8899/";


    @Override
    public void fetchData() {
        //点开页面执行

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_track, container, false);
        return view;
    }

    @Override
    public void onMenuItemClick() {
    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }


}
