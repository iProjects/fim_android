package com.tech.nyax.myapplication10;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

public class pestinsecticidecategoryspinneradapter implements SpinnerAdapter {

    private LayoutInflater inflater;
    List<pestinsecticidecategorydto> _lstdata;
    private static final String TAG = pestinsecticidecategoryspinneradapter.class.getSimpleName();
    Context _context;

    public pestinsecticidecategoryspinneradapter(Context context, List<pestinsecticidecategorydto> datalst) {
//        super(context, 0, data);
        super();
        inflater = LayoutInflater.from(context);
        _lstdata = datalst;
        _context = context;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
         ViewHolder viewHolder;
        if (view == null) {
            /****** Inflate layout.xml file for each row ( Defined below ) *******/
            view = inflater.inflate(R.layout.diseasepest_spinner_row_layout, null);
            // Do some initialization
            //Retrieve the view on the item layout and set the value.
            /****** View Holder Object to contain xml file elements ******/
            viewHolder = new ViewHolder(view);
            /************  Set holder with LayoutInflater ************/
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
		view.setTag(viewHolder);

        final pestinsecticidecategorydto data = getItem(position);
        viewHolder.txtdto_value.setText(data.getdto_value());
        
        return view;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return _lstdata.size();
    }

    @Override
    public pestinsecticidecategorydto getItem(int position) {
        return _lstdata.get(position);    // single item in the list
    }

    @Override
    public long getItemId(int position) {
        pestinsecticidecategorydto data = getItem(position);
        return data.getdto_id();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            /****** Inflate layout.xml file for each row ( Defined below ) *******/
            view = inflater.inflate(R.layout.diseasepest_spinner_row_layout, null);
            // Do some initialization
            //Retrieve the view on the item layout and set the value.
            /****** View Holder Object to contain xml file elements ******/
            viewHolder = new ViewHolder(view);
            /************  Set holder with LayoutInflater ************/
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
		view.setTag(viewHolder);

        final pestinsecticidecategorydto data = getItem(position);
        viewHolder.txtdto_value.setText(data.getdto_value());
        
        return view;
    }

    private class ViewHolder {
        private final TextView txtdto_value;

        private ViewHolder(View view) {
            txtdto_value = view.findViewById(R.id.lbldto_text_row);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
	
	 public int getIndex(Spinner _spinner, String searchstring){
		 for (int i = 0; i < _spinner.getCount(); i++){
			 if (_spinner.getItemAtPosition(i).toString().equalsIgnoreCase(searchstring)){
				 return i;
			 }
		 }
		 return 0;
	 }
		
}
