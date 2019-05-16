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
public class manufacturerslistrecyclerviewadapter extends RecyclerView.Adapter<manufacturerslistrecyclerviewadapter.ViewHolder> {
    
	//dataset
    private List<manufacturerdto> _lstdtos;
	private static final String TAG = manufacturerslistrecyclerviewadapter.class.getSimpleName();
    Context _context;

	//abstract method in Recycleview adapter for implementing endless scrolling
	//public abstract void load();

    // The adapter's constructor sets the used dataset:
    public manufacturerslistrecyclerviewadapter(Context context, List<manufacturerdto> _lst_dtos) {
		_context = context;
        _lstdtos = _lst_dtos;
    }

// First, we implement the ViewHolder. It only inherits the default constructor and saves the needed views into some fields:
    public class ViewHolder extends RecyclerView.ViewHolder {
		 
		private final TextView txtmanufacturerid;
        private final TextView txtmanufacturername;
        private final TextView txtmanufacturerstatus;
        private final ImageButton btneditmanufacturer;
        private final ImageButton btndeletemanufacturer;

        public ViewHolder(View view) {
            super(view);
			 
			txtmanufacturerid = view.findViewById(R.id.txtmanufacturerid);
            txtmanufacturername = view.findViewById(R.id.txtmanufacturername);
            txtmanufacturerstatus = view.findViewById(R.id.txtmanufacturerstatus);
            btneditmanufacturer = view.findViewById(R.id.btneditmanufacturer);
            btndeletemanufacturer = view.findViewById(R.id.btndeletemanufacturer);
			
			btneditmanufacturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

				int _pos = getAdapterPosition();
				
				Log.e(TAG, "Element " + _pos + " clicked.");
				 
				final manufacturerdto _dto = _lstdtos.get(_pos);
				
                Log.e(TAG, "id: " + String.format("%d", _dto.getmanufacturer_Id()));
                Log.e(TAG, "name: " + _dto.getmanufacturer_name());
                Log.e(TAG, "status: " + _dto.getmanufacturer_status());
 
                utilz.getInstance(_context).globallogwithtoasthandler("loading edit activity for id: " + _dto.getmanufacturer_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
				
                dataBundle.putString("manufacturerid", String.format("%d", _dto.getmanufacturer_Id()));
 
                // Starting new intent
                Intent intent = new Intent(_context,
                        editmanufactureractivity.class);
                // sending data to next activity
                intent.putExtras(dataBundle);

				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                // starting new activity
                _context.startActivity(intent);

            }
        });
 
		btndeletemanufacturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
				int _pos = getAdapterPosition();
				
				Log.e(TAG, "Element " + _pos + " clicked.");
				 
				final manufacturerdto _dto = _lstdtos.get(_pos);
				
                Log.e(TAG, "id: " + String.format("%d", _dto.getmanufacturer_Id()));
                Log.e(TAG, "name: " + _dto.getmanufacturer_name());
                Log.e(TAG, "status: " + _dto.getmanufacturer_status());

                utilz.getInstance(_context).globallogwithtoasthandler("loading delete activity for id: " + _dto.getmanufacturer_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
				
                dataBundle.putString("manufacturerid", String.format("%d", _dto.getmanufacturer_Id()));
 
                // Starting new intent
                Intent intent = new Intent(_context,
                        deletemanufactureractivity.class);
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

    // To use our custom list item layout, we override the method onCreateViewHolder(...). //In this example, the layout file is called manufacturers_list_recycler_view_layout.xml.
	// Create new views (invoked by the layout manager).
	//inflate rows.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manufacturers_list_recycler_view_row_layout, parent, false);
        return new ViewHolder(view);
    }

    // In the onBindViewHolder(...), we actually set the views' contents. We get the used model by finding it in the List at the given position and then set image and name on the ViewHolder's views.
	//populate row with data.
    @Override
    public void onBindViewHolder(manufacturerslistrecyclerviewadapter.ViewHolder viewHolder, final int position) {
		if ((position >= getItemCount() - 1)) {
			//call Load() method
			//load();
		}
		// - get element from your dataset at this position
        // - replace the contents of the view with that element
        final manufacturerdto _dto = _lstdtos.get(position);
		 
		viewHolder.txtmanufacturerid.setText("id: " + String.format("%d", _dto.getmanufacturer_Id()));
        viewHolder.txtmanufacturername.setText("name: " + _dto.getmanufacturer_name());
        viewHolder.txtmanufacturerstatus.setText("status: " + _dto.getmanufacturer_status());
  
    }

    // We also need to implement getItemCount(), which simply return the List's size.
	// Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return _lstdtos.size();
    }





}
