package com.grt.callrecorder.utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.grt.callrecorder.R;
import com.grt.callrecorder.userInterface.WelcomeActivity;


public class RecordingNotification {

    Context context;
    private final int NOTIFICATION_ID = 1727;

    public RecordingNotification(Context context) {
        this.context = context;
    }

    public void showNotification() {
        Intent intent = new Intent(context, WelcomeActivity.class);
        intent.putExtra("notificationId", NOTIFICATION_ID);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Intent cancel = new Intent(context, NotificationCancelReceiver.class);
        cancel.setAction("com.grt.callrecorder.CANCEL_NOTIFICATION");
        cancel.putExtra("notificationId", NOTIFICATION_ID);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, 0, cancel, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder notificationBuilder;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher);
        } else {
            notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_notification_icon)
                    .setColor(context.getResources().getColor(R.color.accent));
        }
        notificationBuilder.setContentTitle("Call Recorder")
                .setContentText("You have new recording!")
                .setContentIntent(null)
                .setPriority(Notification.PRIORITY_MAX)
                .addAction(R.mipmap.ic_view_list, "View", pendingIntent)
                .addAction(R.mipmap.ic_close, "Close", cancelPendingIntent)
                .setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }
}
