package com.grt.callrecorder.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Deven on 08-09-2015.
 */
public class MyPreferences {

    private final String DB_NAME = "GrCr";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;


    private final String RECORDING_ACTIVATOR = "recordingActivator";
    private final String SPEAKER_ACTIVATOR = "speakerActivator";
    private final String MAX_RECORDING_VOLUME = "maxRecordingVolume";
    private final String PASSWORD_PROTECTION = "passwordProtection";
    private final String AUDIO_SOURCE = "audioSource";
    private final String AUDIO_CHANNEL = "audioChannel";
    private final String AUDIO_FORMAT = "audioFormat";
    private final String PASSWORD = "password";
    private final String SECURITY_QUESTION = "securityQuestion";
    private final String SECURITY_ANSWER = "securityAnswer";
    private final String ERROR_VOICE_CALLS = "errorVoiceCalls";
    private final String ERROR_DOWN_LINK = "errorDownLink";
    private final String ERROR_UP_LINK = "errorUpLink";
    private final String ERROR_CAM_CORDER = "errorCamCorder";
    private final String ERROR_VOICE_RECOGNITION = "errorVoiceRecognition";
    private final String ERROR_VOICE_COMMUNICATION = "errorVoiceCommunication";
    private final String CALLER_NUMBER = "callerNumber";
    private final String CALL_TYPE = "callType";

    public MyPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(DB_NAME,Context.MODE_PRIVATE);
    }

    public boolean isErrorInVoiceCommunnication() {
        return sharedPreferences.getBoolean(ERROR_VOICE_COMMUNICATION,false);
    }

    public void setErrorInVoiceCommunnication(boolean errorInVoiceCommunnication) {
        spEditor=sharedPreferences.edit();
        spEditor.putBoolean(ERROR_VOICE_COMMUNICATION,errorInVoiceCommunnication);
        spEditor.commit();
    }

    public boolean isErrorInVoiceRecognition() {
        return sharedPreferences.getBoolean(ERROR_VOICE_RECOGNITION,false);
    }

    public void setErrorInVoiceRecognition(boolean errorInVoiceRecognition) {
        spEditor=sharedPreferences.edit();
        spEditor.putBoolean(ERROR_VOICE_RECOGNITION,errorInVoiceRecognition);
        spEditor.commit();
    }

    public boolean isErrorInCamCorder() {
        return sharedPreferences.getBoolean(ERROR_CAM_CORDER, false);
    }

    public void setErrorInCamCorder(boolean errorInCamCorder) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(ERROR_CAM_CORDER, errorInCamCorder);
        spEditor.commit();
    }

    public boolean isErrorInUpLink() {
        return sharedPreferences.getBoolean(ERROR_UP_LINK, false);
    }

    public void setErrorInUpLink(boolean errorInUpLink) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(ERROR_UP_LINK, errorInUpLink);
        spEditor.commit();
    }

    public boolean isErrorInDownLink() {
        return sharedPreferences.getBoolean(ERROR_DOWN_LINK, false);
    }

    public void setErrorInDownLink(boolean errorInDownLink) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(ERROR_DOWN_LINK, errorInDownLink);
        spEditor.commit();
    }

    public boolean isErrorInVoiceCalls() {
        return sharedPreferences.getBoolean(ERROR_VOICE_CALLS, false);
    }

    public void setErrorInVoiceCalls(boolean errorInVoiceCalls) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(ERROR_VOICE_CALLS, errorInVoiceCalls);
        spEditor.commit();
    }

    public String getSecurityAnswer() {
        return sharedPreferences.getString(SECURITY_ANSWER, "");
    }

    public void setSecurityAnswer(String securityAnswer) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(SECURITY_ANSWER, securityAnswer);
        spEditor.commit();
    }

    public String getSecurityQuestion() {
        return sharedPreferences.getString(SECURITY_QUESTION, "");
    }

    public void setSecurityQuestion(String securityQuestion) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(SECURITY_QUESTION, securityQuestion);
        spEditor.commit();
    }

    public String getPassword() {
        return sharedPreferences.getString(PASSWORD, "");
    }

    public void setPassword(String password) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(PASSWORD, password);
        spEditor.commit();
    }

    public int getAudioFormat() {
        return sharedPreferences.getInt(AUDIO_FORMAT, 2);
    }

    public void setAudioFormat(int audioFormat) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(AUDIO_FORMAT, audioFormat);
        spEditor.commit();
    }

    public int getAudioChannel() {
        return sharedPreferences.getInt(AUDIO_CHANNEL, 0);
    }

    public void setAudioChannel(int audioChannel) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(AUDIO_CHANNEL, audioChannel);
        spEditor.commit();
    }

    public int getAudioSource() {
        return sharedPreferences.getInt(AUDIO_SOURCE, 2);
    }

    public void setAudioSource(int audioSource) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(AUDIO_SOURCE, audioSource);
        spEditor.commit();
    }

    public boolean isPasswordProtected() {
        return sharedPreferences.getBoolean(PASSWORD_PROTECTION, false);
    }

    public void setPasswordProtected(boolean passwordProtected) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(PASSWORD_PROTECTION, passwordProtected);
        spEditor.commit();
    }

    public boolean isMaxRecordingVolume() {
        return sharedPreferences.getBoolean(MAX_RECORDING_VOLUME,true);
    }

    public void setMaxRecordingVolume(boolean maxRecordingVolume) {
        spEditor=sharedPreferences.edit();
        spEditor.putBoolean(MAX_RECORDING_VOLUME,maxRecordingVolume);
        spEditor.commit();
    }

    public boolean isAutoSpeakerEnable() {
        return sharedPreferences.getBoolean(SPEAKER_ACTIVATOR, false);
    }

    public void setAutoSpeakerEnable(boolean autoSpeakerEnable) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(SPEAKER_ACTIVATOR, autoSpeakerEnable);
        spEditor.commit();
    }

    public boolean isRecordingEnable() {
        return sharedPreferences.getBoolean(RECORDING_ACTIVATOR, true);
    }

    public void setRecordingEnable(boolean recordingEnable) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(RECORDING_ACTIVATOR, recordingEnable);
        spEditor.commit();
    }

    public String getCallType() {
        return sharedPreferences.getString(CALL_TYPE,"");
    }

    public void setCallType(String callType) {
        spEditor=sharedPreferences.edit();
        spEditor.putString(CALL_TYPE,callType);
        spEditor.commit();
    }

    public String getCallerNumber() {
        return sharedPreferences.getString(CALLER_NUMBER,"");
    }

    public void setCallerNumber(String callerNumber) {
        spEditor=sharedPreferences.edit();
        spEditor.putString(CALLER_NUMBER,callerNumber);
        spEditor.commit();
    }
}
