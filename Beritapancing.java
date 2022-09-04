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
import com.asah.pemancingan.adapter.listberita;
import com.asah.pemancingan.adapter.listinformasi;
import com.asah.pemancingan.modal.Databerita;
import com.asah.pemancingan.modal.Datainformasi;
import com.asah.pemancingan.modal.Responseberita;
import com.asah.pemancingan.modal.Responseinformasi;
import com.asah.pemancingan.rest.ApiRequest;
import com.asah.pemancingan.rest.Retro_server;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Beritapancing extends AppCompatActivity {


    Toolbar toolbar;
    private RecyclerView rv_berita;
    private RecyclerView.Adapter adData;
    private RecyclerView.LayoutManager layData;
    private List<Databerita> listberita = new ArrayList<>();
    private SwipeRefreshLayout refresh_berita;
    private TextView tanggal;
    private LottieAnimationView loadingberita, not_berita;
    private SwipeRefreshLayout refresh;
    private String tanggalpancing;
    private String tglpancing;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beritapancing);

        androidx.appcompat.widget.Toolbar toolbarobat = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbarberita);
        setSupportActionBar(toolbarobat);

//Set icon to toolbar
        toolbarobat.setNavigationIcon(R.drawable.back);
        toolbarobat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Beritapancing.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        rv_berita = findViewById(R.id.rv_berita);
        refresh_berita = findViewById(R.id.refresh_berita);
        loadingberita = findViewById(R.id.loadingberita);
        not_berita = findViewById(R.id.not_berita);


        rv_berita.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        display_berita();

        refresh_berita.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh_berita.setRefreshing(true);
                display_berita();
                refresh_berita.setRefreshing(false);
            }
        });

        Runnable refresh1 = new Runnable() {
            @Override
            public void run() {
                // data request

                display_berita();
                loadingberita.setVisibility(GONE);

            }
        };
        handler.postDelayed(refresh1, 3000);


    }

    private void display_berita() {
        ApiRequest showObat = Retro_server.konekRtrofit().create(ApiRequest.class);
        Call<Responseberita> tampilinfo = showObat.ardberita();
        tampilinfo.enqueue(new Callback<Responseberita>() {
            @Override
            public void onResponse(Call<Responseberita> call, Response<Responseberita> response) {
                String pesan = response.body().getPesan();

                listberita = response.body().getData();

                if (response.body().getData() == null) {
                    not_berita.setVisibility(View.VISIBLE);
                    Toast.makeText(Beritapancing.this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                } else {

                    adData = new listberita(Beritapancing.this, listberita);
                    rv_berita.setAdapter(adData);
                    adData.notifyDataSetChanged();
                    not_berita.setVisibility(GONE);

                }

            }

            @Override
            public void onFailure(Call<Responseberita> call, Throwable t) {
                Toast.makeText(Beritapancing.this, "Gagal Terhubung Dengan Server", Toast.LENGTH_SHORT).show();

            }
        });
    }
}