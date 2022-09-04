package com.asah.pemancingan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Konfirmasi extends AppCompatActivity {

    private TextView kode_konfir,nama_konfirm,ttl,jmlhkonfi,namabayar;
    SharedPreferences sharedPreferences;
    private static final int REQUEST_CODE = 10;
    Context context;
    private static final String filename = "login";
    private static final String Username = "username";
    private  String kode,jumlah,bayar,harga;
    private CardView cetak;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi);


        kode_konfir = findViewById(R.id.kode_konfir);


        androidx.appcompat.widget.Toolbar toolbarobat = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbarkonfirm);
        setSupportActionBar(toolbarobat);

//Set icon to toolbar
        toolbarobat.setNavigationIcon(R.drawable.back);
        toolbarobat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Konfirmasi.this);
                builder.setMessage("Anda Harus Cetak Tiket Terlebih Dahulu");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();

                        bundle.putString("kode", kode_konfir.getText().toString());



                        Intent intent1 = new Intent(Konfirmasi.this, CetakTiket.class);
                        intent1.putExtra("userdata", bundle);
                        startActivity(intent1);
                        finishAffinity();
                    }
                });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        nama_konfirm = findViewById(R.id.nama_pelkonfirm);
        ttl = findViewById(R.id.ttl);
        namabayar = findViewById(R.id.namabayar);
        jmlhkonfi = findViewById(R.id.jmlhkonfir);
        cetak = findViewById(R.id.cetak);
        ttl = findViewById(R.id.totalbayar);


        sharedPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(Username)) {
            nama_konfirm.setText("" + sharedPreferences.getString(Username, ""));
        }

        if (getIntent().getBundleExtra("userdata")!=null) {
            Bundle bundle = getIntent().getBundleExtra("userdata");
           kode = bundle.getString("kode");
            jumlah = bundle.getString("jumlah");
            bayar = bundle.getString("bayar");
            harga = bundle.getString("harga");
        }
       kode_konfir.setText("" + kode);
        jmlhkonfi.setText(""+jumlah);
        namabayar.setText(""+bayar);
        ttl.setText(""+harga);


        cetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                bundle.putString("kode", kode_konfir.getText().toString());



                Intent intent1 = new Intent(Konfirmasi.this, CetakTiket.class);
                intent1.putExtra("userdata", bundle);
                startActivity(intent1);
                finishAffinity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Konfirmasi.this);
        builder.setMessage("Anda Harus Cetak Tiket Terlebih Dahulu");
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle bundle = new Bundle();

                bundle.putString("kode", kode_konfir.getText().toString());



                Intent intent1 = new Intent(Konfirmasi.this, CetakTiket.class);
                intent1.putExtra("userdata", bundle);
                startActivity(intent1);
                finishAffinity();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}