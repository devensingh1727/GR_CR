package com.grt.callrecorder.adapterClasses;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.grt.callrecorder.R;
import com.grt.callrecorder.modelClasses.RecordingsItemPojo;
import com.grt.callrecorder.utilities.MultipleItemSelectionObserver;
import com.grt.callrecorder.utilities.OnViewClickListner;

import java.util.ArrayList;

/**
 * Created by Deven on 21-09-2015.
 */
public class RecordingsAdapter extends RecyclerView.Adapter<RecordingsRowHolder> {

    private ArrayList<RecordingsItemPojo> myRecordingListItems;
    private OnViewClickListner onViewClickListner;
    private MultipleItemSelectionObserver itemSelectionObserver;

    public RecordingsAdapter(ArrayList<RecordingsItemPojo> myRecordingListItems, OnViewClickListner onViewClickListner,MultipleItemSelectionObserver itemSelectionObserver) {
        this.myRecordingListItems = myRecordingListItems;
        this.onViewClickListner = onViewClickListner;
        this.itemSelectionObserver=itemSelectionObserver;
    }

    @Override
    public RecordingsRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recordings_item, parent, false);
        RecordingsRowHolder recordingsRowHolder = new RecordingsRowHolder(v);
        return recordingsRowHolder;
    }

    @Override
    public void onBindViewHolder(final RecordingsRowHolder holder, final int position) {
        final RecordingsItemPojo recordingsItemPojo = myRecordingListItems.get(position);
        holder.callerName.setText("" + recordingsItemPojo.getCallerName());
        holder.audioFormat.setText("" + recordingsItemPojo.getFormat());
        holder.audioSize.setText("" + recordingsItemPojo.getFileSize());
        holder.timing.setText("" + recordingsItemPojo.getTiming());
        if(isAnySelected()){
            holder.actionView.setVisibility(View.GONE);
        }else {
            holder.actionView.setVisibility(View.VISIBLE);
        }
        if (recordingsItemPojo.isSelected()) {
            holder.callType.setImageResource(R.mipmap.ic_check_mark);
            holder.callType.setBackgroundResource(R.drawable.drawable_check_mark);
        } else {
            if (recordingsItemPojo.getCallType().equalsIgnoreCase("outgoing")) {
                holder.callType.setImageResource(R.mipmap.ic_outgoing);
                holder.callType.setBackgroundResource(R.drawable.call_outgoing);
            } else {
                holder.callType.setImageResource(R.mipmap.ic_incoming);
                holder.callType.setBackgroundResource(R.drawable.call_incoming);
            }
        }
        holder.actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("position view ", ""+position);
                onViewClickListner.setOnViewClickListner(holder.actionView, recordingsItemPojo.getRecordingFiles(), position);
            }
        });
        holder.llRecordings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnySelected()) {
                    changeSelection(position);
                } else {
                    onViewClickListner.setOnViewClickListner(holder.llRecordings, recordingsItemPojo.getRecordingFiles(),position);
                }
            }
        });
        holder.llRecordings.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isAnySelected()) {
                    changeSelection(position);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != myRecordingListItems ? myRecordingListItems.size() : 0);
    }

    public void changeSelection(int position) {
        if (myRecordingListItems.get(position).isSelected()) {
            myRecordingListItems.get(position).setSelected(false);
        } else {
            myRecordingListItems.get(position).setSelected(true);
        }
        itemSelectionObserver.isMultipleItemSelected(isAnySelected());
        notifyDataSetChanged();
    }

    public boolean isAnySelected() {
        boolean isAnySelected = false;

        for (int i = 0; i < myRecordingListItems.size(); i++) {
            if (myRecordingListItems.get(i).isSelected()) {
                isAnySelected = true;
                break;
            }
        }

        return isAnySelected;
    }

    public boolean isAtleastTwoSelected() {
        boolean isAtleastTwoSelected = false;
        int count = 0;

        for (int i = 0; i < myRecordingListItems.size(); i++) {
            if (myRecordingListItems.get(i).isSelected()) {
                count++;
                if (count >= 2) {
                    isAtleastTwoSelected = true;
                    break;
                }
            }
        }

        return isAtleastTwoSelected;
    }

    public void selectAllRecordings(){
        for (int i = 0; i < myRecordingListItems.size(); i++) {
            if (myRecordingListItems.get(i).isSelected()) {
            } else {
                myRecordingListItems.get(i).setSelected(true);
            }
        }
        itemSelectionObserver.isMultipleItemSelected(isAnySelected());
        notifyDataSetChanged();
    }

    public void selectNoneRecordings(){
        for (int i = 0; i < myRecordingListItems.size(); i++) {
            if (myRecordingListItems.get(i).isSelected()) {
                myRecordingListItems.get(i).setSelected(false);
            } else {
            }
        }
        itemSelectionObserver.isMultipleItemSelected(isAnySelected());
        notifyDataSetChanged();
    }

    public void selectInverseRecordings(){
        for (int i = 0; i < myRecordingListItems.size(); i++) {
            if (myRecordingListItems.get(i).isSelected()) {
                myRecordingListItems.get(i).setSelected(false);
            } else {
                myRecordingListItems.get(i).setSelected(true);
            }
        }
        itemSelectionObserver.isMultipleItemSelected(isAnySelected());
        notifyDataSetChanged();
    }

    public int countSelected() {
        int count = 0;

        for (int i = 0; i < myRecordingListItems.size(); i++) {
            if (myRecordingListItems.get(i).isSelected()) {
                count++;
            }
        }

        return count;
    }

    public ArrayList<String> getSelectedRecordings() {
        ArrayList<String> selectedRecordings = new ArrayList<String>();

        for (int i = 0; i < myRecordingListItems.size(); i++) {
            if (myRecordingListItems.get(i).isSelected()) {
                selectedRecordings.add(i+"_pos_"+myRecordingListItems.get(i).getRecordingFiles());
            }
        }

        return selectedRecordings;
    }
}
