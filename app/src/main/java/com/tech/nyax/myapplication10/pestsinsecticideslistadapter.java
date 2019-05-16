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

public class pestsinsecticideslistadapter extends ArrayAdapter<pestinsecticidedto> {

    private LayoutInflater inflater;
    List<pestinsecticidedto> _lstdtos;
    private static final String TAG = pestsinsecticideslistadapter.class.getSimpleName();
    Context _context;

    public pestsinsecticideslistadapter(Context context, List<pestinsecticidedto> _lst_dtos) {
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
    public pestinsecticidedto getItem(int position) {
        return _lstdtos.get(position);    // single item in the list
    }

    @Override
    public long getItemId(int position) {
        pestinsecticidedto _selected_dto = getItem(position);
        return _selected_dto.getpestinsecticide_Id();
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            /****** Inflate row_layout.xml file for each row ( Defined below ) *******/
            view = inflater.inflate(R.layout.pestinsecticide_row_layout, parent, false);
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
        final pestinsecticidedto _selected_dto = getItem(position);
        /************  Set values in Holder elements ***********/
        viewHolder.txtpestinsecticideid.setText("id: " + String.format("%d", _selected_dto.getpestinsecticide_Id()));
        viewHolder.txtpestinsecticidename.setText("name: " + _selected_dto.getpestinsecticide_name());
        viewHolder.txtpestinsecticidecategory.setText("category: " + _selected_dto.getpestinsecticide_category());
        viewHolder.txtpestinsecticidestatus.setText("status: " + _selected_dto.getpestinsecticide_status()); 
		viewHolder.txtpestinsecticidediseasepest.setText("disease/pest: " + _selected_dto.getpestinsecticide_crop_disease_name());
		viewHolder.txtpestinsecticidemanufacturer.setText("manufacturer: " + _selected_dto.getpestinsecticide_manufacturer_name());

        viewHolder.btneditpestinsecticide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "id: " + String.format("%d", _selected_dto.getpestinsecticide_Id()));
                Log.e(TAG, "name: " + _selected_dto.getpestinsecticide_name());
                Log.e(TAG, "category: " + _selected_dto.getpestinsecticide_category());
                Log.e(TAG, "status: " + _selected_dto.getpestinsecticide_status());
				Log.e(TAG, "disease/pest: " + _selected_dto.getpestinsecticide_crop_disease_name());
				Log.e(TAG, "manufacturer: " + _selected_dto.getpestinsecticide_manufacturer_name());
			 
                utilz.getInstance(_context).globallogwithtoasthandler("loading edit activity for id: " + _selected_dto.getpestinsecticide_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
                dataBundle.putString("pestinsecticideid", String.format("%d", _selected_dto.getpestinsecticide_Id()));
//              dataBundle.putParcelable("selectedpestinsecticidedto", _selectedpestinsecticidedto);

                // Starting new intent
                Intent intent = new Intent(_context,
                        editpestinsecticideactivity.class);
                // sending data to next activity
                intent.putExtras(dataBundle);

				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                // starting new activity
                _context.startActivity(intent);

            }
        });

        viewHolder.btndeletepestinsecticide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "id: " + String.format("%d", _selected_dto.getpestinsecticide_Id()));
                Log.e(TAG, "name: " + _selected_dto.getpestinsecticide_name());
                Log.e(TAG, "category: " + _selected_dto.getpestinsecticide_category());
                Log.e(TAG, "status: " + _selected_dto.getpestinsecticide_status());
				Log.e(TAG, "disease/pest: " + _selected_dto.getpestinsecticide_crop_disease_name());
				Log.e(TAG, "manufacturer: " + _selected_dto.getpestinsecticide_manufacturer_name());
				
                utilz.getInstance(_context).globallogwithtoasthandler("loading delete activity for id: " + _selected_dto.getpestinsecticide_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
                dataBundle.putString("pestinsecticideid", String.format("%d", _selected_dto.getpestinsecticide_Id()));
 
                // Starting new intent
                Intent intent = new Intent(_context,
                        deletepestinsecticideactivity.class);
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
        private final TextView txtpestinsecticideid;
        private final TextView txtpestinsecticidename;
        private final TextView txtpestinsecticidecategory;
        private final TextView txtpestinsecticidestatus;
		private final TextView txtpestinsecticidediseasepest;
		private final TextView txtpestinsecticidemanufacturer;
        private final ImageButton btneditpestinsecticide;
        private final ImageButton btndeletepestinsecticide;

        private ViewHolder(View view) {
            txtpestinsecticideid = view.findViewById(R.id.txtpestinsecticideid);
            txtpestinsecticidename = view.findViewById(R.id.txtpestinsecticidename);
            txtpestinsecticidecategory = view.findViewById(R.id.txtpestinsecticidecategory);
            txtpestinsecticidestatus = view.findViewById(R.id.txtpestinsecticidestatus); 
			txtpestinsecticidediseasepest = view.findViewById(R.id.txtpestinsecticidediseasepest);
            txtpestinsecticidemanufacturer = view.findViewById(R.id.txtpestinsecticidemanufacturer); 
            btneditpestinsecticide = view.findViewById(R.id.btneditpestinsecticide);
            btndeletepestinsecticide = view.findViewById(R.id.btndeletepestinsecticide);

        }
    }


}