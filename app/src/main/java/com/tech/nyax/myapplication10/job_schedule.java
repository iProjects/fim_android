package com.tech.nyax.myapplication10;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.util.Log;

public class job_schedule extends JobService {
    private final static String TAG = job_schedule.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e(TAG, "Job started");
        utilz.getInstance(getApplicationContext()).globalloghandler("Job started...", TAG, 1, 1);

        // ... your code here ...

        // signal that we're done and don't  want to reschedule the job
        // finished: no more work to be done
        jobFinished(jobParameters, false);

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.e(TAG, "Job stopped");
        return false;
    }
}










