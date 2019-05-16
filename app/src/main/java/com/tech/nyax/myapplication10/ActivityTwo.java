package com.tech.nyax.myapplication10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ActivityTwo extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
    }
    private void closeActivity() {
        Intent intent = new Intent();
        intent.putExtra(crops_list_fragment.CROP_NAME_TAG, "Testing passing data back to ActivityOne");
        setResult(crops_list_fragment.RESULT_CODE, intent); // You can also send result without any data using setResult(int resultCode)
        finish();
    }
}
