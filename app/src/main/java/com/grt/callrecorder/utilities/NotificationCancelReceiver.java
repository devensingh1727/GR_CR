package com.grt.callrecorder.utilities;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Deven on 08-10-2015.
 */
public class NotificationCancelReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase("com.grt.callrecorder.CANCEL_NOTIFICATION")) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(intent.getExtras().getInt("notificationId"));
        }
    }
}
