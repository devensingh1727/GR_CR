package com.grt.callrecorder.userInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


import com.grt.callrecorder.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DEVEN SINGH on 9/8/2015.
 */
public class AppInfoDetailsFragment extends Fragment {

    @Bind(R.id.web_view_legal)
    WebView webViewLegal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        ButterKnife.bind(this, view);
        appInfoDetails(getArguments().getInt("fragNum"));
        return view;
    }

    private void appInfoDetails(int anInt) {
        switch (anInt){
            case 1:
                webViewLegal.loadUrl("file:///android_asset/faq.html");
                break;
            case 2:
                webViewLegal.loadUrl("file:///android_asset/tos.html");
                break;
            case 3:
                webViewLegal.loadUrl("file:///android_asset/privacy_policy.html");
                break;
            case 4:
                webViewLegal.loadUrl("file:///android_asset/open_source_licenses.html");
                break;
        }
    }
}
