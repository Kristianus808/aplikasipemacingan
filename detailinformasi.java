package com.asah.pemancingan;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.asah.pemancingan.modal.DataModel;
import com.asah.pemancingan.modal.Datainformasi;

import java.util.ArrayList;
import java.util.List;

public class detailinformasi extends AppCompatActivity {

    Toolbar toolbar;
    private List<Datainformasi> listinfo = new ArrayList<>();
    private int id;
    private TextView id_detailinformasi,juduldetail,detailinfo,detailpenulis;
    private String jdl,info,pns;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailinformasi);


        androidx.appcompat.widget.Toolbar toolbarobat = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbardetailinfo);
        setSupportActionBar(toolbarobat);

//Set icon to toolbar
        toolbarobat.setNavigationIcon(R.drawable.back);
        toolbarobat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(detailinformasi.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        id_detailinformasi = findViewById(R.id.id_informasi);
        juduldetail = findViewById(R.id.juduldetaiinfo);
        detailinfo = findViewById(R.id.detailinfo);
        detailpenulis = findViewById(R.id.detailpenulis);


        if (getIntent().getBundleExtra("userdata")!=null) {
            Bundle bundle = getIntent().getBundleExtra("userdata");
            id = bundle.getInt("id_informasi");
            jdl = bundle.getString("judul");
            info = bundle.getString("info");
            pns = bundle.getString("penulis");
        }

        id_detailinformasi.setText(String.valueOf(id));
        juduldetail.setText("Judul : " + jdl);
        detailinfo.setText("" + info);
        detailpenulis.setText("Penulis : " + pns);

    }
}