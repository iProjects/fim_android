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
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class manufacturerslistadapter extends ArrayAdapter<manufacturerdto> {

    private LayoutInflater inflater;
    List<manufacturerdto> _lstdtos;
    private static final String TAG = manufacturerslistadapter.class.getSimpleName();
    Context _context;

    public manufacturerslistadapter(Context context, List<manufacturerdto> _lst_dtos) {
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
    public manufacturerdto getItem(int position) {
        return _lstdtos.get(position);    // single item in the list
    }

    @Override
    public long getItemId(int position) {
        manufacturerdto _selected_dto = getItem(position);
        return _selected_dto.getmanufacturer_Id();
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            /****** Inflate row_layout.xml file for each row ( Defined below ) *******/
            view = inflater.inflate(R.layout.manufacturer_row_layout, parent, false);
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
        final manufacturerdto _selected_dto = getItem(position);
        /************  Set values in Holder elements ***********/
        viewHolder.txtmanufacturerid.setText("id: " + String.format("%d", _selected_dto.getmanufacturer_Id()));
        viewHolder.txtmanufacturername.setText("name: " + _selected_dto.getmanufacturer_name());
        viewHolder.txtmanufacturerstatus.setText("status: " + _selected_dto.getmanufacturer_status());

        viewHolder.btneditmanufacturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "id: " + String.format("%d", _selected_dto.getmanufacturer_Id()));
                Log.e(TAG, "name: " + _selected_dto.getmanufacturer_name());
                Log.e(TAG, "status: " + _selected_dto.getmanufacturer_status());
 
                utilz.getInstance(_context).globallogwithtoasthandler("loading edit activity for id: " + _selected_dto.getmanufacturer_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
                dataBundle.putString("manufacturerid", String.format("%d", _selected_dto.getmanufacturer_Id()));
 
                // Starting new intent
                Intent intent = new Intent(_context,
                        editmanufactureractivity.class);
                // sending data to next activity
                intent.putExtras(dataBundle);

				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                // starting new activity
                _context.startActivity(intent);

            }
        });

        viewHolder.btndeletemanufacturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "id: " + String.format("%d", _selected_dto.getmanufacturer_Id()));
                Log.e(TAG, "name: " + _selected_dto.getmanufacturer_name());
                Log.e(TAG, "status: " + _selected_dto.getmanufacturer_status());

                utilz.getInstance(_context).globallogwithtoasthandler("loading delete activity for id: " + _selected_dto.getmanufacturer_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
                dataBundle.putString("manufacturerid", String.format("%d", _selected_dto.getmanufacturer_Id()));
 
                // Starting new intent
                Intent intent = new Intent(_context,
                        deletemanufactureractivity.class);
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
        private final TextView txtmanufacturerid;
        private final TextView txtmanufacturername;
        private final TextView txtmanufacturerstatus;
        private final ImageButton btneditmanufacturer;
        private final ImageButton btndeletemanufacturer;

        private ViewHolder(View view) {
            txtmanufacturerid = view.findViewById(R.id.txtmanufacturerid);
            txtmanufacturername = view.findViewById(R.id.txtmanufacturername);
            txtmanufacturerstatus = view.findViewById(R.id.txtmanufacturerstatus);
            btneditmanufacturer = view.findViewById(R.id.btneditmanufacturer);
            btndeletemanufacturer = view.findViewById(R.id.btndeletemanufacturer);

        }
    }


}