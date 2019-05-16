package com.tech.nyax.myapplication10;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class boot_complete_broadcast_receiver extends BroadcastReceiver {

    private final static String TAG = boot_complete_broadcast_receiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        //Your implementation goes here.

        try {

            String action = intent.getAction();

            if (action != null) {
                if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
                    // TO-DO: Code to handle BOOT COMPLETED EVENT
                    // TO-DO: I can start an service.. display a notification... start an activity

                    final Intent _splashactivity = new Intent(context, splashactivity.class);
                    context.startActivity(_splashactivity);

                    startService(context);

                    startJob(context);

                }
            }

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            utilz.getInstance(context).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    public void startService(Context context) {
        // Start service
        Intent intent = new Intent(context, singleton_Service.class);
        ComponentName _service_component = context.startService(intent);

        if (singleton_Service.isRunning) {
            utilz.getInstance(context).globalloghandler("service running...", TAG, 1, 1);
        }
    }

    public void StopService(Context context) {
        if (singleton_Service.isRunning) {
            // Stop service
            Intent intent = new Intent(context, singleton_Service.class);
            boolean _is_service_stopped = context.stopService(intent);
        }
    }

    public void makeServiceDoSomething() {
        if (singleton_Service.isRunning)
            singleton_Service.instance.start_timer();
    }

    public void startJob(Context context) {
        // get the jobScheduler instance from current context
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        // MyJobService provides the implementation for the job
        ComponentName jobService = new ComponentName(context, job_schedule.class);
        // define that the job will run periodically in intervals of 10 seconds
        // Besides periodic jobs, JobInfo.Builder allows to specify many other settings and constraints. For example you can define that a plugged in charger or a network connection is required to run the job.
        JobInfo jobInfo = new JobInfo.Builder(1, jobService).setPeriodic(10 * 1000).build();
        // schedule/start the job
        int result = jobScheduler.schedule(jobInfo);
        if (result == JobScheduler.RESULT_SUCCESS)
            Log.e(TAG, "Successfully scheduled job: " + result);
        else
            Log.e(TAG, "RESULT_FAILURE: " + result);
    }

    public void stopJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        Log.e(TAG, "Stopping all jobs...");
        jobScheduler.cancelAll(); // cancel all potentially running jobs
    }

}














