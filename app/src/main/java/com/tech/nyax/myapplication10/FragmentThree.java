package com.tech.nyax.myapplication10;

//import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentThree extends Fragment {
	
    public static final int REQUEST_CODE = 11;
    public static final int RESULT_CODE = 12;
    public static final String EXTRA_KEY_TEST = "testKey";
	private static final String SEARCHTERM_ARG = "name";

    public FragmentThree() {

    }
    public static FragmentThree newInstance(final String searchterm) {
        final FragmentThree myFragment = new FragmentThree();
		// The 1 below is an optimization, being the number of arguments that will
		// be added to this bundle. If you know the number of arguments you will add
		// to the bundle it stops additional allocations of the backing map. If
		// unsure, you can construct Bundle without any arguments
		final Bundle args = new Bundle(1);
		// This stores the argument as an argument in the bundle. Note that even if
		// the 'name' parameter is NULL then this will work, so you should consider
		// at this point if the parameter is mandatory and if so check for NULL and
		// throw an appropriate error if so
		args.putString(SEARCHTERM_ARG, searchterm);
		myFragment.setArguments(args);
		return myFragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
        return inflater.inflate(R.layout.fragment_three, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            String testResult = data.getStringExtra(EXTRA_KEY_TEST);
// TODO: Do something with your extra data
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
