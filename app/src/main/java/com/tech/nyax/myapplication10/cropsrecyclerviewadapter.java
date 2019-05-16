/* package com.tech.nyax.myapplication10;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.view.menu.MenuView;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

class cropsrecyclerviewadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int EMPTY_VIEW = 77777;
    List<cropdto> datalist = new ArrayList<>();

    cropsrecyclerviewadapter() {
        super();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == EMPTY_VIEW) {
            return new EmptyView(layoutInflater.inflate(R.layout.nothing_yet, parent, false));
        } else {
            return new ItemView(layoutInflater.inflate(R.layout.my_item, parent, false));
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == EMPTY_VIEW) {
            EmptyView emptyView = (EmptyView) holder;
            emptyView.primaryText.setText("No data yet");
            emptyView.secondaryText.setText("You're doing good !");
            emptyView.primaryText.setCompoundDrawablesWithIntrinsicBounds(null, new
                            IconicsDrawable(getActivity()).icon(FontAwesome.Icon.faw_ticket).sizeDp(48).color(Color.DKGRAY),
                    null, null);
        } else {
            ItemView itemView = (ItemView) holder;
// Bind data to itemView
        }
    }

    @Override
    public int getItemCount() {
        return datalist.size() > 0 ? datalist.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (datalist.size() == 0) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }
} */