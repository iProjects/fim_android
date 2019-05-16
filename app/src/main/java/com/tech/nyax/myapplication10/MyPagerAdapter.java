package com.tech.nyax.myapplication10;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyPagerAdapter extends PagerAdapter {

    private Context mContext;
    private int numberOfPages;

    public MyPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
// Create the page for the given position. For example:
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.main_fragment, collection, false);
        collection.addView(layout);
        return layout;
    }
    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
// Remove a page for the given position. For example:
        collection.removeView((View) view);
    }
    @Override
    public int getCount() {
//Return the number of views available.
        return numberOfPages;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
// Determines whether a page View is associated with a specific key object
// as returned by instantiateItem(ViewGroup, int). For example:
        return view == object;
    }
}
