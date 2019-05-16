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
public class cropsvarietieslistrecyclerviewadapter extends RecyclerView.Adapter<cropsvarietieslistrecyclerviewadapter.ViewHolder> {
    
	//dataset
    private List<cropvarietydto> _lstdtos;
	private static final String TAG = cropsvarietieslistrecyclerviewadapter.class.getSimpleName();
    Context _context;

	//abstract method in Recycleview adapter for implementing endless scrolling
	//public abstract void load();

    // The adapter's constructor sets the used dataset:
    public cropsvarietieslistrecyclerviewadapter(Context context, List<cropvarietydto> _lst_dtos) {
		_context = context;
        _lstdtos = _lst_dtos;
    }

// First, we implement the ViewHolder. It only inherits the default constructor and saves the needed views into some fields:
    public class ViewHolder extends RecyclerView.ViewHolder {
		 
		private final TextView txtcropvarietyid;
        private final TextView txtcropvarietyname;
        private final TextView txtcropvarietystatus;
		private final TextView txtcropvarietycrop;
        private final TextView txtcropvarietymanufacturer;
        private final ImageButton btneditcropvariety;
        private final ImageButton btndeletecropvariety;

        public ViewHolder(View view) {
            super(view);
			 
			txtcropvarietyid = view.findViewById(R.id.txtcropvarietyid);
            txtcropvarietyname = view.findViewById(R.id.txtcropvarietyname);
            txtcropvarietystatus = view.findViewById(R.id.txtcropvarietystatus);
			txtcropvarietycrop = view.findViewById(R.id.txtcropvarietycrop);
            txtcropvarietymanufacturer = view.findViewById(R.id.txtcropvarietymanufacturer);
            btneditcropvariety = view.findViewById(R.id.btneditcropvariety);
            btndeletecropvariety = view.findViewById(R.id.btndeletecropvariety);
			
			btneditcropvariety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

				int _pos = getAdapterPosition();
				
				Log.e(TAG, "Element " + _pos + " clicked.");
				 
				final cropvarietydto _dto = _lstdtos.get(_pos);
				
                Log.e(TAG, "id: " + String.format("%d", _dto.getcropvariety_Id()));
                Log.e(TAG, "name: " + _dto.getcropvariety_name());
                Log.e(TAG, "status: " + _dto.getcropvariety_status());
				Log.e(TAG, "crop: " + _dto.getcropvariety_crop_name());
                Log.e(TAG, "manufacturer: " + _dto.getcropvariety_manufacturer_name());
 
                utilz.getInstance(_context).globallogwithtoasthandler("loading edit activity for id: " + _dto.getcropvariety_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
				
                dataBundle.putString("cropvarietyid", String.format("%d", _dto.getcropvariety_Id())); 
				
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
 
		btndeletecropvariety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
				int _pos = getAdapterPosition();
				
				Log.e(TAG, "Element " + _pos + " clicked.");
				 
				final cropvarietydto _dto = _lstdtos.get(_pos);
				
                Log.e(TAG, "id: " + String.format("%d", _dto.getcropvariety_Id()));
                Log.e(TAG, "name: " + _dto.getcropvariety_name());
                Log.e(TAG, "status: " + _dto.getcropvariety_status());
				Log.e(TAG, "crop: " + _dto.getcropvariety_crop_name());
                Log.e(TAG, "manufacturer: " + _dto.getcropvariety_manufacturer_name());
 
                utilz.getInstance(_context).globallogwithtoasthandler("loading delete activity for id: " + _dto.getcropvariety_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
				
                dataBundle.putString("cropvarietyid", String.format("%d", _dto.getcropvariety_Id()));
 
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
		 
        }
    }

    // To use our custom list item layout, we override the method onCreateViewHolder(...). //In this example, the layout file is called crops_varieties_list_recycler_view_layout.xml.
	// Create new views (invoked by the layout manager).
	//inflate rows.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crops_varieties_list_recycler_view_row_layout, parent, false);
        return new ViewHolder(view);
    }

    // In the onBindViewHolder(...), we actually set the views' contents. We get the used model by finding it in the List at the given position and then set image and name on the ViewHolder's views.
	//populate row with data.
    @Override
    public void onBindViewHolder(cropsvarietieslistrecyclerviewadapter.ViewHolder viewHolder, final int position) {
		if ((position >= getItemCount() - 1)) {
			//call Load() method
			//load();
		}
		// - get element from your dataset at this position
        // - replace the contents of the view with that element
        final cropvarietydto _dto = _lstdtos.get(position);
		 
		viewHolder.txtcropvarietyid.setText("id: " + String.format("%d", _dto.getcropvariety_Id()));
        viewHolder.txtcropvarietyname.setText("name: " + _dto.getcropvariety_name());
        viewHolder.txtcropvarietystatus.setText("status: " + _dto.getcropvariety_status());
		viewHolder.txtcropvarietycrop.setText("crop: " + _dto.getcropvariety_crop_name());
		viewHolder.txtcropvarietymanufacturer.setText("manufacturer: " + _dto.getcropvariety_manufacturer_name());
  
    }

    // We also need to implement getItemCount(), which simply return the List's size.
	// Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return _lstdtos.size();
    }





}
