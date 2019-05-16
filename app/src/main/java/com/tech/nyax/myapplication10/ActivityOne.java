package com.tech.nyax.myapplication10;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;
//import android.widget.VideoView;

public class ActivityOne extends AppCompatActivity {

    private MyPagerAdapter mFragmentAdapter;
    private ViewPager mViewPager;
    public static final String FRAGMENT_TAG = "FragmentOne";
    String pathToVideo = "FragmentOne";
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_one);
            getSupportActionBar().setTitle("Activity One");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*if (null == savedInstanceState) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack("FragmentOne")
                    .replace(R.id.container, FragmentOne.newInstance(),"FragmentOne")
                    .commit();
        }*/

            //replaceFragment(crops_list_fragment.newInstance(), "FragmentOne");
            replaceFragment(FragmentTwo.newInstance(""), "FragmentTwo");
            replaceFragment(FragmentThree.newInstance(""), "FragmentThree");

            // Get the ViewPager and apply the PagerAdapter
            mFragmentAdapter = new MyPagerAdapter(getSupportFragmentManager());
            mViewPager = findViewById(R.id.container);
            mViewPager.setAdapter(mFragmentAdapter);

            // ...... set up ViewPager ............
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    if (position == 0) {
//                    fab.setImageResource(android.R.drawable.ic_dialog_email);
//                    fab.show();
                    } else if (position == 2) {
//                    fab.setImageResource(android.R.drawable.ic_dialog_map);
//                    fab.show();
                    } else {
//                    fab.hide();
                    }
                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int
                        positionOffsetPixels) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

            videoView = (VideoView) findViewById(R.id.videoView);
            /* pathToVideo = getResources().getResourceName(R.raw.the_him_feat_gia_koka_dont_leave_without_me);
            videoView.setVideoPath(pathToVideo); */
//        Start playing video.
//        /**/videoView.start();
//        videoView.setVideoURI(Uri.parse("http://example.com/examplevideo.mp4"));
            videoView.requestFocus();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                }
            });
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    videoView.start();
                    mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                            MediaController mediaController = new MediaController(ActivityOne.this);
                            videoView.setMediaController(mediaController);
                            mediaController.setAnchorView(videoView);
                        }
                    });
                }
            });
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    return false;
                }
            });
        }catch(Exception ex){
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), ActivityOne.class.getSimpleName(), 1, 0, "ActivityOne.OnCreate", "ActivityOne.OnCreate");
        }
    }

    @Override
    public void onBackPressed() {
        int fragmentsInStack = getSupportFragmentManager().getBackStackEntryCount();
        if (fragmentsInStack > 1) { // If we have more than one fragment, pop back stack
            getSupportFragmentManager().popBackStack();
        } else if (fragmentsInStack == 1) { // Finish activity, if only one fragment left, to prevent leaving empty screen
            finish();
        } else {
            super.onBackPressed();
        }
    }

    public void replaceFragment(Fragment fragment, String tag) {
//Get current fragment placed in container
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
//Prevent adding same fragment on top
        /*if (currentFragment.getClass() == fragment.getClass()) {
            return;
        }*/
//If fragment is already on stack, we can pop back stack to prevent stack infinite growth
        if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
            getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
//Otherwise, just replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .add(R.id.container, fragment, tag)
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // You must override this method as the second Activity will always send its results to this Activity and then to the Fragment
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    utilz.getInstance(getApplicationContext()).globalloghandler("FragmentOne", ActivityOne.class.getSimpleName(), 1, 0, "FragmentOne", "FragmentOne");
                    return new crops_list_fragment();
                case 1:
                    utilz.getInstance(getApplicationContext()).globalloghandler("FragmentOne", ActivityOne.class.getSimpleName(), 1, 0, "FragmentOne", "FragmentOne");
                    return new FragmentTwo();
                case 2:
                    utilz.getInstance(getApplicationContext()).globalloghandler("FragmentOne", ActivityOne.class.getSimpleName(), 1, 0, "FragmentOne", "FragmentOne");
                    return new FragmentThree();
                default:
                    return null;
            }
        }

        // Will be displayed as the tab's label
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Fragment 1 title";
                case 1:
                    return "Fragment 2 title";
                case 2:
                    return "Fragment 3 title";
                default:
                    return null;
            }
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return 3;
        }
    }
}
