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

public class categorieslistadapter extends ArrayAdapter<categorydto> {

    private LayoutInflater inflater;
    List<categorydto> _lstdtos;
    private static final String TAG = categorieslistadapter.class.getSimpleName();
    Context _context;

    public categorieslistadapter(Context context, List<categorydto> _lst_dtos) {
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
    public categorydto getItem(int position) {
        return _lstdtos.get(position);    // single item in the list
    }

    @Override
    public long getItemId(int position) {
        categorydto _selected_dto = getItem(position);
        return _selected_dto.getcategory_Id();
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            /****** Inflate row_layout.xml file for each row ( Defined below ) *******/
            view = inflater.inflate(R.layout.category_row_layout, parent, false);
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
        final categorydto _selected_dto = getItem(position);
        /************  Set values in Holder elements ***********/
        viewHolder.txtcategoryid.setText(String.format("%d", _selected_dto.getcategory_Id()));
        viewHolder.txtcategoryname.setText("name: " + _selected_dto.getcategory_name()); 
        viewHolder.txtcategorystatus.setText("status: " + _selected_dto.getcategory_status());

        viewHolder.btneditcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "id: " + String.format("%d", _selected_dto.getcategory_Id()));
                Log.e(TAG, "name: " + _selected_dto.getcategory_name()); 
                Log.e(TAG, "status: " + _selected_dto.getcategory_status());

                utilz.getInstance(_context).globallogwithtoasthandler("loading edit activity for id: " + _selected_dto.getcategory_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
                dataBundle.putString("categoryid", String.format("%d", _selected_dto.getcategory_Id()));
//              dataBundle.putParcelable("selectedcategorydto", _selectedcategorydto);

                // Starting new intent
                Intent intent = new Intent(_context,
                        editcategoryactivity.class);
                // sending data to next activity
                intent.putExtras(dataBundle);

				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                // starting new activity
                _context.startActivity(intent);

            }
        });

        viewHolder.btndeletecategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                Log.e(TAG, "id: " + String.format("%d", _selected_dto.getcategory_Id()));
                Log.e(TAG, "name: " + _selected_dto.getcategory_name()); 
                Log.e(TAG, "status: " + _selected_dto.getcategory_status());

                utilz.getInstance(_context).globallogwithtoasthandler("loading delete activity for id: " + _selected_dto.getcategory_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
                dataBundle.putString("categoryid", String.format("%d", _selected_dto.getcategory_Id()));
 
                // Starting new intent
                Intent intent = new Intent(_context,
                        deletecategoryactivity.class);
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
        private final TextView txtcategoryid;
        private final TextView txtcategoryname; 
        private final TextView txtcategorystatus; 
		private final ImageButton btneditcategory;
		private final ImageButton btndeletecategory; 

        private ViewHolder(View view) {
            txtcategoryid = view.findViewById(R.id.txtcategoryid);
            txtcategoryname = view.findViewById(R.id.txtcategoryname); 
            txtcategorystatus = view.findViewById(R.id.txtcategorystatus);
            btneditcategory = view.findViewById(R.id.btneditcategory);
            btndeletecategory = view.findViewById(R.id.btndeletecategory);

        }
    }


}