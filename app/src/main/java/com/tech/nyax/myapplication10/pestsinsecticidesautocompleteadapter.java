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

public class pestsinsecticidesautocompleteadapter extends ArrayAdapter<pestinsecticidedto> {
    Context context;
    int resource, textViewResourceId;
    List<pestinsecticidedto> items, tempItems, suggestions;

    public pestsinsecticidesautocompleteadapter(Context context, int resource, int textViewResourceId, List<pestinsecticidedto> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<pestinsecticidedto>(items); // this makes the difference.
        suggestions = new ArrayList<pestinsecticidedto>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.pestinsecticide_autocomplete_row_layout, parent, false);
        }
        pestinsecticidedto pestinsecticidedto = items.get(position);
        if (pestinsecticidedto != null) {
            TextView lbl_auto_row = view.findViewById(R.id.lblpestinsecticide_auto_row);
            if (lbl_auto_row != null)
                lbl_auto_row.setText(pestinsecticidedto.getpestinsecticide_name());
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
            String str = ((pestinsecticidedto) resultValue).getpestinsecticide_name();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (pestinsecticidedto pestinsecticidedto : tempItems) {
                    if
                            (pestinsecticidedto.getpestinsecticide_name().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(pestinsecticidedto);
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
            List<pestinsecticidedto> filterList = (ArrayList<pestinsecticidedto>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (pestinsecticidedto pestinsecticidedto : filterList) {
                    add(pestinsecticidedto);
                    notifyDataSetChanged();
                }
            }
        }
    };
}