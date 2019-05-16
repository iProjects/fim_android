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

public class search_crops_list_adapter extends ArrayAdapter<search_crop_dto> {

    private LayoutInflater inflater;
    List<search_crop_dto> _lstdtos;
    private static final String TAG = search_crops_list_adapter.class.getSimpleName();
    Context _context;

    public search_crops_list_adapter(Context context, List<search_crop_dto> _lst_dtos) {
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
    public search_crop_dto getItem(int position) {
        return _lstdtos.get(position);    // single item in the list
    }

    @Override
    public long getItemId(int position) {
        search_crop_dto _selected_dto = getItem(position);
        return _selected_dto.getdto_Id();
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            /****** Inflate row_layout.xml file for each row ( Defined below ) *******/
            view = inflater.inflate(R.layout.search_crop_row_layout, parent, false);
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
        final search_crop_dto _selected_dto = getItem(position);
        /************  Set values in Holder elements ***********/
        viewHolder.txtcropid.setText(String.format("%d", _selected_dto.getdto_Id()));
        viewHolder.txtcropname.setText("crop: " + _selected_dto.getcrop_name()); 
        viewHolder.txtcropvariety.setText("variety: " + _selected_dto.getcrop_variety_name());
		viewHolder.txtcropmanufacturer.setText("manufacturer: " + _selected_dto.getmanufacturer_name());
 
        return view;
    }

    private class ViewHolder {
        private final TextView txtcropid;
        private final TextView txtcropname; 
        private final TextView txtcropvariety;
		private final TextView txtcropmanufacturer;

        private ViewHolder(View view) {
            txtcropid = view.findViewById(R.id.txtcropid);
            txtcropname = view.findViewById(R.id.txtcropname); 
            txtcropvariety = view.findViewById(R.id.txtcropvariety);
            txtcropmanufacturer = view.findViewById(R.id.txtcropmanufacturer);
             
        }
    }


}