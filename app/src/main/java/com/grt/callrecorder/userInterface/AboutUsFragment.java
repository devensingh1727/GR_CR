package com.grt.callrecorder.userInterface;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.grt.callrecorder.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DEVEN SINGH on 9/4/2015.
 */
public class AboutUsFragment extends Fragment {

    @Bind(R.id.website_link)
    Button webSiteLink;
    @Bind(R.id.fb_link)
    Button fbLink;
    @Bind(R.id.support_num)
    Button supportNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.website_link)
    void sendToWebsite() {
        Intent intentWebLink = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.globalradiant.com"));
        startActivity(intentWebLink);
    }

    @OnClick(R.id.fb_link)
    void sendToFbPage() {
        Intent intentFbLink = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/GlobalRadiantTechnologies"));
        startActivity(intentFbLink);
    }

    @OnClick(R.id.support_num)
    void contactToSupport() {
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@globalradiant.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        getActivity().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
}
