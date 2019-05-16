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

public class cropslistadapter extends ArrayAdapter<cropdto> {

    private LayoutInflater inflater;
    List<cropdto> _lstdtos;
    private static final String TAG = cropslistadapter.class.getSimpleName();
    Context _context;

    public cropslistadapter(Context context, List<cropdto> _lst_dtos) {
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
    public cropdto getItem(int position) {
        return _lstdtos.get(position);    // single item in the list
    }

    @Override
    public long getItemId(int position) {
        cropdto _selected_dto = getItem(position);
        return _selected_dto.getcrop_Id();
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            /****** Inflate row_layout.xml file for each row ( Defined below ) *******/
            view = inflater.inflate(R.layout.crop_row_layout, parent, false);
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
        final cropdto _selected_dto = getItem(position);
        /************  Set values in Holder elements ***********/
        viewHolder.txtcropid.setText(String.format("%d", _selected_dto.getcrop_Id()));
        viewHolder.txtcropname.setText("name: " + _selected_dto.getcrop_name()); 
        viewHolder.txtcropstatus.setText("status: " + _selected_dto.getcrop_status());

        viewHolder.btneditcrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "id: " + String.format("%d", _selected_dto.getcrop_Id()));
                Log.e(TAG, "name: " + _selected_dto.getcrop_name()); 
                Log.e(TAG, "status: " + _selected_dto.getcrop_status());

                utilz.getInstance(_context).globallogwithtoasthandler("loading edit activity for id: " + _selected_dto.getcrop_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
                dataBundle.putString("cropid", String.format("%d", _selected_dto.getcrop_Id()));
//              dataBundle.putParcelable("selectedcropdto", _selectedcropdto);

                // Starting new intent
                Intent intent = new Intent(_context,
                        editcropactivity.class);
                // sending data to next activity
                intent.putExtras(dataBundle);

				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                // starting new activity
                _context.startActivity(intent);

            }
        });

        viewHolder.btndeletecrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                Log.e(TAG, "id: " + String.format("%d", _selected_dto.getcrop_Id()));
                Log.e(TAG, "name: " + _selected_dto.getcrop_name()); 
                Log.e(TAG, "status: " + _selected_dto.getcrop_status());

                utilz.getInstance(_context).globallogwithtoasthandler("loading delete activity for id: " + _selected_dto.getcrop_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
                dataBundle.putString("cropid", String.format("%d", _selected_dto.getcrop_Id()));
 
                // Starting new intent
                Intent intent = new Intent(_context,
                        deletecropactivity.class);
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
        private final TextView txtcropid;
        private final TextView txtcropname; 
        private final TextView txtcropstatus; 
		ImageButton btneditcrop;
		ImageButton btndeletecrop; 

        private ViewHolder(View view) {
            txtcropid = view.findViewById(R.id.txtcropid);
            txtcropname = view.findViewById(R.id.txtcropname); 
            txtcropstatus = view.findViewById(R.id.txtcropstatus);
            btneditcrop = view.findViewById(R.id.btneditcrop);
            btndeletecrop = view.findViewById(R.id.btndeletecrop);

        }
    }


}