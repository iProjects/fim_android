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

public class search_cropsdiseasespests_list_adapter extends ArrayAdapter<search_cropdiseasepest_dto> {

    private LayoutInflater inflater;
    List<search_cropdiseasepest_dto> _lstdtos;
    private static final String TAG = search_cropsdiseasespests_list_adapter.class.getSimpleName();
    Context _context;

    public search_cropsdiseasespests_list_adapter(Context context, List<search_cropdiseasepest_dto> _lst_dtos) {
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
    public search_cropdiseasepest_dto getItem(int position) {
        return _lstdtos.get(position);    // single item in the list
    }

    @Override
    public long getItemId(int position) {
        search_cropdiseasepest_dto _selected_dto = getItem(position);
        return _selected_dto.getdto_Id();
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            /****** Inflate row_layout.xml file for each row ( Defined below ) *******/
            view = inflater.inflate(R.layout.search_cropdisease_row_layout, parent, false);
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
        final search_cropdiseasepest_dto _selected_dto = getItem(position);
        /************  Set values in Holder elements ***********/
        viewHolder.txtcropdiseasedtoid.setText("id: " + String.format("%d", _selected_dto.getdto_Id()));
        viewHolder.txtcropdiseasepestname.setText("diseasepest: " + _selected_dto.getcrop_diseasepest_name()); 
        viewHolder.txtpestinsecticidename.setText("pestinsecticide: " + _selected_dto.getpestinsecticide_name());
		viewHolder.txtpestinsecticidemanufacturer.setText("manufacturer: " + _selected_dto.getpestinsecticide_manufacturer());
 
        return view;
    }

    private class ViewHolder {
        private final TextView txtcropdiseasedtoid;
        private final TextView txtcropdiseasepestname; 
        private final TextView txtpestinsecticidename;
		private final TextView txtpestinsecticidemanufacturer;

        private ViewHolder(View view) {
            txtcropdiseasedtoid = view.findViewById(R.id.txtcropdiseasedtoid);
            txtcropdiseasepestname = view.findViewById(R.id.txtcropdiseasepestname); 
            txtpestinsecticidename = view.findViewById(R.id.txtpestinsecticidename);
            txtpestinsecticidemanufacturer = view.findViewById(R.id.txtpestinsecticidemanufacturer);
             
        }
    }


}