package com.grt.callrecorder.logics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.grt.callrecorder.storage.MyPreferences;


public class CallReceiverBroadcast extends BroadcastReceiver {

    private MyPreferences myPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        myPreferences = new MyPreferences(context);
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            myPreferences.setCallerNumber(intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
            myPreferences.setCallType("outgoing");
        }

        if (intent.getAction().equalsIgnoreCase("android.intent.action.PHONE_STATE")) {
            Intent intentService = new Intent(context, CallRecordingService.class);
            int state = telephonyManager.getCallState();
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    context.stopService(intentService);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    context.startService(intentService);
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    myPreferences.setCallerNumber(intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER));
                    myPreferences.setCallType("incoming");
                    break;
            }
        }

    }
}
