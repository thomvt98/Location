package com.example.chien.location.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chien.location.R;
import com.example.chien.location.activity.DirectionActivity;
import com.example.chien.location.model.GisTable;
import com.example.chien.location.sqlite.DatabaseHelper;

import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder>
{
    private List<GisTable> gisTables;
    public LocationAdapter(List<GisTable> gisTables) {
        this.gisTables = gisTables;

    }

    @NonNull
    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_location_recyclerview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LocationAdapter.ViewHolder viewHolder, final int i) {
        final GisTable gis = gisTables.get(i);
        viewHolder.tvCode.setText(gis.getCode());
        viewHolder.tvName.setText(gis.getName());
        viewHolder.tvGisdata.setText(gis.getGisdata());
        viewHolder.tvType.setText(gis.getType());
        viewHolder.tvparent.setText(gis.getParent());
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(view.getContext());
                Toast.makeText(view.getContext(), "Xóa thành công : " + gis.getId(), Toast.LENGTH_SHORT).show();
                db.deleteGis(gis.getId());
                removeItem(i);
            }
        });

        viewHolder.btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view.getContext(),DirectionActivity.class);
                intent.putExtra("ID",gis);
                intent.putExtra("CODE",gis.getCode());
                intent.putExtra("NAME",gis.getName());
                intent.putExtra("TYPE",gis.getType());
                intent.putExtra("GISDATA",gis.getGisdata());
                intent.putExtra("PARENT",gis.getParent());
                //SHOW LÀ HIỂN THỊ LÊN BUTTON UPDATE
                intent.putExtra("SHOW",String.valueOf(1));
                view.getContext().startActivity(intent);

            }
        });
    }
    @Override
    public int getItemCount() {
        return gisTables.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvCode, tvName, tvGisdata, tvType,tvparent;
        public Button btnUp,btnDelete;
        public ViewHolder(View v) {
            super(v);
            tvCode = v.findViewById(R.id.tv_code);
            tvName = v.findViewById(R.id.tv_name);
            tvGisdata = v.findViewById(R.id.tv_gisdata);
            tvType = v.findViewById(R.id.tv_type);
            btnUp=v.findViewById(R.id.btnUp);
            btnDelete=v.findViewById(R.id.btnDelete);
            tvparent=v.findViewById(R.id.tv_parent);
        }
    }
    public void removeItem(int position){
        gisTables.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, gisTables.size());
    }
}
