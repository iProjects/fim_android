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
public class categorieslistrecyclerviewadapter extends RecyclerView.Adapter<categorieslistrecyclerviewadapter.ViewHolder> {
    
	//dataset
    private List<categorydto> _lstdtos;
	private static final String TAG = categorieslistrecyclerviewadapter.class.getSimpleName();
    Context _context;

	//abstract method in Recycleview adapter for implementing endless scrolling
	//public abstract void load();

    // The adapter's constructor sets the used dataset:
    public categorieslistrecyclerviewadapter(Context context, List<categorydto> _lst_dtos) {
		_context = context;
        _lstdtos = _lst_dtos;
    }

// First, we implement the ViewHolder. It only inherits the default constructor and saves the needed views into some fields:
    public class ViewHolder extends RecyclerView.ViewHolder {
		 
		private final TextView txtcategoryid;
        private final TextView txtcategoryname; 
        private final TextView txtcategorystatus; 
		private final ImageButton btneditcategory;
		private final ImageButton btndeletecategory;

        public ViewHolder(View view) {
            super(view);
			 
			txtcategoryid = view.findViewById(R.id.txtcategoryid);
            txtcategoryname = view.findViewById(R.id.txtcategoryname); 
            txtcategorystatus = view.findViewById(R.id.txtcategorystatus);
            btneditcategory = view.findViewById(R.id.btneditcategory);
            btndeletecategory = view.findViewById(R.id.btndeletecategory);
			
			btneditcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

				int _pos = getAdapterPosition();
				
				Log.e(TAG, "Element " + _pos + " clicked.");
				 
				final categorydto _dto = _lstdtos.get(_pos);
				
                Log.e(TAG, "id: " + String.format("%d", _dto.getcategory_Id()));
                Log.e(TAG, "name: " + _dto.getcategory_name()); 
                Log.e(TAG, "status: " + _dto.getcategory_status());

                utilz.getInstance(_context).globallogwithtoasthandler("loading edit activity for id: " + _dto.getcategory_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
                dataBundle.putString("categoryid", String.format("%d", _dto.getcategory_Id())); 
				
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
 
		btndeletecategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
				int _pos = getAdapterPosition();
				
				Log.e(TAG, "Element " + _pos + " clicked.");
				 
				final categorydto _dto = _lstdtos.get(_pos);
				
                Log.e(TAG, "id: " + String.format("%d", _dto.getcategory_Id()));
                Log.e(TAG, "name: " + _dto.getcategory_name()); 
                Log.e(TAG, "status: " + _dto.getcategory_status());

                utilz.getInstance(_context).globallogwithtoasthandler("loading delete activity for id: " + _dto.getcategory_Id(), TAG, 1, 1);

                Bundle dataBundle = new Bundle();
                dataBundle.putString("categoryid", String.format("%d", _dto.getcategory_Id()));
 
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
		 
        }
    }

    // To use our custom list item layout, we override the method onCreateViewHolder(...). //In this example, the layout file is called crops_list_recycler_view_layout.xml.
	// Create new views (invoked by the layout manager).
	//inflate rows.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_list_recycler_view_row_layout, parent, false);
        return new ViewHolder(view);
    }

    // In the onBindViewHolder(...), we actually set the views' contents. We get the used model by finding it in the List at the given position and then set image and name on the ViewHolder's views.
	//populate row with data.
    @Override
    public void onBindViewHolder(categorieslistrecyclerviewadapter.ViewHolder viewHolder, final int position) {
		if ((position >= getItemCount() - 1)) {
			//call Load() method
			//load();
		}
		// - get element from your dataset at this position
        // - replace the contents of the view with that element
        final categorydto _dto = _lstdtos.get(position);
		 
		viewHolder.txtcategoryid.setText(String.format("%d", _dto.getcategory_Id()));
        viewHolder.txtcategoryname.setText("name: " + _dto.getcategory_name()); 
        viewHolder.txtcategorystatus.setText("status: " + _dto.getcategory_status());
  
    }

    // We also need to implement getItemCount(), which simply return the List's size.
	// Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return _lstdtos.size();
    }





}
