package com.asah.pemancingan;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.asah.pemancingan.modal.Databerita;
import com.asah.pemancingan.modal.Datainformasi;

import java.util.ArrayList;
import java.util.List;

public class detailberita extends AppCompatActivity {

    Toolbar toolbar;
    private List<Databerita> listberita = new ArrayList<>();
    private int id;
    private TextView id_detailberita,judulberita,detailberita,detailpenulisberita;
    private String jdl,berita,pns;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailberita);

        androidx.appcompat.widget.Toolbar toolbarobat = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbardetailberita);
        setSupportActionBar(toolbarobat);

//Set icon to toolbar
        toolbarobat.setNavigationIcon(R.drawable.back);
        toolbarobat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(detailberita.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });


        id_detailberita = findViewById(R.id.id_detailberita);
        judulberita = findViewById(R.id.juduldetailberita);
        detailberita = findViewById(R.id.detailberita);
        detailpenulisberita = findViewById(R.id.detailpenuliberita);


        if (getIntent().getBundleExtra("userdata")!=null) {
            Bundle bundle = getIntent().getBundleExtra("userdata");
            id = bundle.getInt("id_berita");
            jdl = bundle.getString("judul");
            berita = bundle.getString("berita");
            pns = bundle.getString("penulis");
        }

        id_detailberita.setText(String.valueOf(id));
        judulberita.setText("Judul : " + jdl);
        detailberita.setText("" + berita);
        detailpenulisberita.setText("Penulis : " + pns);


    }
}