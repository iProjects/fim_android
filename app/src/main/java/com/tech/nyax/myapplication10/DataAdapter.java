package com.tech.nyax.myapplication10;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<cropdto> studentList;
    //The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public DataAdapter(List<cropdto> students, RecyclerView recyclerView) {
        studentList = students;
		
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
			
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)
                    recyclerView.getLayoutManager();
					
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
				
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
					
                    super.onScrolled(recyclerView, dx, dy);
					
                    totalItemCount = linearLayoutManager.getItemCount();
					
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
					
                    if (!loading && totalItemCount <= (lastVisibleItem +
                            visibleThreshold)) {
								
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
						
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return studentList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent,
                    false);
            vh = new StudentViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item,
                    parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StudentViewHolder) {
            cropdto _dto = studentList.get(position);
            ((StudentViewHolder) holder).txtcropid.setText(String.format("%d", _dto.getcrop_Id()));
			((StudentViewHolder) holder).txtcropname.setText(_dto.getcrop_name());
			((StudentViewHolder) holder).txtcropstatus.setText(_dto.getcrop_status());
            ((StudentViewHolder) holder)._cropdto = _dto;
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded(boolean state) {
        loading = state;
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtcropid;
        private final TextView txtcropname; 
        private final TextView txtcropstatus; 
		ImageButton btneditcrop;
		ImageButton btndeletecrop;
        public cropdto _cropdto;

        public StudentViewHolder(View view) {
            super(view);
            txtcropid = view.findViewById(R.id.txtcropid);
            txtcropname = view.findViewById(R.id.txtcropname); 
            txtcropstatus = view.findViewById(R.id.txtcropstatus);
            btneditcrop = view.findViewById(R.id.btneditcrop);
            btndeletecrop = view.findViewById(R.id.btndeletecrop);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressBarrecyclerview);

        }
    }
}


