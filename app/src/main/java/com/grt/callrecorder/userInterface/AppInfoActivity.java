package com.grt.callrecorder.userInterface;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.grt.callrecorder.R;
import com.grt.callrecorder.utilities.ConstantsValue;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;

/**
 * Created by DEVEN SINGH on 9/8/2015.
 */
public class AppInfoActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_app_info)
    Toolbar toolbar;
    @BindDrawable(R.mipmap.ic_back)
    Drawable backButton;
    private int fragNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        ButterKnife.bind(AppInfoActivity.this);
        initializeToolbar();
        initFragment();
    }

    private void initializeToolbar() {
        if (toolbar != null)
            setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(backButton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initFragment() {
        fragNum = getIntent().getIntExtra("fragNum", 0);
        if (fragNum == 0) {
            displayFrag(new AboutAppFragment());
        } else {
            AppInfoDetailsFragment fragmentDetails = new AppInfoDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("fragNum", fragNum);
            fragmentDetails.setArguments(bundle);
            displayFrag(fragmentDetails);
        }
    }

    private void displayFrag(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.app_info_container, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(ConstantsValue.APP_INFO_FRAG_TITTLE[fragNum]);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
