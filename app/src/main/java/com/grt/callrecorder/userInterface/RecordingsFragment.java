package com.grt.callrecorder.userInterface;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bowyer.app.fabtoolbar.FabToolbar;
import com.grt.callrecorder.R;
import com.grt.callrecorder.adapterClasses.RecordingsAdapter;
import com.grt.callrecorder.modelClasses.RecordingsItemPojo;
import com.grt.callrecorder.utilities.MultipleItemSelectionObserver;
import com.grt.callrecorder.utilities.OnViewClickListner;
import com.grt.callrecorder.utilities.RecyclerViewScrollListner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DEVEN SINGH on 9/4/2015.
 */
public class RecordingsFragment extends Fragment implements OnViewClickListner, MultipleItemSelectionObserver {

    @Bind(R.id.no_recordings)
    TextView noRecordings;
    @Bind(R.id.recordings_list)
    RecyclerView recordingsRecyclerView;
    //    ProgressDialog progressDialog;
    RecordingsAdapter recordingsAdapter;
    ArrayList<RecordingsItemPojo> recordingsItemPojosList = new ArrayList<RecordingsItemPojo>();
    @Bind(R.id.fabtoolbar)
    FabToolbar mFabToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.img_share)
    ImageView shareBt;
    @Bind(R.id.img_delete)
    ImageView deleteBt;
    @Bind(R.id.img_cross)
    ImageView backBt;
    boolean isMultipleItemSelected = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        progressDialog = ProgressDialog.show(getActivity(), "",
//                "Loading. Please wait...", true);
        View view = inflater.inflate(R.layout.fragment_recordings, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeComponents();
        initializeRecordingListItem();
    }

    private void iconAnim(View icon) {
        Animator iconAnim = ObjectAnimator.ofPropertyValuesHolder(
                icon,
                PropertyValuesHolder.ofFloat("scaleX", 1f, 1.5f, 1f),
                PropertyValuesHolder.ofFloat("scaleY", 1f, 1.5f, 1f));
        iconAnim.start();
    }

    private void initializeComponents() {
        recordingsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mFabToolbar.setFab(mFab);
        recordingsRecyclerView.addOnScrollListener(new RecyclerViewScrollListner() {
            @Override
            public void onHide() {
                if (isMultipleItemSelected)
                    mFabToolbar.slideOutFab();
            }

            @Override
            public void onShow() {
                if (isMultipleItemSelected)
                    mFabToolbar.slideInFab();
            }
        });
    }

    @OnClick(R.id.fab)
    void fabButton() {
        mFabToolbar.expandFab();
    }

    @OnClick(R.id.img_cross)
    void exitFabToolBar() {
        mFabToolbar.contractFab();
    }

    @OnClick(R.id.img_share)
    void shareRecordings() {
        shareMultipleRecordings();
    }

    @OnClick(R.id.img_delete)
    void deleteRecordings() {
        mFabToolbar.slideOutFab();
        if (recordingsAdapter.isAnySelected()) {
            alertDeleteRecordings();
        } else {
            Toast.makeText(getActivity(), "Select at least one item.", Toast.LENGTH_LONG).show();
        }
    }

    private void alertDeleteRecordings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.IshiDialogTheme);
        builder.setTitle("Do you want to delete these recordings?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new DeleteMultiPleRecordings().execute();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private void initializeRecordingListItem() {
        if (recordingsItemPojosList != null) {
            recordingsAdapter = new RecordingsAdapter(recordingsItemPojosList, RecordingsFragment.this, RecordingsFragment.this);
        }
        recordingsRecyclerView.setAdapter(recordingsAdapter);
//                            progressDialog.dismiss();
//        mFabToolbar.slideOutFab();
        new Thread() {
            public void run() {
                getRecordingListItems();
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            if (recordingsItemPojosList.size() > 0) {
                                noRecordings.setVisibility(View.GONE);
                            } else {
                                noRecordings.setVisibility(View.VISIBLE);
                            }
                            mFabToolbar.slideOutFab();
                        }
                    });
                } catch (Exception e) {

                }
            }
        }.start();

    }

    private void getRecordingListItems() {
        File audioDir = new File(Environment.getExternalStorageDirectory()
                + "/Global Call Recorder/.Recordings");
        if (audioDir.exists()) {
            String[] audioFileListArray = audioDir.list();
            if (audioFileListArray.length != 0) {
                for (String audioFileName : audioFileListArray) {
                    RecordingsItemPojo myRecordingListItem = new RecordingsItemPojo();
                    myRecordingListItem.setRecordingFiles(audioFileName);
                    myRecordingListItem.setCallerName(getCallerName(audioFileName.substring(audioFileName.indexOf("mn") + 2, audioFileName.indexOf("ct"))));
                    myRecordingListItem.setCallType(getCallType(audioFileName));
                    myRecordingListItem.setFormat(getAudioFileFormat(audioFileName));
                    myRecordingListItem.setTiming(getRecordingTime(audioFileName));
                    myRecordingListItem.setFileSize(getAudioFileSize(audioDir, audioFileName));
                    recordingsItemPojosList.add(myRecordingListItem);
                    if (getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recordingsAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }
    }

    private String getAudioFileSize(File audioDir, String audioFileName) {
        final File file = new File(audioDir, "/" + audioFileName);
        double fileSize = (file.length() / 1024);
        return String.valueOf(fileSize) + " KB";
    }

    private String getRecordingTime(String audioFileName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ' ' h:mm a");
        String dateString = dateFormat.format(new Date(Long.parseLong(audioFileName.substring(0, audioFileName.indexOf("mn")))));
        return dateString;
    }

    private String getAudioFileFormat(String audioFileName) {
        if (audioFileName.contains("mpeg_4")) {
            return "MPEG_4";
        } else if (audioFileName.contains("3gp")) {
            return "THREE_GPP";
        } else if (audioFileName.contains("amr")) {
            return "AMR";
        } else {
            return "Unavailable";
        }
    }

    private String getCallType(String audioFileName) {
        if (audioFileName.contains("incoming")) {
            return "Incoming";
        } else if (audioFileName.contains("outgoing")) {
            return "Outgoing";
        } else {
            return "Unavailable";
        }
    }

    private String getCallerName(String number) {
        if (number == null) {
            return "Unavailable";
        } else {
            return getCallerNameNum(number);
        }
    }

    private String getCallerNameNum(String num) {
        String callerName = null;
        Cursor c = null;
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(num));
        try {
            c = getActivity().getContentResolver().query(lookupUri,
                    new String[]{ContactsContract.Data.DISPLAY_NAME}, null,
                    null, null);

            c.moveToFirst();
            callerName = c.getString(0);
        } catch (Exception e) {

        } finally {
            if (c != null)
                c.close();
        }
        if (callerName != null) {
            num = callerName;
        } else {
            if (num == null && num.equalsIgnoreCase("")) {
                num = "Unavailable";
            }
        }
        return num;
    }

    @Override
    public void setOnViewClickListner(View view, String filePath, int position) {
        if (view.getId() == R.id.more_option) {
            showPopupMenu(view, filePath, position);
        }
        if (view.getId() == R.id.ll_recordings) {
            playRecording(filePath);
        }
    }

    private void playRecording(String filePath) {
        Intent intentAudioPlayer = new Intent(getActivity(), MusicPlayerActivity.class);
        intentAudioPlayer.putExtra("audioFile", filePath);
        startActivity(intentAudioPlayer);
    }

    private void showPopupMenu(View view, final String filePath, final int position) {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        popup.getMenuInflater().inflate(R.menu.action_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_play:
                        playRecording(filePath);
                        break;
                    case R.id.action_share:
                        shareSingleRecording(filePath);
                        break;
                    case R.id.action_delete:
                        deleteSingleRecording(filePath, position);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    private void deleteSingleRecording(String filePath, int position) {
        File file = new File(Environment.getExternalStorageDirectory()
                + "/Global Call Recorder/.Recordings" + "/" + filePath);
        file.delete();
        recordingsItemPojosList.remove(position);
        recordingsAdapter.notifyDataSetChanged();
    }

    private void shareSingleRecording(String filePath) {
        Intent intentShare = new Intent();
        intentShare.setAction(Intent.ACTION_SEND);
        intentShare.setType("audio/*");
        File recordingFile = new File(Environment.getExternalStorageDirectory()
                + "/Global Call Recorder/.Recordings" + "/" + filePath);
        intentShare.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(recordingFile));
        startActivity(Intent.createChooser(intentShare, "Share Recording"));
    }

    protected void shareMultipleRecordings() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("audio/*");
        File audioDir = new File(Environment.getExternalStorageDirectory()
                + "/Global Call Recorder/.Recordings");
        ArrayList<Uri> files = new ArrayList<Uri>();
        ArrayList<String> audioPath = recordingsAdapter.getSelectedRecordings();
        if (audioPath.size() == 0) {
            Toast.makeText(getActivity(), "Select at least one item.", Toast.LENGTH_LONG).show();
        } else {
            for (int i = 0; i < audioPath.size(); i++) {
                File file = new File(audioDir.getAbsolutePath() + "/" + audioPath.get(i).toString().substring(audioPath.get(i).toString().indexOf("_pos_") + 5));
                Uri uri = Uri.fromFile(file);
                files.add(uri);
            }
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
            startActivity(Intent.createChooser(intent, "Share Recordings"));
        }
    }

    @Override
    public void isMultipleItemSelected(boolean selected) {
        if (selected) {
            if (!isMultipleItemSelected)
                mFabToolbar.slideInFab();
            else if (mFabToolbar.isFabExpanded())
                mFabToolbar.contractFab();
        } else {
            if (isMultipleItemSelected) {
                mFabToolbar.slideOutFab();
            }
        }
        isMultipleItemSelected = selected;
    }

    private class DeleteMultiPleRecordings extends AsyncTask<Void, Integer, Void> {

        ProgressDialog progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new ProgressDialog(getActivity(), R.style.IshiDialogTheme);
            progressBar.setTitle("Processing");
            progressBar.setMessage(Html.fromHtml("Please Wait..."));
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(false);
            progressBar.show();
            progressBar.setCanceledOnTouchOutside(false);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            deleteAudios();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.dismiss();
            recordingsAdapter.notifyDataSetChanged();
        }
    }

    private void deleteAudios() {
        File audioDir = new File(Environment.getExternalStorageDirectory()
                + "/Global Call Recorder/.Recordings");
        ArrayList<String> audioPath = recordingsAdapter.getSelectedRecordings();
        for (int i = 0; i < audioPath.size(); i++) {
            File file = new File(audioDir.getAbsolutePath() + "/" + audioPath.get(i).toString().substring(audioPath.get(i).toString().indexOf("_pos_") + 5));
            file.delete();
            recordingsItemPojosList.remove(Integer.parseInt(audioPath.get(i).toString().substring(0, audioPath.get(i).toString().indexOf("_pos_"))) - i);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.recordings_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_select_all) {
            if (null != recordingsAdapter) {
                recordingsAdapter.selectAllRecordings();
            } else {
                Toast.makeText(getActivity(), "There is no recordings present.", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if (item.getItemId() == R.id.action_select_none) {
            if (null != recordingsAdapter) {
                recordingsAdapter.selectNoneRecordings();
            } else {
                Toast.makeText(getActivity(), "There is no recordings present.", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if (item.getItemId() == R.id.action_select_inverse) {
            if (null != recordingsAdapter) {
                if (recordingsAdapter.isAnySelected()) {
                    recordingsAdapter.selectInverseRecordings();
                } else {
                    Toast.makeText(getActivity(), "At least one item should be selected for this action.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), "There is no recordings present.", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
