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
import com.asah.pemancingan.adapter.listmancing;
import com.asah.pemancingan.modal.DataModel;
import com.asah.pemancingan.modal.ResponsePaket;
import com.asah.pemancingan.modal.ResponsePelanggan;
import com.asah.pemancingan.rest.ApiRequest;
import com.asah.pemancingan.rest.Retro_server;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Pemesanan extends AppCompatActivity {


    Toolbar toolbar;
    private RecyclerView rv_mancing;
    private RecyclerView.Adapter adData;
    private RecyclerView.LayoutManager layData;
    private List<DataModel> listmanc = new ArrayList<>();
    private SwipeRefreshLayout refresh_mancing;
    private TextView tanggal;
   private LottieAnimationView loadingpemesanan, not_mancing;
   private SwipeRefreshLayout refresh;
    private String tanggalpancing;
    private String tglpancing;
    final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);

        androidx.appcompat.widget.Toolbar toolbarobat = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbarpesan);
        setSupportActionBar(toolbarobat);

//Set icon to toolbar
        toolbarobat.setNavigationIcon(R.drawable.back);
        toolbarobat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pemesanan.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        rv_mancing = findViewById(R.id.rv_mancing);
        refresh_mancing = findViewById(R.id.refresh_mancing);
        tanggal = findViewById(R.id.kodetanggal);
        loadingpemesanan = findViewById(R.id.loadingpemesanan);
        not_mancing = findViewById(R.id.not_mancing);


        rv_mancing.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        if (getIntent().getBundleExtra("userdata")!=null) {
            Bundle bundle = getIntent().getBundleExtra("userdata");
            tanggalpancing = bundle.getString("tanggal");
        }
            tanggal.setText("" + tanggalpancing);
            tglpancing = tanggal.getText().toString();


            refresh_mancing.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh_mancing.setRefreshing(true);
                    display_pancing();
                    refresh_mancing.setRefreshing(false);
                }
            });

        Runnable refresh1 = new Runnable() {
            @Override
            public void run() {
                // data request

                display_pancing();
                loadingpemesanan.setVisibility(GONE);

            }
        };
        handler.postDelayed(refresh1, 3000);



    }


    @Override
    protected void onResume() {
        super.onResume();
        display_pancing();
    }
    private void display_pancing() {
        ApiRequest showObat = Retro_server.konekRtrofit().create(ApiRequest.class);
        Call<ResponsePaket> tampilkeranjang = showObat.ardGetPancing(tanggalpancing);
        tampilkeranjang.enqueue(new Callback<ResponsePaket>() {
            @Override
            public void onResponse(Call<ResponsePaket> call, Response<ResponsePaket> response) {
                String pesan = response.body().getPesan();

                listmanc = response.body().getData();

               if (response.body().getData() == null){
                     not_mancing.setVisibility(View.VISIBLE);
                     Toast.makeText(Pemesanan.this,"Data Tidak Ditemukan",Toast.LENGTH_SHORT).show();
                }else{

                    adData = new listmancing(Pemesanan.this, listmanc);
                    rv_mancing.setAdapter(adData);
                    adData.notifyDataSetChanged();
                    not_mancing.setVisibility(GONE);

                }

            }

            @Override
            public void onFailure(Call<ResponsePaket> call, Throwable t) {
                Toast.makeText(Pemesanan.this,"Gagal Terhubung Dengan Server", Toast.LENGTH_SHORT).show();

            }
        });

    }
}