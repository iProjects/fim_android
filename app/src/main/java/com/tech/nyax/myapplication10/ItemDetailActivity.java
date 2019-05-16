package com.tech.nyax.myapplication10;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
//import android.databinding.DataBindingUtil;
//import android.support.v7.widget.RecyclerView;

public class ItemDetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
		//getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));
		
/*
        ItemDetailActivityBinding binding = DataBindingUtil.setContentView(this,
                R.layout.item_detail_activity);
        Item item = new Item("Example item", "This is an example item.");
        binding.setItem(item);

        setContentView(R.layout.fragment_main);*/

        showfragment();
    }

    public void showfragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        MyFragment mFragment = MyFragment.newInstance("my name");
//        ft.replace(R.id.fragment_one, mFragment);
//R.id.placeholder is where we want to load our fragment
        ft.commit();
    }
}


