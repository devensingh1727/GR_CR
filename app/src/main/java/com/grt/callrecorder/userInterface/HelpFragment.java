package com.grt.callrecorder.userInterface;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.grt.callrecorder.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DEVEN SINGH on 9/4/2015.
 */
public class HelpFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.ll_about)
    LinearLayout llAbout;
    @Bind(R.id.ll_faq)
    LinearLayout llFaq;
//    @Bind(R.id.ll_tos)
//    LinearLayout llTos;
    @Bind(R.id.ll_privacy_policy)
    LinearLayout llPrivacyPolicy;
    @Bind(R.id.ll_software_licences)
    LinearLayout llSoftwareLicences;
    @Bind(R.id.ll_share)
    LinearLayout llShare;
    @Bind(R.id.ll_rate)
    LinearLayout llRate;
    @Bind(R.id.ll_like_fb)
    LinearLayout llLikeFb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeComponents();
    }

    private void initializeComponents() {
        llAbout.setOnClickListener(this);
        llFaq.setOnClickListener(this);
//        llTos.setOnClickListener(this);
        llPrivacyPolicy.setOnClickListener(this);
        llSoftwareLicences.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llRate.setOnClickListener(this);
        llLikeFb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == llAbout) {
            startDetailActivity(0);
        }
        if (v == llFaq) {
            startDetailActivity(1);
        }
//        if (v == llTos) {
//            startDetailActivity(2);
//        }
        if (v == llPrivacyPolicy) {
            startDetailActivity(3);
        }
        if (v == llSoftwareLicences) {
            startDetailActivity(4);
        }
        if (v == llShare) {
            shareApp();
        }
        if (v == llRate) {
            rateApp();
        }
        if (v == llLikeFb) {
            likeUsOnFb();
        }
    }


    private void startDetailActivity(int fragNum) {
        Intent intentDetails = new Intent(getActivity(), AppInfoActivity.class);
        intentDetails.putExtra("fragNum", fragNum);
        startActivity(intentDetails);
    }

    private void likeUsOnFb() {
        Intent likeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/GlobalRadiantTechnologies"));
        startActivity(likeIntent);
    }

    private void rateApp() {
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.grt.callrecorder"));
        startActivity(rateIntent);
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hi! I am recommending you Call Recorder app, since I think it can be useful to you too. https://play.google.com/store/apps/details?id=com.grt.callrecorder");
        startActivity(Intent.createChooser(shareIntent, "Share App"));
    }
}
