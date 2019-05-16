package com.tech.nyax.myapplication10;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.ProgressBar;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

public class splashactivity extends AppCompatActivity {

    SpannableStringBuilder _strbuilder = new SpannableStringBuilder();
    Animation anim;
    ImageView imgsplashloader;
    TextView lblsplashprogressinfo, lblcounta;
    public final int SPLASH_DISPLAY_LENGTH = 10000;
    private final static String TAG = splashactivity.class.getSimpleName();
    private static final long TIMER_DURATION = 10000L;//60 seconds
    private static final long TIMER_INTERVAL = 1L;//tick every 1 millisecond
    private CountDownTimer mCountDownTimer;
    private TextView lbltimer;
    private long mTimeRemaining;
    long counta;
    private ProgressBar progressBarsplash;
    // Unique request code
    public static final int MULTIPLE_PERMISSIONS = 20;
    List<String> permissions = new ArrayList<>();
    private Drawable[] backgroundsDrawableArrayForTransition;
    private TransitionDrawable transitionDrawable;
    private static final int TRANSITION_DRAWABLE_INTERVAL = 2; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity_layout);

        Drawable background = getApplicationContext().getDrawable(R.drawable.background);

        background.setTint(getResources().getColor(R.color.blue_background));

        //Load the untinted resource
        final Drawable drawableRes = ContextCompat.getDrawable(getApplicationContext(), R.drawable.background);

        //Wrap it with the compatibility library so it can be altered
        Drawable tintedDrawable = DrawableCompat.wrap(drawableRes);

        //Apply a coloured tint
        DrawableCompat.setTint(tintedDrawable, getResources().getColor(R.color.blue_background));
        //At this point you may use the tintedDrawable just as you usually would
        //(and drawableRes can be discarded)

        progressBarsplash = findViewById(R.id.progressBarsplash);
        progressBarsplash.setVisibility(View.VISIBLE);
        //An indeterminate ProgressBar shows a cyclic animation without an indication of progress. Basic indeterminate ProgressBar (spinning wheel)
        progressBarsplash.setIndeterminate(true);
		/* progressBarsplash.setCancelable(true);
		progressBarsplash.setCanceledOnTouchOutside(true); */

        imgsplashloader = findViewById(R.id.imgsplashloader);

        lblsplashprogressinfo = findViewById(R.id.lblsplashprogressinfo);
        lblcounta = findViewById(R.id.lblcounta);
        lbltimer = findViewById(R.id.lbltimer);

        lbltimer.setText("");
        lblsplashprogressinfo.setText("");
        lblcounta.setText("");

		loginfo("LAUNCHING APP...\n");
		 
		loginfo("checking Permissions...\n");
					
		//backgroundAnimTransAction();

		//animation_shake_splash_loader();

		animation_fade_out_splash_loader();
		
		count_down_timer();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //request permissions. NOTE: Copying this and the manifest will cause the app to crash as the permissions requested aren't defined in the manifest.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { 
                    checkPermission();
                } else {
                    checkPermission();
                }

                RuntimePermission();

                loginfo("\nEnumerating NetworkInterface for HostAddress...\n");
                getIpAddress();

                launchmainactivity();

            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void count_down_timer() {
         
        mCountDownTimer = new CountDownTimer(TIMER_DURATION, TIMER_INTERVAL) {

			Resources resources = getResources();	
			TypedArray icons = resources.obtainTypedArray(R.array.splash_images);	 
			int _repeat_count = 0;

            @Override
            public void onTick(long millisUntilFinished) {

                lbltimer.setText(String.format(Locale.getDefault(), "%d sec.", millisUntilFinished / 1000L));

                // Saving timeRemaining in Activity for pause/resume of CountDownTimer.
                mTimeRemaining = millisUntilFinished;

                counta++;

                lblcounta.setText(String.format("%d", counta));

                Log.e(TAG, String.format("%d", counta));

                _repeat_count++;
				
                Log.e(TAG, String.format("%d", _repeat_count));

				if(_repeat_count >= icons.length())_repeat_count = 0;
				
				Drawable _drawable = icons.getDrawable(_repeat_count);
				
				imgsplashloader.setImageDrawable(_drawable);
            }

            @Override
            public void onFinish() {
                lbltimer.setText("Done.");
            }

        }.start();
 
    }

    private void animation_fade_out_splash_loader() {
        
        // Declare an imgsplashloader to show the animation.
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shrink_fadeout_from_bottom);

        // Create the animation.
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
              
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
				
            }
        });

        imgsplashloader.startAnimation(anim);
  
    }

    private void animation_shake_splash_loader() {
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shakeanimation);
         
        // Create the animation.
        shake.setAnimationListener(new Animation.AnimationListener() {
			
			Resources resources = getResources();	
			TypedArray icons = resources.obtainTypedArray(R.array.splash_images);	 
			int _repeat_count = 0;

            @Override
            public void onAnimationStart(Animation animation) {
				@SuppressWarnings("ResourceType")
				Drawable _drawable = icons.getDrawable(0);
				imgsplashloader.setImageDrawable(_drawable);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
				_repeat_count++;
				if(_repeat_count >= icons.length())_repeat_count = 0;
				Drawable _drawable = icons.getDrawable(_repeat_count);
				imgsplashloader.setImageDrawable(_drawable);
            }
        });

        imgsplashloader.startAnimation(shake);
 
    }

    private void backgroundAnimTransAction() {
        // set res image array
        Resources resources = getResources();
        TypedArray icons = resources.obtainTypedArray(R.array.splash_images);

        @SuppressWarnings("ResourceType")
        Drawable drawable1 = icons.getDrawable(0);

        @SuppressWarnings("ResourceType")
        Drawable drawable2 = icons.getDrawable(1);

        @SuppressWarnings("ResourceType")
        Drawable drawable3 = icons.getDrawable(2);

        @SuppressWarnings("ResourceType")
        Drawable drawable4 = icons.getDrawable(3);

        backgroundsDrawableArrayForTransition = new Drawable[4];
        backgroundsDrawableArrayForTransition[0] = drawable1;
        backgroundsDrawableArrayForTransition[1] = drawable2;
        backgroundsDrawableArrayForTransition[2] = drawable3;
        backgroundsDrawableArrayForTransition[3] = drawable4;

        transitionDrawable = new TransitionDrawable(backgroundsDrawableArrayForTransition);

        // your image view here
        imgsplashloader.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(TRANSITION_DRAWABLE_INTERVAL);
        transitionDrawable.setCrossFadeEnabled(false);
    }

    @Override
    protected void onResume() {
        
        if (mCountDownTimer == null) { // Timer was paused, re-create with saved time.
            mCountDownTimer = new CountDownTimer(mTimeRemaining, TIMER_INTERVAL) {

                @Override
                public void onTick(long millisUntilFinished) {
                    lbltimer.setText(String.format(Locale.getDefault(), "%d sec.", millisUntilFinished
                            / 1000L));
                    mTimeRemaining = millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    lbltimer.setText("Done.");
                }
            }.start();
        }
		
		super.onResume();
    }

    @Override
    protected void onPause() {
        mCountDownTimer.cancel();
        mCountDownTimer = null;
        super.onPause();
    }

    @Override
    protected void onDestroy() { 
        super.onDestroy();
    }

    void launchmainactivity() {

        utilz.getInstance(getApplicationContext()).globalloghandler("launching MainActivity", TAG, 1, 1);
		
		/* Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); */
        //startActivity(intent);
        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
        // MainActivity.class is the activity to go after showing the splash screen.

        //after three seconds, it will execute all of this code.
        //as such, we then want to redirect to the master-activity
        Intent mainIntent = new Intent(splashactivity.this, MainActivity.class);
        splashactivity.this.startActivity(mainIntent);
        //then we finish this class. Dispose of it as it is longer needed
        splashactivity.this.finish();

        utilz.getInstance(getApplicationContext()).globalloghandler("launching searchutilsactivity...", TAG, 1, 1);

        final Intent searchutilsactivity = new Intent(splashactivity.this, searchutilsactivity.class);
        splashactivity.this.startActivity(searchutilsactivity);
        splashactivity.this.finish();

    }

    //method for getting local ip
    private String getIpAddress() {
        try {
            String ip = "";

            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();

            while (enumNetworkInterfaces.hasMoreElements()) {

                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();

                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();

                while (enumInetAddress.hasMoreElements()) {

                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += inetAddress.getHostAddress() + "\n";
                    }
                }
            }
 
            loginfo(ip);
            return ip;

        } catch (Exception ex) { 
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            return ex.toString();
        }
    }

    private void checkPermission() {
        try {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                logwarning("INTERNET PERMISSION NOT GRANTED\n");
                permissions.add(Manifest.permission.INTERNET);
            } else {
                //Permission granted
                loginfo("INTERNET PERMISSION GRANTED\n");
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                logwarning("WAKE_LOCK PERMISSION NOT GRANTED\n");
                permissions.add(Manifest.permission.WAKE_LOCK);
            } else {
                //Permission granted
                loginfo("WAKE_LOCK PERMISSION GRANTED\n");
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                logwarning("ACCESS_NETWORK_STATE PERMISSION NOT GRANTED\n");
                permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
            } else {
                //Permission granted
                loginfo("ACCESS_NETWORK_STATE PERMISSION GRANTED\n");
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                logwarning("ACCESS_WIFI_STATE PERMISSION NOT GRANTED\n");
                permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
            } else {
                //Permission granted
                loginfo("ACCESS_WIFI_STATE PERMISSION GRANTED\n");
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                logwarning("CHANGE_WIFI_STATE PERMISSION NOT GRANTED\n");
                permissions.add(Manifest.permission.CHANGE_WIFI_STATE);
            } else {
                //Permission granted
                loginfo("CHANGE_WIFI_STATE PERMISSION GRANTED\n");
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                logwarning("ACCESS_FINE_LOCATION PERMISSION NOT GRANTED\n");
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            } else {
                //Permission granted
                loginfo("ACCESS_FINE_LOCATION PERMISSION GRANTED\n");
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                logwarning("ACCESS_COARSE_LOCATION PERMISSION NOT GRANTED\n");
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            } else {
                //Permission granted
                loginfo("ACCESS_COARSE_LOCATION PERMISSION GRANTED\n");
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                logwarning("CAMERA PERMISSION NOT GRANTED\n");
                permissions.add(Manifest.permission.CAMERA);
            } else {
                //Permission granted
                loginfo("CAMERA PERMISSION GRANTED\n");
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                logwarning("WRITE_EXTERNAL_STORAGE PERMISSION NOT GRANTED\n");
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                //Permission granted
                loginfo("WRITE_EXTERNAL_STORAGE PERMISSION GRANTED\n");
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                logwarning("READ_EXTERNAL_STORAGE PERMISSION NOT GRANTED\n");
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                //Permission granted
                loginfo("READ_EXTERNAL_STORAGE PERMISSION GRANTED\n");
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                logwarning("USE_FINGERPRINT PERMISSION NOT GRANTED\n");
                permissions.add(Manifest.permission.USE_FINGERPRINT);
            } else {
                //Permission granted
                loginfo("USE_FINGERPRINT PERMISSION GRANTED\n");
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                logwarning("BLUETOOTH PERMISSION NOT GRANTED\n");
                permissions.add(Manifest.permission.BLUETOOTH);
            } else {
                //Permission granted
                loginfo("BLUETOOTH PERMISSION GRANTED\n");
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                logwarning("BLUETOOTH_ADMIN PERMISSION NOT GRANTED\n");
                permissions.add(Manifest.permission.BLUETOOTH_ADMIN);
            } else {
                //Permission granted
                loginfo("BLUETOOTH_ADMIN PERMISSION GRANTED\n");
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                logwarning("VIBRATE PERMISSION NOT GRANTED\n");
                permissions.add(Manifest.permission.VIBRATE);
            } else {
                //Permission granted
                loginfo("VIBRATE PERMISSION GRANTED\n");
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                logwarning("READ_PHONE_STATE PERMISSION NOT GRANTED\n");
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            } else {
                //Permission granted
                loginfo("READ_PHONE_STATE PERMISSION GRANTED\n");
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                logwarning("WRITE_SETTINGS PERMISSION NOT GRANTED\n");
                permissions.add(Manifest.permission.WRITE_SETTINGS);
            } else {
                //Permission granted
                loginfo("WRITE_SETTINGS PERMISSION GRANTED\n");
            }

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    public void loginfo(String _message) {
        try {
            lblsplashprogressinfo.setText("");

            SpannableString _SpannableString = new SpannableString(_message);

            _SpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.splash_info)), 0, _message.length(), 0);

            _strbuilder.append(_SpannableString + "\n");

            lblsplashprogressinfo.setText(_strbuilder.toString());

            Log.e(TAG, _strbuilder.toString());

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }

    }

    public void logwarning(String _message) {
        try {
            lblsplashprogressinfo.setText("");

            SpannableString _SpannableString = new SpannableString(_message);

            _SpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.splash_warning)), 0, _message.length(), 0);

            _strbuilder.append(_SpannableString + "\n");

            lblsplashprogressinfo.setText(_strbuilder.toString());

            Log.e(TAG, _strbuilder.toString());

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }

    }

    void RuntimePermission() {
        try {

            if (promptPermissions()) {
                // permissions granted.
            } else {
                // show dialog informing them that we lack certain permissions
            }


        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    private boolean promptPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
                Log.e(TAG, p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new
                    String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);

            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:

                for (int i = 0; i < grantResults.length; i++) {
                    //perms.put(permissions[i], grantResults[i]);
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        // permissions granted.
                        utilz.getInstance(getApplicationContext()).globalloghandler("permission [ " + permissions[i] + " ] granted.", TAG, 1, 1);
                    } else {
                        // permissions not granted.
                        utilz.getInstance(getApplicationContext()).globalloghandler("permission [ " + permissions[i] + " ] not granted.", TAG, 1, 0);
                    }
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}































