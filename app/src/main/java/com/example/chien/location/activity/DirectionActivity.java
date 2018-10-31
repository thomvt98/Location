package com.example.chien.location.activity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chien.location.adapter.DirectionAdapter;
import com.example.chien.location.adapter.LocationAdapter;
import com.example.chien.location.api.GisTableReponse;
import com.example.chien.location.api.IGisServer;
import com.example.chien.location.api.RetrofitFactory;
import com.example.chien.location.gps.LocationTrack;
import com.example.chien.location.model.NodeGis;
import com.example.chien.location.R;
import com.example.chien.location.model.GisTable;
import com.example.chien.location.sqlite.DatabaseHelper;
import com.example.chien.location.touchHelper.SimpleItemTouchHelperCallback;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DirectionActivity extends AppCompatActivity {
    //    Context context = this;
    private Button btnLocation, btnSave, btnIntent, btnUpdate;
    private TextView edtCode;
    private TextView edtName;
    private Spinner spinners, spinParent;
    private String item;
    private RecyclerView recyclerViewDirection;

    DatabaseHelper dh;
    private ArrayList<NodeGis> arrList;
    private DirectionAdapter adapter;
    private NodeGis coor;
    private Gson gson;
    private GisTable gisTable;

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        init();
        initAccessLocation();
        initSpinner();
        eventsButton();
        Reciver();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
                gisTable = (GisTable) getIntent().getSerializableExtra("ID");
                gisTable.setType(spinners.getSelectedItem().toString());
                gisTable.setParent(spinParent.getSelectedItem().toString());
                gisTable.setGisdata(arrList.toString());
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy,HH:mm:ss");
                String timeUpdate =mdformat.format(calendar.getTime());
                gisTable.setUpdateTime(timeUpdate);
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                db.update(gisTable);
                Toast.makeText(getApplication(), "Sửa thành công", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();

            }
        });
    }

    private void initAccessLocation() {
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    private void initSpinner() {
        spinners = findViewById(R.id.spinners);
        spinParent = findViewById(R.id.spinParent);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSpin = ArrayAdapter.createFromResource(this,
                R.array.spinners, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterSpinParent = ArrayAdapter.createFromResource(this,
                R.array.spinParent, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSpin.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        adapterSpinParent.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        // Apply the adapter to the spinner
        spinners.setAdapter(adapterSpin);
        spinParent.setAdapter(adapterSpinParent);
    }

    private void eventsButton() {
        btnIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
                startActivity(intent);
            }
        });

        spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view, final int position, final long id) {
                item = parent.getItemAtPosition(position).toString();

                btnLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        check(position);
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gson = new Gson();

                        String code = edtCode.getText().toString();
                        String name = edtName.getText().toString();
                        String parentChild= String.valueOf(spinParent.getSelectedItem());
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy,HH:mm:ss");
                        String strDate =mdformat.format(calendar.getTime());
                        gisTable = new GisTable(code, name, arrList.toString(),item,parentChild,strDate,"0");

                        // Add data into to Sqlite
                        dh.addData(gisTable);
                        gisTable.setName(name);
                        gisTable.setCode(code);
                        gisTable.setGisdata(arrList.toString());
                        gisTable.setType(item);
                        gisTable.setCreateTime(strDate);
                        gisTable.setUpdateTime("0");

                        // Use Retrofit post data from app into api
                        IGisServer iGisServer = RetrofitFactory.getService();
                        try {
                            NodeGis nodeGis = new NodeGis();
                            nodeGis.setLat((float) 1.25511);
                            nodeGis.setLng((float) 100.25511);
                            gisTable.getList().add(nodeGis);
                            Log.e("HH", new Gson().toJson(gisTable));

                            Call<GisTableReponse> service1 = iGisServer.listCheck(name, "", item, "", gson.toJson(gisTable.getList()));
                            service1.enqueue(new Callback<GisTableReponse>() {
                                @Override
                                public void onResponse(Call<GisTableReponse> call, Response<GisTableReponse> response) {
                                }

                                @Override
                                public void onFailure(Call<GisTableReponse> call, Throwable t) {
                                }
                            });
                            Toast.makeText(DirectionActivity.this,"Thêm thành công ",Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                        }

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void init() {
        edtCode = findViewById(R.id.edtCode);
        edtName = findViewById(R.id.edtName);
        btnLocation = findViewById(R.id.btnLocation);
        btnIntent = findViewById(R.id.btnIntent);
        recyclerViewDirection=findViewById(R.id.recycler_viewDiretion);
        btnSave = findViewById(R.id.btnSave);
        btnUpdate = findViewById(R.id.btnUpdate);
        arrList = new ArrayList<>();
        coor = new NodeGis();
        adapter = new DirectionAdapter(arrList);
        recyclerViewDirection.setAdapter(adapter);
        recyclerViewDirection.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerViewDirection);
        dh = new DatabaseHelper(this);
    }

    public void check(int number) {
        locationTrack = new LocationTrack(DirectionActivity.this);
        if (locationTrack.canGetLocation()) {
            float longitude = (float) locationTrack.getLongitude();
            float latitude = (float) locationTrack.getLatitude();
            coor.setLat(latitude);
            coor.setLng(longitude);

            switch (number) {
                case 0:
                    arrList.clear();
                    arrList.add(coor);
                    break;
                case 1:
                    if (arrList.size() > 2) {
                        arrList.clear();
                        arrList.add(coor);
                    } else if (!arrList.contains(coor)) {
                        arrList.add(coor);
                    }
                    break;
                case 2:
                    arrList.add(coor);
                    break;
            }
            adapter.notifyDataSetChanged();
        } else {
            locationTrack.showSettingsAlert();
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(DirectionActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void Reciver() {
        btnUpdate.setVisibility(View.INVISIBLE);
        try {
            int show = Integer.parseInt(String.valueOf(getIntent().getStringExtra("SHOW")));
            if(show==1)
            {
                btnUpdate.setVisibility(View.VISIBLE);
                String txtCode = String.valueOf(getIntent().getSerializableExtra("CODE"));
                edtCode.setText(txtCode);
                edtCode.setEnabled(false);
                String txtName = String.valueOf(getIntent().getSerializableExtra("NAME"));
                edtName.setText(txtName);
                edtName.setEnabled(false);
                String txtType = String.valueOf(getIntent().getSerializableExtra("TYPE"));
                if (txtType.equals("Polygon")) {
                    spinners.setSelection(2);
                } else if (txtType.equals("Line")) {
                    spinners.setSelection(1);
                } else {
                    spinners.setSelection(0);
                }
                String parent=String.valueOf(getIntent().getSerializableExtra("PARENT"));
                if(parent=="Child")
                {
                    spinParent.setSelection(1);
                }else{spinParent.setSelection(0);}
                try {
                    String arrGis = String.valueOf(getIntent().getSerializableExtra("GISDATA"));
                    String strArray[] = arrGis.trim().replaceAll("[\\[\\](){}]", "").split(",");
                    for (int i = 0; i < strArray.length; i++) {
                        if (i % 2 == 0) {
                            coor = new NodeGis(Float.parseFloat(strArray[i]), Float.parseFloat(strArray[i + 1]));
                            Log.d("KAME", String.valueOf(coor));
                            arrList.add(coor);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }catch (Exception e)
        {
        }

    }

 /*   @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }*/
}