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

public class cropsvarietiesautocompleteadapter extends ArrayAdapter<cropvarietydto> {
    Context context;
    int resource, textViewResourceId;
    List<cropvarietydto> items, tempItems, suggestions;

    public cropsvarietiesautocompleteadapter(Context context, int resource, int textViewResourceId, List<cropvarietydto> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<cropvarietydto>(items); // this makes the difference.
        suggestions = new ArrayList<cropvarietydto>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.crop_variety_autocomplete_row_layout, parent, false);
        }
        cropvarietydto cropvarietydto = items.get(position);
        if (cropvarietydto != null) {
            TextView lbl_auto_row = view.findViewById(R.id.lblcropvarietyname_auto_row);
            if (lbl_auto_row != null)
                lbl_auto_row.setText(cropvarietydto.getcropvariety_name());
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
            String str = ((cropvarietydto) resultValue).getcropvariety_name();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (cropvarietydto cropvarietydto : tempItems) {
                    if
                            (cropvarietydto.getcropvariety_name().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(cropvarietydto);
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
            List<cropvarietydto> filterList = (ArrayList<cropvarietydto>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (cropvarietydto cropvarietydto : filterList) {
                    add(cropvarietydto);
                    notifyDataSetChanged();
                }
            }
        }
    };
}