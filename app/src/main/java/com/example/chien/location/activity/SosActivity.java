package com.example.chien.location.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chien.location.R;
import com.example.chien.location.adapter.SosAdapter;
import com.example.chien.location.api.BaseRepose;
import com.example.chien.location.api.IGisServer;
import com.example.chien.location.gps.LocationTrack;
import com.example.chien.location.model.GisTable;
import com.example.chien.location.model.NodeGis;
import com.example.chien.location.model.SosInfo;
import com.example.chien.location.model.SosMedia;
import com.example.chien.location.sqlite.DatabaseHelper;
import com.example.chien.location.sqlite.DatabaseSos;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SosActivity extends AppCompatActivity {
    private TextView txtSosCode;
    private Button btnSave, btnIntent, btnUpdate;
    private EditText edtTitle, edtNote;
    private RadioButton rdCao, rdThap, rdTrungBinh;
    private ImageView pic1, pic2, pic3;
    private ArrayList<SosInfo> list;
    private SosInfo sosInfo;
    private RadioGroup rdGroup;
    private String priority = "Cao";
    private static final int PICK_IMAGE = 14;
    private static final int REQUEST_PHOTO = 3;
    private static final int MY_CAMERA_REQUEST_CODE = 2;

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;

    DatabaseSos db;
    private int selected;
    private Uri contentURI;
    String uri1, uri2, uri3;
    private SosMedia sosMedia;
    IGisServer iGisServer;

    public static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz|!£$%&/=@#";
    public static Random RANDOM = new Random();

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }
        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        init();
        eventsButton();
        Reciver();
    }

    private void init() {
        btnSave = findViewById(R.id.btnSave);
        btnIntent = findViewById(R.id.btnIntent);
        txtSosCode = findViewById(R.id.txtSosCode);
        edtTitle = findViewById(R.id.edtTitle);
        edtNote = findViewById(R.id.edtNote);
        rdCao = findViewById(R.id.rdCao);
        rdThap = findViewById(R.id.rdThap);
        rdTrungBinh = findViewById(R.id.rdTrungBinh);
        rdGroup = findViewById(R.id.rdGroup);
        pic1 = findViewById(R.id.pic1);
        pic2 = findViewById(R.id.pic2);
        pic3 = findViewById(R.id.pic3);
        db = new DatabaseSos(this);
        btnUpdate = findViewById(R.id.btnupdate);

    }

    private void eventsButton() {
        pic1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                selected = 1;
                camera();
            }
        });
        pic2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                selected = 2;
                camera();
            }


        });
        pic3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                selected = 3;
                camera();
            }
        });


        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                int checkedRadioId = radioGroup.getCheckedRadioButtonId();
                if (checkedRadioId == R.id.rdCao) {
                    priority = "Cao";
                } else if (checkedRadioId == R.id.rdThap) {
                    priority = "Thap";
                } else {
                    priority = "Trung Binh";
                }

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edtTitle.getText().toString();
                String note = edtNote.getText().toString();
                String soscode = txtSosCode.getText().toString();
                String filepath = uri1 + ", " + uri2 + ", " + uri3;
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy,HH:mm:ss");
                String createtime = mdformat.format(calendar.getTime());
                locationTrack = new LocationTrack(SosActivity.this);
                if (locationTrack.canGetLocation()) {
                    float longitude = (float) locationTrack.getLongitude();
                    float latitude = (float) locationTrack.getLatitude();

                    String gisdata = "[" + latitude + "," + longitude + "]";
                    sosInfo = new SosInfo(soscode, note, gisdata, title, priority, createtime, "Test001");
                    sosMedia = new SosMedia(soscode, filepath);

                    // Add data into to Sqlite
                    db.addData(sosInfo);
                    sosInfo.setSos_code(soscode);
                    sosInfo.setSos_note(note);
                    sosInfo.setGisdata(gisdata);
                    sosInfo.setSos_title(title);
                    sosInfo.setPriority(priority);
                    sosInfo.setSos_createdTime(createtime);

                    sosInfo.setCreatedby("Test001");
                    //
                    db.addSOSMedia(sosMedia);
                    sosMedia.setSos_code(soscode);
                    sosMedia.setFile_path(filepath);
                    Toast.makeText(SosActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    edtNote.setText("");
                    edtTitle.setText("");
                    pic1.setImageResource(R.drawable.image_deful);
                    pic2.setImageResource(R.drawable.image_deful);
                    pic3.setImageResource(R.drawable.image_deful);
                    txtSosCode.setText(randomString(8));
                }
            }
        });

        btnIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), SosListActivity.class);
                    startActivity(intent);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SosActivity.this, SosListActivity.class);
                sosInfo=(SosInfo)getIntent().getSerializableExtra("DATA");
                sosInfo.setSos_note(edtNote.getText().toString());
                sosInfo.setSos_title(edtTitle.getText().toString());
                sosInfo.setPriority(priority);

                sosMedia = new SosMedia();
                sosMedia.setIdsos_media(getIntent().getIntExtra("ID_MEDIA", 0));
                sosMedia.setFile_path(uri1 + ", " + uri2 + ", " + uri3);

                DatabaseSos db = new DatabaseSos(SosActivity.this);
                db.update(sosInfo);
                db.updateMedia(sosMedia);
                Toast.makeText(getApplication(), "Sửa thành công", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && data != null) {
            contentURI = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(SosActivity.this.getContentResolver(), contentURI);
                switch (selected) {
                    case 1:
                        pic1.setImageBitmap(bitmap);
                        uri1 = contentURI.toString();
                        break;
                    case 2:
                        pic2.setImageBitmap(bitmap);
                        uri2 = contentURI.toString();
                        break;
                    case 3:
                        pic3.setImageBitmap(bitmap);
                        uri3 = contentURI.toString();
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            switch (selected) {
                case 1:
                    pic1.setImageBitmap(bitmap);
                    contentURI = getImageUri(SosActivity.this, bitmap);
                    uri1 = contentURI.toString();
                    break;
                case 2:
                    pic2.setImageBitmap(bitmap);
                    contentURI = getImageUri(SosActivity.this, bitmap);
                    uri2 = contentURI.toString();
                    break;
                case 3:
                    pic3.setImageBitmap(bitmap);
                    contentURI = getImageUri(SosActivity.this, bitmap);
                    uri3 = contentURI.toString();
                    break;
            }
        }
//
//        File file = new File(pic1.get);
//        RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);


        super.onActivityResult(requestCode, resultCode, data);
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void Reciver() {

        txtSosCode.setText(randomString(8));
        btnUpdate.setVisibility(View.INVISIBLE);
        try {
            int show = Integer.parseInt(getIntent().getStringExtra("SHOW"));
            if (show == 1) {
                try {
                    btnUpdate.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.INVISIBLE);
                    btnIntent.setVisibility(View.INVISIBLE);
                    String txsos_code = String.valueOf(getIntent().getSerializableExtra("SOS_CODE"));
                    txtSosCode.setText(txsos_code);
                    txtSosCode.setEnabled(false);
                    String txtTitle = String.valueOf(getIntent().getSerializableExtra("TITLE"));
                    edtTitle.setText(txtTitle);
                    String txtNode = String.valueOf(getIntent().getSerializableExtra("NOTE"));
                    edtNote.setText(txtNode);
                    String txtPriority = String.valueOf(getIntent().getSerializableExtra("Priority"));
                    if (txtPriority.equals("Trung Binh")) {
                        rdTrungBinh.setChecked(true);
                    } else if (txtPriority.equals("Thap")) {
                        rdThap.setChecked(true);
                    } else rdCao.setChecked(true);
                    String filePath = String.valueOf(getIntent().getSerializableExtra("IMAGE_PATH"));
                    String filePaths[] = filePath.replaceAll("\\s+", "").split(",");
                    pic1.setImageURI(Uri.parse(filePaths[0]));
                    pic2.setImageURI(Uri.parse(filePaths[1]));
                    pic3.setImageURI(Uri.parse(filePaths[2]));
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {

        }
    }

    public void camera() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SosActivity.this);
        builder.setMessage("You want to choose?");
        builder.setCancelable(true);
        builder.setPositiveButton("CAMERA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startCamera();
            }
        });
        builder.setNegativeButton("PHOTO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_IMAGE);
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, MY_CAMERA_REQUEST_CODE);
    }

    public void UploadImage(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // Change base URL to your upload server URL.
        iGisServer = new Retrofit.Builder().baseUrl("http://192.168.0.234:3000").client(client).build().create(IGisServer.class);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {

                        return;
                    }
                    //permission da dc dong y het
                    if (requestCode == MY_CAMERA_REQUEST_CODE) {
                        startCamera();

                    }
                }
             case 2:
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
            }
        }
    private boolean initAccessLocation() {
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        return false;
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
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(SosActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initAccessLocation();
    }
}
