package com.grt.callrecorder.userInterface;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.grt.callrecorder.R;
import com.grt.callrecorder.utilities.MusicPlayerUtility;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MusicPlayerActivity extends AppCompatActivity implements OnSeekBarChangeListener, OnCompletionListener {

    @Bind(R.id.toolbar_music)
    Toolbar toolbar;
    @BindDrawable(R.mipmap.ic_back)
    Drawable backButton;
    @Bind(R.id.seek_bar_music)
    SeekBar seekBar;
    @Bind(R.id.current_time)
    TextView currentTimeTextView;
    @Bind(R.id.total_time)
    TextView totalTimeTextView;
    @Bind(R.id.play_pause_button)
    ToggleButton playPauseTB;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private MusicPlayerUtility playerUtils;
    private Bundle extras;
    private String recordingFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initializeToolbar();
        initializeComponents();
        extras = getIntent().getExtras();
        if (extras != null) {
            recordingFilePath = extras.getString("audioFile");
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        playerUtils = new MusicPlayerUtility();
        playRecordings(recordingFilePath);
    }

    private void initializeToolbar() {
        if (toolbar != null)
            setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(backButton);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initializeComponents() {
        seekBar.setOnSeekBarChangeListener(this);
    }

    @OnClick(R.id.play_pause_button)
    void playPauseMusic() {
        if (mediaPlayer.isPlaying()) {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = playerUtils.progressToTimer(seekBar.getProgress(), totalDuration);

        mediaPlayer.seekTo(currentPosition);

        updateProgressBar();
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();
            totalTimeTextView.setText("" + playerUtils.milliSecondsToTimer(totalDuration));
            currentTimeTextView.setText("" + playerUtils.milliSecondsToTimer(currentDuration));

            int progress = (int) (playerUtils.getProgressPercentage(currentDuration, totalDuration));
            seekBar.setProgress(progress);

            handler.postDelayed(this, 100);
        }
    };

    public void updateProgressBar() {
        handler.postDelayed(mUpdateTimeTask, 100);
    }

    public void playRecordings(String recordingFilePath) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(audioFile(recordingFilePath));
            mediaPlayer.prepare();
            mediaPlayer.start();
            playPauseTB.setChecked(true);
            seekBar.setProgress(0);
            seekBar.setMax(100);
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playRecordings(recordingFilePath);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        handler.removeCallbacks(mUpdateTimeTask);
        mediaPlayer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        playPauseTB.setChecked(false);
    }

    private String audioFile(String recordingFilePath) {
        File audioFile = null;
        File globalFolder = new File(Environment.getExternalStorageDirectory(), "/Global Call Recorder/.Recordings");
        if (globalFolder.exists()) {
            audioFile = new File(globalFolder, "/" + recordingFilePath);
        }
        return audioFile.getAbsolutePath();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
