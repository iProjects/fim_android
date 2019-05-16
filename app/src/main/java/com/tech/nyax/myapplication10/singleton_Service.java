package com.tech.nyax.myapplication10;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.os.Handler;
import android.widget.Toast;

public class singleton_Service extends Service {
    
    private final static String TAG = singleton_Service.class.getSimpleName();
    
	private int NOTIFICATION = 1; // Unique identifier for our notification
    public static boolean isRunning = false;
    public static singleton_Service instance = null;
    private NotificationManager notificationManager = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        instance = this;
        isRunning = true;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher) // the status icon
                .setTicker("Service running...") // the status text
                .setWhen(System.currentTimeMillis()) // the time stamp
                .setContentTitle("My App") // the label of the entry
                .setContentText("Service running...") // the content of the entry
                .setContentIntent(contentIntent) // the intent to send when the entry is clicked
                .setOngoing(true) // make persistent (disable swipe-away)
                .build();
        // Start service in foreground mode
        startForeground(NOTIFICATION, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        instance = null;
        notificationManager.cancel(NOTIFICATION); // Remove notification
        super.onDestroy();
    }

    public void doSomething() {
        Toast.makeText(getApplicationContext(), "Doing stuff from service...", Toast.LENGTH_SHORT).show();
    }

    public void start_timer() {

        try {

            // run "do abc" after 10s. It same as timer, thread...
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // do abc long 5s or so
                    utilz.getInstance(getApplicationContext()).globalloghandler("run after 5 seconds.", TAG, 1, 1);
                }
            }, 5000);

            //This code will print "Hello" every second.
            Timer timer = new Timer(new Runnable() {
                public void run() {
                    System.out.println("Hello");
                    utilz.getInstance(getApplicationContext()).globalloghandler("print Hello every 10 seconds.", TAG, 1, 1);
                }
            }, 10000, true);

            //Executing code after 1.5 seconds:
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //The code you want to run after the time is up
                    utilz.getInstance(getApplicationContext()).globalloghandler("Executing code after 15 seconds.", TAG, 1, 1);
                }
            }, 15000); //the time you want to delay in milliseconds

            //Executing code repeatedly every 1 second:
            final Handler _handler = new Handler();
            _handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    utilz.getInstance(getApplicationContext()).globalloghandler("Executing code repeatedly every 20 second.", TAG, 1, 1);
                    _handler.postDelayed(this, 20000);
                }
            }, 20000); //the time you want to delay in milliseconds

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    public class Timer {
        private Handler handler;
        private boolean paused;
        private int interval;
        private Runnable task = new Runnable() {
            @Override
            public void run() {
                if (!paused) {
                    runnable.run();
                    Timer.this.handler.postDelayed(this, interval);
                }
            }
        };

        private Runnable runnable;

        public int getInterval() {
            return interval;
        }

        public void setInterval(int interval) {
            this.interval = interval;
        }

        public void startTimer() {
            paused = false;
            handler.postDelayed(task, interval);
        }

        public void stopTimer() {
            paused = true;
        }

        public Timer(Runnable runnable, int interval, boolean started) {
            handler = new Handler();
            this.runnable = runnable;
            this.interval = interval;
            if (started)
                startTimer();
        }
    }

}