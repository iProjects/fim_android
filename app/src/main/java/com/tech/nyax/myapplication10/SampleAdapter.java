//package com.tech.nyax.myapplication10;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.wings.example.recycleview.MainActivity;
//
//import java.util.ArrayList;
//
//public class SampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    Context context;
//    private ArrayList<String> arrayList;
//    private static onClickListner onclicklistner;
//    private static final int VIEW_HEADER = 0;
//    private static final int VIEW_NORMAL = 1;
//    private View headerView;
//
//    public SampleAdapter(Context context) {
//        this.context = context;
//        arrayList = MainActivity.arrayList;
//    }
//
//    public class HeaderViewHolder extends RecyclerView.ViewHolder {
//        public HeaderViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
//            View.OnLongClickListener {
//        TextView txt_pos;
//        SampleAdapter sampleAdapter;
//
//        public ItemViewHolder(View itemView, SampleAdapter sampleAdapter) {
//            super(itemView);
//            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
//            txt_pos = (TextView) itemView.findViewById(R.id.txt_pos);
//            this.sampleAdapter = sampleAdapter;
//            itemView.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//            onclicklistner.onItemClick(getAdapterPosition(), v);
//        }
//
//        @Override
//        public boolean onLongClick(View v) {
//            onclicklistner.onItemLongClick(getAdapterPosition(), v);
//            return true;
//        }
//    }
//
//    public void setOnItemClickListener(onClickListner onclicklistner) {
//        SampleAdapter.onclicklistner = onclicklistner;
//    }
//
//    public void setHeader(View v) {
//        this.headerView = v;
//    }
//
//    public interface onClickListner {
//        void onItemClick(int position, View v);
//
//        void onItemLongClick(int position, View v);
//    }
//
//    @Override
//    public int getItemCount() {
//        return arrayList.size() + 1;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position == 0 ? VIEW_HEADER : VIEW_NORMAL;
//    }
//
//    @SuppressLint("InflateParams")
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        if (viewType == VIEW_HEADER) {
//            return new HeaderViewHolder(headerView);
//        } else {
//            View view =
//                    LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_recycler_row_sample_item,
//                            viewGroup, false);
//            return new ItemViewHolder(view, this);
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
//        if (viewHolder.getItemViewType() == VIEW_HEADER) {
//            return;
//        } else {
//            ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
//            itemViewHolder.txt_pos.setText(arrayList.get(position - 1));
//        }
//    }
//}