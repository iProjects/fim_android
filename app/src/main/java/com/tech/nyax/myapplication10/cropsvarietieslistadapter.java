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

public class cropsvarietieslistadapter extends ArrayAdapter<cropvarietydto> {

    private LayoutInflater inflater;
    List<cropvarietydto> _lstdtos;
    private static final String TAG = cropsvarietieslistadapter.class.getSimpleName();
    Context _context;

    public cropsvarietieslistadapter(Context context, List<cropvarietydto> _lst_dtos) {
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
    public cropvarietydto getItem(int position) {
        return _lstdtos.get(position);    // single item in the list
    }

    @Override
    public long getItemId(int position) {
        cropvarietydto _selected_dto = getItem(position);
        return _selected_dto.getcropvariety_Id();
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            /****** Inflate row_layout.xml file for each row ( Defined below ) *******/
            view = inflater.inflate(R.layout.crop_variety_row_layout, parent, false);
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
        final cropvarietydto _selected_dto = getItem(position);
        /************  Set values in Holder elements ***********/
        viewHolder.txtcropvarietyid.setText("id: " + String.format("%d", _selected_dto.getcropvariety_Id()));
        viewHolder.txtcropvarietyname.setText("name: " + _selected_dto.getcropvariety_name());
        viewHolder.txtcropvarietystatus.setText("status: " + _selected_dto.getcropvariety_status());
		viewHolder.txtcropvarietycrop.setText("crop: " + _selected_dto.getcropvariety_crop_id());
		viewHolder.txtcropvarietymanufacturer.setText("manufacturer: " + _selected_dto.getcropvariety_manufacturer_id());

        viewHolder.btneditcropvariety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "id: " + String.format("%d", _selected_dto.getcropvariety_Id()));
                Log.e(TAG, "name: " + _selected_dto.getcropvariety_name());
                Log.e(TAG, "status: " + _selected_dto.getcropvariety_status());
				Log.e(TAG, "crop: " + _selected_dto.getcropvariety_crop_id());
                Log.e(TAG, "manufacturer: " + _selected_dto.getcropvariety_manufacturer_id());
 
                utilz.getInstance(_context).globallogwithtoasthandler("loading edit activity for id: " + _selected_dto.getcropvariety_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
                dataBundle.putString("cropvarietyid", String.format("%d", _selected_dto.getcropvariety_Id()));
//              dataBundle.putParcelable("selectedcropvarietydto", _selectedcropvarietydto);

                // Starting new intent
                Intent intent = new Intent(_context,
                        editcropvarietyactivity.class);
                // sending data to next activity
                intent.putExtras(dataBundle);

				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                // starting new activity
                _context.startActivity(intent);

            }
        });

        viewHolder.btndeletecropvariety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "id: " + String.format("%d", _selected_dto.getcropvariety_Id()));
                Log.e(TAG, "name: " + _selected_dto.getcropvariety_name());
                Log.e(TAG, "status: " + _selected_dto.getcropvariety_status());
				Log.e(TAG, "crop: " + _selected_dto.getcropvariety_crop_id());
                Log.e(TAG, "manufacturer: " + _selected_dto.getcropvariety_manufacturer_id());
 
                utilz.getInstance(_context).globallogwithtoasthandler("loading delete activity for id: " + _selected_dto.getcropvariety_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
                dataBundle.putString("cropvarietyid", String.format("%d", _selected_dto.getcropvariety_Id()));
 
                // Starting new intent
                Intent intent = new Intent(_context,
                        deletecropvarietyactivity.class);
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
        private final TextView txtcropvarietyid;
        private final TextView txtcropvarietyname;
        private final TextView txtcropvarietystatus;
		private final TextView txtcropvarietycrop;
        private final TextView txtcropvarietymanufacturer;
        private final ImageButton btneditcropvariety;
        private final ImageButton btndeletecropvariety;

        private ViewHolder(View view) {
            txtcropvarietyid = view.findViewById(R.id.txtcropvarietyid);
            txtcropvarietyname = view.findViewById(R.id.txtcropvarietyname);
            txtcropvarietystatus = view.findViewById(R.id.txtcropvarietystatus);
			txtcropvarietycrop = view.findViewById(R.id.txtcropvarietycrop);
            txtcropvarietymanufacturer = view.findViewById(R.id.txtcropvarietymanufacturer);
            btneditcropvariety = view.findViewById(R.id.btneditcropvariety);
            btndeletecropvariety = view.findViewById(R.id.btndeletecropvariety);

        }
    }


}