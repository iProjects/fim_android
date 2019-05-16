package com.tech.nyax.myapplication10;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class OriginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_origin);

//        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
//        viewPager.setAdapter(new MyPagerAdapter(this));
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//// This method will be invoked when a new page becomes selected. Animation is not necessarily complete.
//            @Override
//            public void onPageSelected(int position) {
//// Your code
//            }// This method will be invoked when the current page is scrolled, either as part of
//            // a programmatically initiated smooth scroll or a user initiated touch scroll.
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//// Your code
//            }
//            // Called when the scroll state changes. Useful for discovering when the user begins
//// dragging, when the pager is automatically settling to the current page,
//// or when it is fully stopped/idle.
//            @Override
//            public void onPageScrollStateChanged(int state) {
//// Your code
//            }
//        });
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
///Override defining menu resource
        inflater.inflate(R.menu.menu_resource_id, menu);
        super.onCreateOptionsMenu(menu);
    }
  /*  @Override
    public void onPrepareOptionsMenu(Menu menu) {
//Override for preparing items (setting visibility, change text, change icon...)
        super.onPrepareOptionsMenu(menu);
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//Override it for handling items
        int menuItemId = item.getItemId();
        switch (menuItemId) {
            case R.id.first_item_id:
            return true; //return true, if is handled
        }
        return super.onOptionsItemSelected(item);
    }


}