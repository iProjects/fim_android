package com.tech.nyax.myapplication10;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
 

public class searchautocompleteadapter extends ArrayAdapter<searchautocompletedto> {
    Context context;
    int resource, textViewResourceId;
    List<searchautocompletedto> items, tempItems, suggestions;

    public searchautocompleteadapter(Context context, int resource, int textViewResourceId, List<searchautocompletedto> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<>(items); // this makes the difference.
        suggestions = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.crop_autocomplete_row_layout, parent, false);
        }
        searchautocompletedto searchautocompletedto = items.get(position);
        if (searchautocompletedto != null) {
            TextView lblcropname = view.findViewById(R.id.lblcropname_auto_row);
            if (lblcropname != null)
                lblcropname.setText(searchautocompletedto.getdto_value());
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
            String str = ((searchautocompletedto) resultValue).getdto_value();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (searchautocompletedto searchautocompletedto : tempItems) {
                    if
                            (searchautocompletedto.getdto_value().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(searchautocompletedto);
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
            List<searchautocompletedto> filterList = (ArrayList<searchautocompletedto>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (searchautocompletedto searchautocompletedto : filterList) {
                    add(searchautocompletedto);
                    notifyDataSetChanged();
                }
            }
        }
    };
}