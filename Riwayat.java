package com.asah.pemancingan;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.asah.pemancingan.adapter.listboking;
import com.asah.pemancingan.adapter.listmancing;
import com.asah.pemancingan.modal.DataModel;
import com.asah.pemancingan.modal.Databoking;
import com.asah.pemancingan.modal.ResponseBoking;
import com.asah.pemancingan.rest.ApiRequest;
import com.asah.pemancingan.rest.Retro_server;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Riwayat extends AppCompatActivity {


    Toolbar toolbar;
    private RecyclerView rv_riwayat;
    private RecyclerView.Adapter adData;
    private RecyclerView.LayoutManager layData;
    private List<Databoking> listbok = new ArrayList<>();
    private SwipeRefreshLayout refresh_riwayat;
    private TextView userriwayat;
    private LottieAnimationView loadingriwayat, not_riwayat;
    private SwipeRefreshLayout refresh;
    private String user;
    SharedPreferences sharedPreferences;
    private String username;
    private static final int REQUEST_CODE = 10;
    Context context;
    private static final String filename = "login";
    private static final String Username = "username";
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);


        androidx.appcompat.widget.Toolbar toolbarobat = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbarriwayat);
        setSupportActionBar(toolbarobat);

//Set icon to toolbar
        toolbarobat.setNavigationIcon(R.drawable.back);
        toolbarobat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Riwayat.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        rv_riwayat = findViewById(R.id.rv_riwayat);
        refresh_riwayat = findViewById(R.id.refresh_riwayat);
        userriwayat = findViewById(R.id.usernameriwayat);
        loadingriwayat= findViewById(R.id.loadingriwayat);
        not_riwayat = findViewById(R.id.not_riwayat);


        rv_riwayat.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        sharedPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(Username)) {
           userriwayat.setText("" + sharedPreferences.getString(Username, ""));
        }

        username = userriwayat.getText().toString();

        refresh_riwayat.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh_riwayat.setRefreshing(true);
                display_riwayat();
                refresh_riwayat.setRefreshing(false);
            }
        });

        Runnable refresh1 = new Runnable() {
            @Override
            public void run() {
                // data request

                display_riwayat();
                loadingriwayat.setVisibility(GONE);

            }
        };
        handler.postDelayed(refresh1, 3000);


    }

    private void display_riwayat() {


        ApiRequest ardedit = Retro_server.konekRtrofit().create(ApiRequest.class);
        Call<ResponseBoking> update = ardedit.ardRiwayat(username);

        update.enqueue(new Callback<ResponseBoking>() {
            @Override
            public void onResponse(Call<ResponseBoking> call, retrofit2.Response<ResponseBoking> response) {
                String pesan = response.body().getPesan();

                listbok = response.body().getData();

                if (response.body().getData() == null){
                    not_riwayat.setVisibility(View.VISIBLE);
                    Toast.makeText(Riwayat.this,"Data Tidak Ditemukan",Toast.LENGTH_SHORT).show();
                }else{

                    adData = new listboking(Riwayat.this, listbok);
                    rv_riwayat.setAdapter(adData);
                    adData.notifyDataSetChanged();
                    not_riwayat.setVisibility(GONE);

                }
                Toast.makeText(Riwayat.this, "Riwayat Boking Pemancingan", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onFailure(Call<ResponseBoking> call, Throwable t) {
                Toast.makeText(Riwayat.this,"Gagal Terhubung Dengan Server", Toast.LENGTH_SHORT).show();
            }

        });


    }
}