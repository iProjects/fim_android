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

public class cropsdiseaseslistadapter extends ArrayAdapter<cropdiseasedto> {

    private LayoutInflater inflater;
    List<cropdiseasedto> _lstdtos;
    private static final String TAG = cropsdiseaseslistadapter.class.getSimpleName();
    Context _context;

    public cropsdiseaseslistadapter(Context context, List<cropdiseasedto> _lst_dtos) {
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
    public cropdiseasedto getItem(int position) {
        return _lstdtos.get(position);    // single item in the list
    }

    @Override
    public long getItemId(int position) {
        cropdiseasedto _selected_dto = getItem(position);
        return _selected_dto.getcropdisease_Id();
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            /****** Inflate row_layout.xml file for each row ( Defined below ) *******/
            view = inflater.inflate(R.layout.crop_disease_row_layout, parent, false);
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
        final cropdiseasedto _selected_dto = getItem(position);
        /************  Set values in Holder elements ***********/
        viewHolder.txtcropdiseaseid.setText("id: " + String.format("%d", _selected_dto.getcropdisease_Id()));
        viewHolder.txtcropdiseasename.setText("name: " + _selected_dto.getcropdisease_name());
        viewHolder.txtcropdiseasecategory.setText("category: " + _selected_dto.getcropdisease_category());
        viewHolder.txtcropdiseasestatus.setText("status: " + _selected_dto.getcropdisease_status());

        viewHolder.btneditcropdisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "id: " + String.format("%d", _selected_dto.getcropdisease_Id()));
                Log.e(TAG, "name: " + _selected_dto.getcropdisease_name());
                Log.e(TAG, "category: " + _selected_dto.getcropdisease_category());
                Log.e(TAG, "status: " + _selected_dto.getcropdisease_status());

                utilz.getInstance(_context).globallogwithtoasthandler("loading edit activity for id: " + _selected_dto.getcropdisease_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
                dataBundle.putString("cropdiseaseid", String.format("%d", _selected_dto.getcropdisease_Id()));
//              dataBundle.putParcelable("selectedcropdiseasedto", _selectedcropdiseasedto);

                // Starting new intent
                Intent intent = new Intent(_context,
                        editcropdiseaseactivity.class);
                // sending data to next activity
                intent.putExtras(dataBundle);

				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                // starting new activity
                _context.startActivity(intent);

            }
        });

        viewHolder.btndeletecropdisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "id: " + String.format("%d", _selected_dto.getcropdisease_Id()));
                Log.e(TAG, "name: " + _selected_dto.getcropdisease_name());
                Log.e(TAG, "category: " + _selected_dto.getcropdisease_category());
                Log.e(TAG, "status: " + _selected_dto.getcropdisease_status());

                utilz.getInstance(_context).globallogwithtoasthandler("loading delete activity for id: " + _selected_dto.getcropdisease_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
                dataBundle.putString("cropdiseaseid", String.format("%d", _selected_dto.getcropdisease_Id()));
 
                // Starting new intent
                Intent intent = new Intent(_context,
                        deletecropdiseasepestactivity.class);
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
        private final TextView txtcropdiseaseid;
        private final TextView txtcropdiseasename;
        private final TextView txtcropdiseasecategory;
        private final TextView txtcropdiseasestatus;
        private final ImageButton btneditcropdisease;
        private final ImageButton btndeletecropdisease;

        private ViewHolder(View view) {
            txtcropdiseaseid = view.findViewById(R.id.txtcropdiseaseid);
            txtcropdiseasename = view.findViewById(R.id.txtcropdiseasename);
            txtcropdiseasecategory = view.findViewById(R.id.txtcropdiseasecategory);
            txtcropdiseasestatus = view.findViewById(R.id.txtcropdiseasestatus);
            btneditcropdisease = view.findViewById(R.id.btneditcropdisease);
            btndeletecropdisease = view.findViewById(R.id.btndeletecropdisease);

        }
    }


}