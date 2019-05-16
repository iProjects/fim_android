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

public class cropsautocompleteadapter extends ArrayAdapter<cropdto> {
    Context context;
    int resource, textViewResourceId;
    List<cropdto> items, tempItems, suggestions;

    public cropsautocompleteadapter(Context context, int resource, int textViewResourceId, List<cropdto> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<cropdto>(items); // this makes the difference.
        suggestions = new ArrayList<cropdto>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.crop_autocomplete_row_layout, parent, false);
        }
        cropdto cropdto = items.get(position);
        if (cropdto != null) {
            TextView lblcropname = view.findViewById(R.id.lblcropname_auto_row);
            if (lblcropname != null)
                lblcropname.setText(cropdto.getcrop_name());
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
            String str = ((cropdto) resultValue).getcrop_name();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (cropdto cropdto : tempItems) {
                    if
                            (cropdto.getcrop_name().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(cropdto);
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
            List<cropdto> filterList = (ArrayList<cropdto>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (cropdto cropdto : filterList) {
                    add(cropdto);
                    notifyDataSetChanged();
                }
            }
        }
    };
}