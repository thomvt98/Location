package com.example.chien.location.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chien.location.model.GisTable;
import com.example.chien.location.model.NodeGis;
import com.example.chien.location.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectionAdapter extends RecyclerView.Adapter<DirectionAdapter.ViewHolder> {
    private List<NodeGis> nodeGis;
    public DirectionAdapter(List<NodeGis> nodeGis) {
        this.nodeGis = nodeGis;
    }

    @NonNull
    @Override
    public DirectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_direction_listview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        NodeGis node=nodeGis.get(i);
        viewHolder.tvLat.setText(String.valueOf(node.getLat()));
        viewHolder.tvLng.setText(String.valueOf(node.getLng()));
    }
    public void swap(int firstPosition, int secondPosition)
    {
        Collections.swap(nodeGis, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
    }
    public void remove(int position) {
        nodeGis.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return nodeGis.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvLat, tvLng;
        public ViewHolder(View v) {
            super(v);
            tvLat = v.findViewById(R.id.tvLat);
            tvLng = v.findViewById(R.id.tvLng);

        }
    }
}
