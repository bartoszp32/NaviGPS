package com.navigps.receivers;

import com.navigps.MenuActivity;
import com.navigps.R;
import com.navigps.providers.PreferencesProvider;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {
    Context context;
    private NotificationManager manager;
    public static final String ACTION_NOTIFICATION_CHANGED = "action_notification_changed";
    private PreferencesProvider preferencesProvider;
    public NotificationReceiver(Context context) {
        this.context = context;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        preferencesProvider = new PreferencesProvider(context);
    }
    public static IntentFilter getIntentFilter()   {
        return new IntentFilter(ACTION_NOTIFICATION_CHANGED);
    }
    public static Intent sendIntent() {
        return new Intent(ACTION_NOTIFICATION_CHANGED);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
               if(ACTION_NOTIFICATION_CHANGED.equals(intent.getAction())) {
                if(preferencesProvider.getNotification()) {
                    startNotification();
                } else {
                    deleteNotification();
                }
               }
    }
    public void startNotification() {
        String title = context.getString(R.string.app_name);
        String text = "Us³uga programu uruchomiona.";//context.getString(R.string.notification_text);
        NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(text)
                .setOngoing(true)
                .setTicker(text)
                ;
        manager.notify(1222,mBuilder.build());

    }
    public void deleteNotification() {
        try {
            manager.cancel(1222);
        } catch (Exception ignored){}
    }
}