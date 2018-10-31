package com.example.chien.location.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Button;

import com.example.chien.location.R;
import com.example.chien.location.adapter.SosAdapter;
import com.example.chien.location.model.SosInfo;
import com.example.chien.location.sqlite.DatabaseSos;
import com.example.chien.location.touchHelper.SimpleItemTouchHelperCallback;
import com.example.chien.location.touchHelper.SimpleItemTouchHelperCallbackSos;

import java.util.ArrayList;
import java.util.List;

public class SosListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SosAdapter sosAdapter;
    private List<SosInfo> sosInfo;
    private DatabaseSos db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos_list);

        init();
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallbackSos(sosAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

    }

    private void init() {
        recyclerView = findViewById(R.id.recycler_viewsos);
        sosInfo = new ArrayList<>();
        db = new DatabaseSos(this);
        List<SosInfo> list = db.getAllSOS();
        sosAdapter = new SosAdapter(list);
        recyclerView.setAdapter(sosAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
