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

public class cropsdiseasesautocompleteadapter extends ArrayAdapter<cropdiseasedto> {
    Context context;
    int resource, textViewResourceId;
    List<cropdiseasedto> items, tempItems, suggestions;

    public cropsdiseasesautocompleteadapter(Context context, int resource, int textViewResourceId, List<cropdiseasedto> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<cropdiseasedto>(items); // this makes the difference.
        suggestions = new ArrayList<cropdiseasedto>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.crop_disease_autocomplete_row_layout, parent, false);
        }
        cropdiseasedto cropdiseasedto = items.get(position);
        if (cropdiseasedto != null) {
            TextView lbl_auto_row = view.findViewById(R.id.lblcropdiseasename_auto_row);
            if (lbl_auto_row != null)
                lbl_auto_row.setText(cropdiseasedto.getcropdisease_name());
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
            String str = ((cropdiseasedto) resultValue).getcropdisease_name();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (cropdiseasedto cropdiseasedto : tempItems) {
                    if
                            (cropdiseasedto.getcropdisease_name().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(cropdiseasedto);
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
            List<cropdiseasedto> filterList = (ArrayList<cropdiseasedto>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (cropdiseasedto cropdiseasedto : filterList) {
                    add(cropdiseasedto);
                    notifyDataSetChanged();
                }
            }
        }
    };
}