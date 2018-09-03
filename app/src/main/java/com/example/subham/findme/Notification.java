package com.example.subham.findme;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

public class Notification extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Vibrator V = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        V.vibrate(1000);

        android.app.Notification notification = new android.app.Notification.Builder(context)
                .setContentTitle("Found You !")
                .setSmallIcon(R.drawable.mapsicon)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0,notification);
    }
}
