package com.asah.pemancingan;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.asah.pemancingan.adapter.listinformasi;
import com.asah.pemancingan.adapter.listmancing;
import com.asah.pemancingan.modal.DataModel;
import com.asah.pemancingan.modal.Datainformasi;
import com.asah.pemancingan.modal.ResponsePaket;
import com.asah.pemancingan.modal.Responseinformasi;
import com.asah.pemancingan.rest.ApiRequest;
import com.asah.pemancingan.rest.Retro_server;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Infopemancingan extends AppCompatActivity {

    Toolbar toolbar;
    private RecyclerView rv_info;
    private RecyclerView.Adapter adData;
    private RecyclerView.LayoutManager layData;
    private List<Datainformasi> listinfo = new ArrayList<>();
    private SwipeRefreshLayout refresh_info;
    private TextView tanggal;
    private LottieAnimationView loadinginfo, not_informasi;
    private SwipeRefreshLayout refresh;
    private String tanggalpancing;
    private String tglpancing;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infopemancingan);

        androidx.appcompat.widget.Toolbar toolbarobat = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbarinfo);
        setSupportActionBar(toolbarobat);

//Set icon to toolbar
        toolbarobat.setNavigationIcon(R.drawable.back);
        toolbarobat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Infopemancingan.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        rv_info = findViewById(R.id.rv_informasi);
        refresh_info = findViewById(R.id.refresh_informasi);
        loadinginfo = findViewById(R.id.loadinginformasi);
        not_informasi = findViewById(R.id.not_informasi);



        rv_info.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        display_info();

        refresh_info.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh_info.setRefreshing(true);
                display_info();
                refresh_info.setRefreshing(false);
            }
        });

        Runnable refresh1 = new Runnable() {
            @Override
            public void run() {
                // data request

                display_info();
                loadinginfo.setVisibility(GONE);

            }
        };
        handler.postDelayed(refresh1, 3000);


    }

    @Override
    protected void onResume () {
        super.onResume();
        display_info();
    }

    private void display_info() {

            ApiRequest showObat = Retro_server.konekRtrofit().create(ApiRequest.class);
            Call<Responseinformasi> tampilinfo = showObat.ardinformasi();
            tampilinfo.enqueue(new Callback<Responseinformasi>() {
                @Override
                public void onResponse(Call<Responseinformasi> call, Response<Responseinformasi> response) {
                    String pesan = response.body().getPesan();

                    listinfo = response.body().getData();

                    if (response.body().getData() == null) {
                        not_informasi.setVisibility(View.VISIBLE);
                        Toast.makeText(Infopemancingan.this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    } else {

                        adData = new listinformasi(Infopemancingan.this, listinfo);
                        rv_info.setAdapter(adData);
                        adData.notifyDataSetChanged();
                        not_informasi.setVisibility(GONE);

                    }

                }

                @Override
                public void onFailure(Call<Responseinformasi> call, Throwable t) {
                    Toast.makeText(Infopemancingan.this, "Gagal Terhubung Dengan Server", Toast.LENGTH_SHORT).show();

                }
            });


    }
}