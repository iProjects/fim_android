package com.tech.nyax.myapplication10;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
//import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class settingslistadapter extends ArrayAdapter<settingdto> {

    private LayoutInflater inflater;
    List<settingdto> _lstdtos;
    private static final String TAG = settingslistadapter.class.getSimpleName();
    Context _context;

    public settingslistadapter(Context context, List<settingdto> _lst_dtos) {
        super(context, 0, _lst_dtos);
        inflater = LayoutInflater.from(context);
        _lstdtos = _lst_dtos;
        _context = context;
    }

    @Override
    public int getCount() {
        return _lstdtos.size();    // total number of elements in the list
    }

    @Override
    public settingdto getItem(int position) {
        return _lstdtos.get(position);    // single item in the list
    }

    @Override
    public long getItemId(int position) {
        settingdto _selected_dto = getItem(position);
        return _selected_dto.getsetting_Id();
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            /****** Inflate row_layout.xml file for each row ( Defined below ) *******/
            view = inflater.inflate(R.layout.setting_row_layout, parent, false);
            // Do some initialization
            //Retrieve the view on the item layout and set the value.
            /****** View Holder Object to contain item.xml file elements ******/
            viewHolder = new ViewHolder(view);
            /************  Set holder with LayoutInflater ************/
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
		view.setTag(viewHolder);

        //Retrieve your object
        final settingdto _selected_dto = getItem(position);
        /************  Set values in Holder elements ***********/
        viewHolder.txtsettingid.setText("id: " + String.format("%d", _selected_dto.getsetting_Id()));
        viewHolder.txtsettingname.setText("name: " + _selected_dto.getsetting_name());
        viewHolder.txtsettingvalue.setText("value: " + _selected_dto.getsetting_value());
        viewHolder.txtsettingstatus.setText("status: " + _selected_dto.getsetting_status());

        viewHolder.btneditsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "id: " + String.format("%d", _selected_dto.getsetting_Id()));
                Log.e(TAG, "name: " + _selected_dto.getsetting_name());
                Log.e(TAG, "value: " + _selected_dto.getsetting_value());
                Log.e(TAG, "status: " + _selected_dto.getsetting_status());
 
                utilz.getInstance(_context).globallogwithtoasthandler("loading edit activity for id: " + _selected_dto.getsetting_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
                dataBundle.putString("settingid", String.format("%d", _selected_dto.getsetting_Id()));
 
                // Starting new intent
                Intent intent = new Intent(_context,
                        editsettingactivity.class);
                // sending data to next activity
                intent.putExtras(dataBundle);

				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                // starting new activity
                _context.startActivity(intent);

            }
        });

        viewHolder.btndeletesetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "id: " + String.format("%d", _selected_dto.getsetting_Id()));
                Log.e(TAG, "name: " + _selected_dto.getsetting_name());
                Log.e(TAG, "value: " + _selected_dto.getsetting_value());
                Log.e(TAG, "status: " + _selected_dto.getsetting_status());

                utilz.getInstance(_context).globallogwithtoasthandler("loading delete activity for id: " + _selected_dto.getsetting_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
                dataBundle.putString("settingid", String.format("%d", _selected_dto.getsetting_Id()));
 
                // Starting new intent
                Intent intent = new Intent(_context,
                        deletesettingactivity.class);
                // sending data to next activity
                intent.putExtras(dataBundle);

				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                // starting new activity
                _context.startActivity(intent);
 
            }
        });

        return view;
    }

    private class ViewHolder {
        private final TextView txtsettingid;
        private final TextView txtsettingname;
        private final TextView txtsettingvalue;
        private final TextView txtsettingstatus;
        private final ImageButton btneditsetting;
        private final ImageButton btndeletesetting;

        private ViewHolder(View view) {
            txtsettingid = view.findViewById(R.id.txtsettingid);
            txtsettingname = view.findViewById(R.id.txtsettingname);
            txtsettingvalue = view.findViewById(R.id.txtsettingvalue);
            txtsettingstatus = view.findViewById(R.id.txtsettingstatus);
            btneditsetting = view.findViewById(R.id.btneditsetting);
            btndeletesetting = view.findViewById(R.id.btndeletesetting);

        }
    }


}