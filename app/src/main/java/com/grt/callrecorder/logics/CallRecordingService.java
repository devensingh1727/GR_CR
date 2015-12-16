package com.grt.callrecorder.logics;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;


import com.grt.callrecorder.R;
import com.grt.callrecorder.storage.MyPreferences;
import com.grt.callrecorder.utilities.RecordingNotification;

import java.io.File;
import java.io.IOException;

/**
 * Created by DEVEN SINGH on 9/8/2015.
 */
public class CallRecordingService extends Service implements MediaRecorder.OnInfoListener,
        MediaRecorder.OnErrorListener {

    private MyPreferences myPreferences;
    private static final int RECORDING_NOTIFICATION_ID = 27;
    private String recordingsPath;
    private boolean isRecording = false;
    private MediaRecorder mediaRecorder;
    private AudioManager audioManager;
    private Context context;
    private String mobNumber;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isRecording)
            return 0;
        context = getApplicationContext();
        myPreferences = new MyPreferences(context);
        recordingsPath=getRecordingFilePath(myPreferences);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if(myPreferences.isMaxRecordingVolume()) {
            int streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,streamMaxVolume, 0);
        }
        if (myPreferences.isAutoSpeakerEnable()) {
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.setSpeakerphoneOn(true);
        }
        try {
            createAndPrepareMediaRecorder(myPreferences.getAudioSource());
            try {
                mediaRecorder.start();
                isRecording = true;
                updateNotification(true);
            } catch (Exception e) {
                switch (myPreferences.getAudioSource()) {
                    case 2:
                        myPreferences.setErrorInUpLink(true);
                        break;
                    case 3:
                        myPreferences.setErrorInDownLink(true);
                        break;
                    case 4:
                        myPreferences.setErrorInVoiceCalls(true);
                        break;
                    case 5:
                        myPreferences.setErrorInCamCorder(true);
                        break;
                    case 6:
                        myPreferences.setErrorInVoiceRecognition(true);
                        break;
                    case 7:
                        myPreferences.setErrorInVoiceCommunnication(true);
                        break;
                }
                createAndPrepareMediaRecorder(1);
                myPreferences.setAudioSource(1);
                mediaRecorder.start();
                isRecording = true;
                updateNotification(true);
            }
        } catch (Exception e) {
            isRecording = false;
        }
        return START_NOT_STICKY;
    }

    private String getRecordingFilePath(MyPreferences myPreferences) {
        if (myPreferences.getCallerNumber() != null) {
            mobNumber = myPreferences.getCallerNumber().replace("-", "");
            mobNumber = mobNumber.replace("+", "");
            mobNumber = mobNumber.replace(" ", "");
        }
        File recordingFile=new File(recordingsDirectory() + "/" + System.currentTimeMillis()
                + "mn" + mobNumber + "ct"
                + myPreferences.getCallType() + getAudioPrefix(myPreferences));
        return recordingFile.getAbsolutePath();
    }

    String getAudioPrefix(MyPreferences myPreferences) {
        String suffix = "";
        int n = myPreferences.getAudioFormat();
        switch (n) {
            case 0:
                suffix = ".3gp";
                break;
            case 1:
                suffix = ".mpeg_4";
                break;
            case 2:
                suffix = ".amr";
                break;
        }
        return suffix;
    }

    File recordingsDirectory() {
        File recordingsMainDir = new File(Environment.getExternalStorageDirectory(),
                "/Global Call Recorder");
        File recordingsFolder = new File(recordingsMainDir.getAbsolutePath(),
                "/.Recordings");
        if (!recordingsMainDir.exists()) {
            recordingsMainDir.mkdirs();
            File noMedia = new File(recordingsMainDir.getAbsolutePath(), "/"
                    + ".nomedia");
            try {
                noMedia.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!recordingsFolder.exists()) {
                recordingsFolder.mkdirs();
                return recordingsFolder;
            } else {
                return recordingsFolder;
            }
        } else {
            return recordingsFolder;
        }
    }


    void createAndPrepareMediaRecorder(int audioSource) {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(audioSource);
        mediaRecorder.setOutputFormat(myPreferences.getAudioFormat());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setAudioChannels(myPreferences.getAudioChannel() + 1);
        mediaRecorder.setOutputFile(recordingsPath);
        mediaRecorder.setOnErrorListener(this);
        mediaRecorder.setOnInfoListener(this);
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Toast.makeText(context,"Unable to start recording call", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context,"Unable to start recording call.)", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (myPreferences.isAutoSpeakerEnable()) {
                audioManager.setMode(AudioManager.MODE_NORMAL);
                audioManager.setSpeakerphoneOn(false);
            }
            if (null != mediaRecorder) {
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                isRecording = false;
                mediaRecorder = null;
            }
        } catch (RuntimeException stopException) {
        }
        updateNotification(false);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        isRecording = false;
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        isRecording = false;
    }

    private void updateNotification(Boolean status) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (status) {
            Context context = getApplicationContext();
            CharSequence contentTitle = "Call Recorder Status";
            CharSequence contentText = "Call is recording...";
            Intent notificationIntent = new Intent(this,
                    CallRecordingService.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);
            Notification notification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_record_dot)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setContentIntent(contentIntent)
                    .build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            mNotificationManager.notify(RECORDING_NOTIFICATION_ID, notification);
        } else {
            mNotificationManager.cancel(RECORDING_NOTIFICATION_ID);
            RecordingNotification recordingNotification = new RecordingNotification(context);
            recordingNotification.showNotification();
        }
    }

}
