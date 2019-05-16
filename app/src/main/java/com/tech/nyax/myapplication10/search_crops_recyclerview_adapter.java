package com.tech.nyax.myapplication10;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import java.util.List;

// Create a RecyclerView adapter and ViewHolder
// Next, you have to inherit the RecyclerView.Adapter and the RecyclerView.ViewHolder. A usual class structure would be:
public class search_crops_recyclerview_adapter extends RecyclerView.Adapter<search_crops_recyclerview_adapter.ViewHolder> {
    
	//dataset
    private List<search_crop_dto> _lstdtos;
	private static final String TAG = search_crops_recyclerview_adapter.class.getSimpleName();
    Context _context;

	//abstract method in Recycleview adapter for implementing endless scrolling
	//public abstract void load();

    // The adapter's constructor sets the used dataset:
    public search_crops_recyclerview_adapter(Context context, List<search_crop_dto> _lst_dtos) {
		_context = context;
        _lstdtos = _lst_dtos;
    }

// First, we implement the ViewHolder. It only inherits the default constructor and saves the needed views into some fields:
    public class ViewHolder extends RecyclerView.ViewHolder {
		 
		private final TextView txtcropid;
        private final TextView txtcropname; 
        private final TextView txtcropvariety;
		private final TextView txtcropmanufacturer;

        public ViewHolder(View view) {
            super(view);
			 
			txtcropid = view.findViewById(R.id.txtcropid);
            txtcropname = view.findViewById(R.id.txtcropname); 
            txtcropvariety = view.findViewById(R.id.txtcropvariety);
            txtcropmanufacturer = view.findViewById(R.id.txtcropmanufacturer);
			 
        }
    }

    // To use our custom list item layout, we override the method onCreateViewHolder(...). //In this example, the layout file is called crops_list_recycler_view_layout.xml.
	// Create new views (invoked by the layout manager).
	//inflate rows.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_crop_recycler_view_row_layout, parent, false);
        return new ViewHolder(view);
    }

    // In the onBindViewHolder(...), we actually set the views' contents. We get the used model by finding it in the List at the given position and then set image and name on the ViewHolder's views.
	//populate row with data.
    @Override
    public void onBindViewHolder(search_crops_recyclerview_adapter.ViewHolder viewHolder, final int position) {
		if ((position >= getItemCount() - 1)) {
			//call Load() method
			//load();
		}
		// - get element from your dataset at this position
        // - replace the contents of the view with that element
        final search_crop_dto _selected_dto = _lstdtos.get(position);
		 
		viewHolder.txtcropid.setText(String.format("%d", _selected_dto.getdto_Id()));
        viewHolder.txtcropname.setText("crop: " + _selected_dto.getcrop_name()); 
        viewHolder.txtcropvariety.setText("variety: " + _selected_dto.getcrop_variety_name());
		viewHolder.txtcropmanufacturer.setText("manufacturer: " + _selected_dto.getmanufacturer_name());
		
    }

    // We also need to implement getItemCount(), which simply return the List's size.
	// Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return _lstdtos.size();
    }





}
