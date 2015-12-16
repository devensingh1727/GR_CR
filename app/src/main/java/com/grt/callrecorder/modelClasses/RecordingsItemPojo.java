package com.grt.callrecorder.modelClasses;

/**
 * Created by DEVEN SINGH on 5/15/2015.
 */
public class RecordingsItemPojo {

    private String callerName="";
    private String callType="";
    private String format="";
    private String fileSize="";
    private String timing="";
    private String recordingFiles;
    public boolean isSelected=false;

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getRecordingFiles() {
        return recordingFiles;
    }

    public void setRecordingFiles(String recordingFiles) {
        this.recordingFiles = recordingFiles;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
