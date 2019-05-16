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

public class categoriesautocompleteadapter extends ArrayAdapter<categorydto> {
    Context context;
    int resource, textViewResourceId;
    List<categorydto> items, tempItems, suggestions;

    public categoriesautocompleteadapter(Context context, int resource, int textViewResourceId, List<categorydto> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<categorydto>(items); // this makes the difference.
        suggestions = new ArrayList<categorydto>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.category_autocomplete_row_layout, parent, false);
        }
        categorydto categorydto = items.get(position);
        if (categorydto != null) {
            TextView lblcategoryname = view.findViewById(R.id.lblcategoryname_auto_row);
            if (lblcategoryname != null)
                lblcategoryname.setText(categorydto.getcategory_name());
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
            String str = ((categorydto) resultValue).getcategory_name();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (categorydto categorydto : tempItems) {
                    if
                            (categorydto.getcategory_name().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(categorydto);
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
            List<categorydto> filterList = (ArrayList<categorydto>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (categorydto categorydto : filterList) {
                    add(categorydto);
                    notifyDataSetChanged();
                }
            }
        }
    };
}