package com.tech.nyax.myapplication10;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

public class listcropsactivity extends AppCompatActivity {

    private final static String TAG = listcropsactivity.class.getCanonicalName();
    // Create DatabasehelperUtilz class object in your activity.
    private DatabasehelperUtilz db;
    List<cropdto> cropdtos;
    ListView cropslistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crops_list_layout);

		getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));
		
        try {
            db = new DatabasehelperUtilz(listcropsactivity.this);
            db.openDataBase();
            cropdtos = db.getallcrops();
            db.close();

            for (int i = 1; i < cropdtos.size(); i++) {
                cropdto _cropdto = cropdtos.get(i);
                Log.e(TAG, "cropdto name: " + _cropdto.getcrop_name() + " cropdto status: " + _cropdto.getcrop_status());
				
                utilz.getInstance(getApplicationContext()).globalloghandler("cropdto name: " + _cropdto.getcrop_name() + " cropdto status: " + _cropdto.getcrop_status(), TAG, 1, 0);
            }

            cropslistadapter _cropslistadapter = new cropslistadapter(getApplicationContext(), cropdtos);
            cropslistview = findViewById(R.id.lstcrops);
            cropslistview.setAdapter(_cropslistadapter);

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.crops_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.create_crop_activity:
                    final Intent createcropactivity = new Intent(this, createcropactivity.class);
                    startActivity(createcropactivity);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            return  false;
        }
    }



}
