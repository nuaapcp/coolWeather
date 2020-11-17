package com.test.coolweather.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.coolweather.R;

import java.util.ArrayList;
import java.util.List;

public class AreaListAdapter extends RecyclerView.Adapter<AreaListAdapter.AreaItemViewHolder> {
    private OnItemClickListener onItemClickListener;
    private List<String> areaList = new ArrayList<>();

    public void setAreaList(List<String> areaList) {
        this.areaList = areaList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AreaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.area_item, parent, false);
        return new AreaItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaItemViewHolder holder, final int position) {
        final String area = areaList.get(position);
        holder.textView.setText(area);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(area);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (areaList.isEmpty()) {
            return 0;
        }
        return areaList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String message);
    }

    public class AreaItemViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public AreaItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView2);
        }
    }
}
