package com.grt.callrecorder.userInterface;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grt.callrecorder.R;
import com.grt.callrecorder.logics.CallReceiverBroadcast;
import com.grt.callrecorder.storage.MyPreferences;
import com.grt.callrecorder.utilities.ConstantsValue;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DEVEN SINGH on 9/4/2015.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.recorder_activator)
    SwitchCompat recordingEnable;
    @Bind(R.id.password_enable)
    SwitchCompat passwordProtection;
    @Bind(R.id.auto_speaker_enable)
    SwitchCompat autoSpeaker;
    @Bind(R.id.auto_max_volume)
    SwitchCompat autoMaxVolume;
    @Bind(R.id.ll_audio_source)
    LinearLayout llAudioSource;
    @Bind(R.id.ll_audio_channel)
    LinearLayout llAudioChannel;
    @Bind(R.id.ll_audio_format)
    LinearLayout llAudioFormat;
    @Bind(R.id.ll_password)
    LinearLayout llPasswordChange;
    @Bind(R.id.ll_security_q)
    LinearLayout llSecurityQuestion;
    @Bind(R.id.audio_source_tv)
    TextView audioSource;
    @Bind(R.id.audio_channel_tv)
    TextView audioChannel;
    @Bind(R.id.audio_format_tv)
    TextView audioFormat;
    private MyPreferences myPreferences;
    private int audioChoice;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeComponents();
    }

    private void initializeComponents() {
        myPreferences = new MyPreferences(getActivity());
        llAudioSource.setOnClickListener(this);
        llAudioChannel.setOnClickListener(this);
        llAudioFormat.setOnClickListener(this);
        llPasswordChange.setOnClickListener(this);
        llSecurityQuestion.setOnClickListener(this);
        recordingEnable.setOnCheckedChangeListener(this);
        autoSpeaker.setOnCheckedChangeListener(this);
        autoMaxVolume.setOnCheckedChangeListener(this);
        passwordProtection.setOnCheckedChangeListener(this);
        recordingEnable.setChecked(myPreferences.isRecordingEnable());
        passwordProtection.setChecked(myPreferences.isPasswordProtected());
        autoSpeaker.setChecked(myPreferences.isAutoSpeakerEnable());
        autoMaxVolume.setChecked(myPreferences.isMaxRecordingVolume());
        initializeAudioPrefs();
    }

    private void initializeAudioPrefs() {
        audioSource.setText(ConstantsValue.audioSource[myPreferences.getAudioSource()]);
        audioChannel.setText(ConstantsValue.audioChannel[myPreferences.getAudioChannel()]);
        audioFormat.setText(ConstantsValue.audioFormat[myPreferences.getAudioFormat()]);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), SecurityActivity.class);
        if (v == llAudioSource) {
            chooseAudio(getActivity().getResources().getString(R.string.audio_source), ConstantsValue.audioSource, myPreferences.getAudioSource());
        }
        if (v == llAudioChannel) {
            chooseAudio(getActivity().getResources().getString(R.string.audio_channel), ConstantsValue.audioChannel, myPreferences.getAudioChannel());
        }
        if (v == llAudioFormat) {
            chooseAudio(getActivity().getResources().getString(R.string.audio_format), ConstantsValue.audioFormat, myPreferences.getAudioFormat());
        }
        if (v == llPasswordChange) {
            intent.putExtra("fragNum", 0);
            startActivity(intent);
        }
        if (v == llSecurityQuestion) {
            intent.putExtra("fragNum", 1);
            startActivity(intent);
        }
    }

    private void chooseAudio(String tittle, final String[] audio, final int prefs) {
        audioChoice = prefs;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.IshiDialogTheme);
        builder.setTitle(tittle)
                .setSingleChoiceItems(audio, prefs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        audioChoice = which;
                    }
                })
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setAudioPreferences(audio.length, audioChoice);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setAudioPreferences(audio.length, prefs);
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setAudioPreferences(int length, int audioPrefs) {
        if (length == 8) {
            if (audioPrefs == 2 && myPreferences.isErrorInUpLink()) {
                showPreviousError("Voice UpLink", 2);
            } else if (audioPrefs == 3 && myPreferences.isErrorInDownLink()) {
                showPreviousError("Voice DownLink", 3);
            } else if (audioPrefs == 4 && myPreferences.isErrorInVoiceCalls()) {
                showPreviousError("Voice Calls", 4);
            } else if (audioPrefs == 5 && myPreferences.isErrorInCamCorder()) {
                showPreviousError("CamCorder", 5);
            } else if (audioPrefs == 6 && myPreferences.isErrorInVoiceRecognition()) {
                showPreviousError("Voice Recognition", 6);
            } else if (audioPrefs == 7 && myPreferences.isErrorInVoiceCommunnication()) {
                showPreviousError("Voice Communication", 7);
            } else {
                myPreferences.setAudioSource(audioPrefs);
                audioSource.setText(ConstantsValue.audioSource[audioPrefs]);
            }
        } else if (length == 2) {
            myPreferences.setAudioChannel(audioPrefs);
            audioChannel.setText(ConstantsValue.audioChannel[audioPrefs]);
        } else {
            myPreferences.setAudioFormat(audioPrefs);
            audioFormat.setText(ConstantsValue.audioFormat[audioPrefs]);
        }
    }

    protected void showPreviousError(String audioSourceName, final int audioSourceChoice) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.IshiDialogTheme);
        alertDialog.setTitle("Alert");
        alertDialog
                .setMessage("The last attempt to record call from "
                        + audioSourceName
                        + " audio source failed. This can be happened one of two reasons: either recording with "
                        + audioSourceName
                        + " audio source is not supported by your device or other application is recording at the same time. Do you want to try "
                        + audioSourceName + " again?");
        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myPreferences.setAudioSource(audioSourceChoice);
                        audioSource.setText(ConstantsValue.audioSource[audioSourceChoice]);
                        if (audioSourceChoice == 2) {
                            myPreferences.setErrorInUpLink(false);
                        } else if (audioSourceChoice == 3) {
                            myPreferences.setErrorInDownLink(false);
                        } else if (audioSourceChoice == 4) {
                            myPreferences.setErrorInVoiceCalls(false);
                        } else if (audioSourceChoice == 5) {
                            myPreferences.setErrorInCamCorder(false);
                        } else if (audioSourceChoice == 6) {
                            myPreferences.setErrorInVoiceRecognition(false);
                        } else if (audioSourceChoice == 7) {
                            myPreferences.setErrorInVoiceCommunnication(false);
                        }
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == recordingEnable) {
            ComponentName componentName = new ComponentName(getActivity(),
                    CallReceiverBroadcast.class);
            PackageManager packageManager = getActivity().getPackageManager();
            if (isChecked) {
                myPreferences.setRecordingEnable(true);
                packageManager.setComponentEnabledSetting(componentName,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
            } else {
                myPreferences.setRecordingEnable(false);
                packageManager.setComponentEnabledSetting(componentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }
        }
        if (buttonView == autoSpeaker) {
            myPreferences.setAutoSpeakerEnable(isChecked);
        }
        if (buttonView == autoMaxVolume) {
            myPreferences.setMaxRecordingVolume(isChecked);
        }
        if (buttonView == passwordProtection) {
            if (TextUtils.isEmpty(myPreferences.getPassword()) || TextUtils.isEmpty(myPreferences.getSecurityAnswer())) {
                passwordProtection.setChecked(false);
                Toast.makeText(getActivity(), "There is no password or security question created yet. Create a password and security question first.", Toast.LENGTH_LONG).show();
            } else {
                myPreferences.setPasswordProtected(isChecked);
            }
        }
    }
}
