package com.asah.pemancingan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Boking extends AppCompatActivity {

    private EditText edtanggal;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private CardView caripancing;
    public static final int CAMERA_REQUEST = 100;
    public static final int STORAGE_REQUEST = 101;
    String[]cameraPermission;
    String[]storagePermission;

    private String mediaPath;
    private String postPath;
    Uri uri;
    private Bitmap bitmap;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boking);


        androidx.appcompat.widget.Toolbar toolbarobat = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbarbok);
        setSupportActionBar(toolbarobat);

//Set icon to toolbar
        toolbarobat.setNavigationIcon(R.drawable.back);
        toolbarobat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Boking.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        edtanggal = findViewById(R.id.edtanggal);
        caripancing = findViewById(R.id.caripancing);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        edtanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tanggal_post();
            }

        });

        caripancing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tgl = edtanggal.getText().toString();

               if(TextUtils.isEmpty(tgl)){
                   edtanggal.setError("Kolom Tidak Boleh Kosong");
                   edtanggal.requestFocus();
               }else {
                   Bundle bundle = new Bundle();

                   bundle.putString("tanggal", edtanggal.getText().toString());

                   Intent intent1 = new Intent(Boking.this, Pemesanan.class);
                   intent1.putExtra("userdata", bundle);
                   startActivity(intent1);
               }


            }
        });
    }

    private void tanggal_post() {

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);

                edtanggal.setText(dateFormatter.format(newDate.getTime()));

            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }


}