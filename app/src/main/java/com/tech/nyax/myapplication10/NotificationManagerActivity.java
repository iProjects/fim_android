package com.tech.nyax.myapplication10;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationManagerActivity {

    private static NotificationManagerActivity singleInstance;
    private final Context context;
    private ArrayList<String> _messages = new ArrayList<>();
    Bundle _bundle = new Bundle();

    public NotificationManagerActivity(Context context) {
        this.context = context;
    }

    public static NotificationManagerActivity getInstance(Context context) {
        if (singleInstance == null)
            singleInstance = new NotificationManagerActivity(context);
        return singleInstance;
    }

    public void shownotificationmsg(String message, String title, String ticker, String tag) {

//        String intenttocall = tag + ".class";
//
//        LoggerSingleton.getInstance(context).logtotoast(intenttocall, 1);
//        LoggerSingleton.getInstance(context).logtologcat(intenttocall, 1, tag);

        // Tapping the Notification will open up MainActivity
        Intent intent = null;
//        try {
////            intent = new Intent(context, Class.forName(tag));
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            LoggerSingleton.getInstance(context).logtotoast(e.toString(), 1);
//        }

        intent = new Intent(context, msgutilsactivity.class);

        // an action to use later defined as an app constant:
        // public static final String MESSAGE_CONSTANT = "com.tech.nyax.myapplication10";
        intent.setAction(msgutilsactivity.MESSAGE_CONSTANT);
        // you can use extras as well

        _messages.add(message);
        _bundle.putStringArrayList("messages", _messages);

        intent.putExtra("message", message);

        intent.putExtra("messages", _bundle);

        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        String requestcodetimeStamp = new SimpleDateFormat("ddHHmmss", Locale.getDefault()).format(new Date());
        int intrequestcodetimeStamp = Integer.valueOf(requestcodetimeStamp);

        // Create Pending Intent
        PendingIntent notificationIntent = PendingIntent.getActivity(context, intrequestcodetimeStamp, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentIntent(notificationIntent);
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.eclipse));
        builder.setSmallIcon(R.drawable.eclipse);
        builder.setContentText(message);
        builder.setTicker(ticker);
        builder.setContentTitle(title);
// set high priority for Heads Up Notification
        /*Different Priority Levels Info:
        PRIORITY_MAX -- Use for critical and urgent notifications that alert the user to a condition that is time-critical or
        needs to be resolved before they can continue with a particular task.
                PRIORITY_HIGH -- Use primarily for important communication, such as message or chat events with content that is
        particularly interesting for the user. High-priority notifications trigger the heads-up notification display.
        PRIORITY_DEFAULT -- Use for all notifications that don't fall into any of the other priorities described here.
        PRIORITY_LOW -- Use for notifications that you want the user to be informed about, but that are less urgent. Lowpriority
        notifications tend to show up at the bottom of the list, which makes them a good choice for things like
        public or undirected social updates: The user has asked to be notified about them, but these notifications should
        never take precedence over urgent or direct communication.
        PRIORITY_MIN -- Use for contextual or background information such as weather information or contextual location
        information. Minimum-priority notifications do not appear in the status bar. The user discovers them on expanding
        the notification shade.*/
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setWhen(System.currentTimeMillis());
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 1, intent, 0);
        builder.addAction(R.drawable.eclipse, context.getResources().getString(R.string.notification_action_name), pIntent);
        builder.setUsesChronometer(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setColor(context.getResources().getColor(R.color.custom_greenish));

// Notify using NotificationManager
// It won't show "Heads Up" unless it plays a sound
        if (Build.VERSION.SDK_INT >= 21) builder.setVibrate(new long[0]);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(intrequestcodetimeStamp, builder.build());
    }
}
