package com.example.chien.location.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.chien.location.R;
import com.example.chien.location.adapter.LocationAdapter;
import com.example.chien.location.model.GisTable;
import com.example.chien.location.sqlite.DatabaseHelper;
import com.example.chien.location.touchHelper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity  {
    private RecyclerView recyclerView;
    private LocationAdapter locAdapter;
    private List<GisTable> gisList;
    private DatabaseHelper dh;
    private GisTable gisTable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        init();

    }
    private void init() {
        recyclerView = findViewById(R.id.recycler_view);
        gisList = new ArrayList<>();
        dh = new DatabaseHelper(this);
        List<GisTable> list = dh.getAllGis();
        locAdapter = new LocationAdapter(list);
        recyclerView.setAdapter(locAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
