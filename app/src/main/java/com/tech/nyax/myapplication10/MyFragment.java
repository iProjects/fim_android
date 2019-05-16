package com.tech.nyax.myapplication10;

import android.app.Fragment;
import android.os.Bundle;
//import android.support.v4.app.Fragment;

public class MyFragment extends Fragment {

    // Our identifier for obtaining the name from arguments
    private static final String NAME_ARG = "MycodebehideFragment";
    private String mName;

    // Required
    public MyFragment() {
    }
// The static constructor. This is the only way that you should instantiate
// the fragment yourself

    public static MyFragment newInstance(final String name) {
        final MyFragment myFragment = new MyFragment();
// The 1 below is an optimization, being the number of arguments that will
// be added to this bundle. If you know the number of arguments you will add
// to the bundle it stops additional allocations of the backing map. If
// unsure, you can construct Bundle without any arguments
        final Bundle args = new Bundle(1);
// This stores the argument as an argument in the bundle. Note that even if
// the 'name' parameter is NULL then this will work, so you should consider
// at this point if the parameter is mandatory and if so check for NULL and
// throw an appropriate error if so
        args.putString(NAME_ARG, name);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle arguments = getArguments();
        if (arguments == null || !arguments.containsKey(NAME_ARG)) {
// Set a default or error as you see fit
        } else {
            mName = arguments.getString(NAME_ARG);
        }
    }
}
