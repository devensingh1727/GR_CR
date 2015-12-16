package com.grt.callrecorder.adapterClasses;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grt.callrecorder.R;


/**
 * Created by Deven on 21-09-2015.
 */
public class RecordingsRowHolder extends RecyclerView.ViewHolder {

    protected ImageView callType;
    protected TextView callerName;
    protected TextView audioFormat;
    protected TextView audioSize;
    protected TextView timing;
    protected ImageView actionView;
    protected LinearLayout llRecordings;

    public RecordingsRowHolder(View itemView) {
        super(itemView);
        callerName = (TextView) itemView.findViewById(R.id.callerName);
        callType = (ImageView) itemView.findViewById(R.id.call_img);
        audioFormat = (TextView) itemView.findViewById(R.id.recording_formate);
        audioSize = (TextView) itemView.findViewById(R.id.recording_size);
        timing = (TextView) itemView.findViewById(R.id.recording_time);
        actionView = (ImageView) itemView.findViewById(R.id.more_option);
        llRecordings= (LinearLayout) itemView.findViewById(R.id.ll_recordings);
    }
}
