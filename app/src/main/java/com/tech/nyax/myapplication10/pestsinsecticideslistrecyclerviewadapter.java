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
public class pestsinsecticideslistrecyclerviewadapter extends RecyclerView.Adapter<pestsinsecticideslistrecyclerviewadapter.ViewHolder> {
    
	//dataset
    private List<pestinsecticidedto> _lstdtos;
	private static final String TAG = pestsinsecticideslistrecyclerviewadapter.class.getSimpleName();
    Context _context;

	//abstract method in Recycleview adapter for implementing endless scrolling
	//public abstract void load();

    // The adapter's constructor sets the used dataset:
    public pestsinsecticideslistrecyclerviewadapter(Context context, List<pestinsecticidedto> _lst_dtos) {
		_context = context;
        _lstdtos = _lst_dtos;
    }

// First, we implement the ViewHolder. It only inherits the default constructor and saves the needed views into some fields:
    public class ViewHolder extends RecyclerView.ViewHolder {
		 
		private final TextView txtpestinsecticideid;
        private final TextView txtpestinsecticidename;
        private final TextView txtpestinsecticidecategory;
        private final TextView txtpestinsecticidestatus;
		private final TextView txtpestinsecticidediseasepest;
		private final TextView txtpestinsecticidemanufacturer;
        private final ImageButton btneditpestinsecticide;
        private final ImageButton btndeletepestinsecticide;

        public ViewHolder(View view) {
            super(view);
			 
			txtpestinsecticideid = view.findViewById(R.id.txtpestinsecticideid);
            txtpestinsecticidename = view.findViewById(R.id.txtpestinsecticidename);
            txtpestinsecticidecategory = view.findViewById(R.id.txtpestinsecticidecategory);
            txtpestinsecticidestatus = view.findViewById(R.id.txtpestinsecticidestatus); 
			txtpestinsecticidediseasepest = view.findViewById(R.id.txtpestinsecticidediseasepest);
            txtpestinsecticidemanufacturer = view.findViewById(R.id.txtpestinsecticidemanufacturer); 
            btneditpestinsecticide = view.findViewById(R.id.btneditpestinsecticide);
            btndeletepestinsecticide = view.findViewById(R.id.btndeletepestinsecticide);
			
			btneditpestinsecticide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

				int _pos = getAdapterPosition();
				
				Log.e(TAG, "Element " + _pos + " clicked.");
				 
				final pestinsecticidedto _dto = _lstdtos.get(_pos);
				
                Log.e(TAG, "id: " + String.format("%d", _dto.getpestinsecticide_Id()));
                Log.e(TAG, "name: " + _dto.getpestinsecticide_name());
                Log.e(TAG, "category: " + _dto.getpestinsecticide_category());
                Log.e(TAG, "status: " + _dto.getpestinsecticide_status());
				Log.e(TAG, "disease/pest: " + _dto.getpestinsecticide_crop_disease_name());
				Log.e(TAG, "manufacturer: " + _dto.getpestinsecticide_manufacturer_name());
			 
                utilz.getInstance(_context).globallogwithtoasthandler("loading edit activity for id: " + _dto.getpestinsecticide_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
				
                dataBundle.putString("pestinsecticideid", String.format("%d", _dto.getpestinsecticide_Id())); 
				
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
 
		btndeletepestinsecticide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
				int _pos = getAdapterPosition();
				
				Log.e(TAG, "Element " + _pos + " clicked.");
				 
				final pestinsecticidedto _dto = _lstdtos.get(_pos);
				
                Log.e(TAG, "id: " + String.format("%d", _dto.getpestinsecticide_Id()));
                Log.e(TAG, "name: " + _dto.getpestinsecticide_name());
                Log.e(TAG, "category: " + _dto.getpestinsecticide_category());
                Log.e(TAG, "status: " + _dto.getpestinsecticide_status());
				Log.e(TAG, "disease/pest: " + _dto.getpestinsecticide_crop_disease_name());
				Log.e(TAG, "manufacturer: " + _dto.getpestinsecticide_manufacturer_name());
				
                utilz.getInstance(_context).globallogwithtoasthandler("loading delete activity for id: " + _dto.getpestinsecticide_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
				
                dataBundle.putString("pestinsecticideid", String.format("%d", _dto.getpestinsecticide_Id()));
 
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
		 
        }
    }

    // To use our custom list item layout, we override the method onCreateViewHolder(...). //In this example, the layout file is called pestsinsecticides_list_recycler_view_layout.xml.
	// Create new views (invoked by the layout manager).
	//inflate rows.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pestsinsecticides_list_recycler_view_row_layout, parent, false);
        return new ViewHolder(view);
    }

    // In the onBindViewHolder(...), we actually set the views' contents. We get the used model by finding it in the List at the given position and then set image and name on the ViewHolder's views.
	//populate row with data.
    @Override
    public void onBindViewHolder(pestsinsecticideslistrecyclerviewadapter.ViewHolder viewHolder, final int position) {
		if ((position >= getItemCount() - 1)) {
			//call Load() method
			//load();
		}
		// - get element from your dataset at this position
        // - replace the contents of the view with that element
        final pestinsecticidedto _dto = _lstdtos.get(position);
		 
		viewHolder.txtpestinsecticideid.setText("id: " + String.format("%d", _dto.getpestinsecticide_Id()));
        viewHolder.txtpestinsecticidename.setText("name: " + _dto.getpestinsecticide_name());
        viewHolder.txtpestinsecticidecategory.setText("category: " + _dto.getpestinsecticide_category());
        viewHolder.txtpestinsecticidestatus.setText("status: " + _dto.getpestinsecticide_status()); 
		viewHolder.txtpestinsecticidediseasepest.setText("disease/pest: " + _dto.getpestinsecticide_crop_disease_name());
		viewHolder.txtpestinsecticidemanufacturer.setText("manufacturer: " + _dto.getpestinsecticide_manufacturer_name());
  
    }

    // We also need to implement getItemCount(), which simply return the List's size.
	// Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return _lstdtos.size();
    }





}
