package com.grt.callrecorder.userInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.grt.callrecorder.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DEVEN SINGH on 9/4/2015.
 */
public class FeedbackFragment extends Fragment {

    @Bind(R.id.feedback_et)
    EditText feedbackET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.feedback_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_send_feedback) {
            submitFeedback();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void submitFeedback() {
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@globalradiant.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT, feedbackET.getText());
        getActivity().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
}
