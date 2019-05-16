package com.tech.nyax.myapplication10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class manufacturersautocompleteadapter extends ArrayAdapter<manufacturerdto> {
    Context context;
    int resource, textViewResourceId;
    List<manufacturerdto> items, tempItems, suggestions;

    public manufacturersautocompleteadapter(Context context, int resource, int textViewResourceId, List<manufacturerdto> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<manufacturerdto>(items); // this makes the difference.
        suggestions = new ArrayList<manufacturerdto>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.manufacturer_autocomplete_row_layout, parent, false);
        }
        manufacturerdto manufacturerdto = items.get(position);
        if (manufacturerdto != null) {
            TextView lbl_auto_row = view.findViewById(R.id.lblmanufacturername_auto_row);
            if (lbl_auto_row != null)
                lbl_auto_row.setText(manufacturerdto.getmanufacturer_name());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((manufacturerdto) resultValue).getmanufacturer_name();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (manufacturerdto manufacturerdto : tempItems) {
                    if
                            (manufacturerdto.getmanufacturer_name().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(manufacturerdto);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<manufacturerdto> filterList = (ArrayList<manufacturerdto>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (manufacturerdto manufacturerdto : filterList) {
                    add(manufacturerdto);
                    notifyDataSetChanged();
                }
            }
        }
    };
}