package com.example.chien.location.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chien.location.R;
import com.example.chien.location.activity.SosActivity;
import com.example.chien.location.model.SosInfo;
import com.example.chien.location.model.SosMedia;
import com.example.chien.location.sqlite.DatabaseSos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SosAdapter  extends RecyclerView.Adapter<SosAdapter.ViewHolder> {
     List<SosInfo> sosInfos;


    public SosAdapter(List<SosInfo> sosInfos) {
        this.sosInfos = sosInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_sos_recyclerview, viewGroup, false);
        return new SosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final SosInfo sosInfo = sosInfos.get(i);

        viewHolder.tvTitle.setText(sosInfo.getSos_title());
        viewHolder.tvNote.setText(sosInfo.getSos_note());
        viewHolder.tvCode.setText(sosInfo.getSos_code());
        viewHolder.tvPriority.setText(sosInfo.getPriority());
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    DatabaseSos db = new DatabaseSos(view.getContext());
                    db.deleteSos(sosInfo.getSos_code());
                    Toast.makeText(view.getContext(), "Xóa thành công : " + sosInfo.getId(), Toast.LENGTH_SHORT).show();
                    removeItem(i);


            }
        });
        viewHolder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view.getContext(),SosActivity.class);
                intent.putExtra("DATA",sosInfo);
                intent.putExtra("TITLE",sosInfo.getSos_title());
                intent.putExtra("NOTE",sosInfo.getSos_note());
                intent.putExtra("Priority",sosInfo.getPriority());
                intent.putExtra("SOS_CODE",sosInfo.getSos_code());
                DatabaseSos db = new DatabaseSos(view.getContext());
                SosMedia sosMedia = db.getMedia(sosInfo.getSos_code());
                intent.putExtra("IMAGE_PATH", sosMedia.getFile_path());
                intent.putExtra("ID_MEDIA", sosMedia.getIdsos_media());
                intent.putExtra("SHOW",String.valueOf(1));
                view.getContext().startActivity(intent);

                           }
        });



    }

    @Override
    public int getItemCount() {
        return sosInfos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCode, tvPriority, tvTitle, tvNote,tvimg;
        public Button btnUpdate,btnDelete;
        public ViewHolder(View v) {
            super(v);
            tvCode = v.findViewById(R.id.rvCode);
            tvPriority = v.findViewById(R.id.rvPriority);
            tvTitle = v.findViewById(R.id.rvTitle);
            tvNote = v.findViewById(R.id.rvNote);
            btnUpdate=v.findViewById(R.id.btnUpDate);
            btnDelete=v.findViewById(R.id.btnDelete);
        }
        }


    public void swap(int firstPosition, int secondPosition)
    {
        Collections.swap(sosInfos, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
    }
    public void remove(int position) {
        sosInfos.remove(position);
        notifyItemRemoved(position);
    }
    public void removeItem(int position){
        sosInfos.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, sosInfos.size());
    }
    }

