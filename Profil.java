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
import android.widget.Toast;

import com.asah.pemancingan.modal.DataPelanggan;
import com.asah.pemancingan.modal.ResponsePelanggan;
import com.asah.pemancingan.rest.ApiRequest;
import com.asah.pemancingan.rest.Retro_server;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profil extends AppCompatActivity {

    private String usern,name,almt,tel;
    private TextView namapgn,alamat,telepon;
    private List<DataPelanggan> listPelanggan = new ArrayList<>();
    SharedPreferences sharedPreferences;
    private static final int REQUEST_CODE = 10;
    Context context;
    private TextView usernameprof;
    private static final String filename = "login";
    private static final String Username = "username";
    private String username;
    private CardView keluar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        androidx.appcompat.widget.Toolbar toolbarobat = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbarprof);
        setSupportActionBar(toolbarobat);

//Set icon to toolbar
        toolbarobat.setNavigationIcon(R.drawable.back);
        toolbarobat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profil.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        namapgn = findViewById(R.id.namapgn);
        alamat = findViewById(R.id.alamatprof);
        telepon = findViewById(R.id.teleponprofil);
        usernameprof = findViewById(R.id.usernameprofil);
        keluar = findViewById(R.id.logout);

        sharedPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(Username)) {
            usernameprof.setText("" + sharedPreferences.getString(Username, ""));
        } else {
            Toast.makeText(Profil.this, "Anda Belum Memiliki Akun", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Profil.this, Login.class);
            startActivity(intent);
            finish();
        }

        tampil_profil();

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                editor.commit();
                Intent intent = new Intent(Profil.this, Login.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Anda Telah Keluar", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        });
    }


    private void tampil_profil() {
        username = usernameprof.getText().toString();

        ApiRequest showObat = Retro_server.konekRtrofit().create(ApiRequest.class);
        Call<ResponsePelanggan> tampilKonfirmasi = showObat.ardProfil(username);
        tampilKonfirmasi.enqueue(new Callback<ResponsePelanggan>() {
            @Override
            public void onResponse(Call<ResponsePelanggan> call, Response<ResponsePelanggan> response) {


                String pesan = response.body().getPesan();
                listPelanggan = response.body().getData();

                if (response.body().getData() == null) {
                    Toast.makeText(Profil.this, "Data Tidak Ditemukan ", Toast.LENGTH_SHORT).show();
                } else {
                    namapgn.setText("" +listPelanggan.get(0).getNama_pelanggan());
                    alamat.setText("" +listPelanggan.get(0).getAlamat());
                    telepon.setText("" +listPelanggan.get(0).getTelepon());
                }
            }

            @Override
            public void onFailure(Call<ResponsePelanggan> call, Throwable t) {
                Toast.makeText(Profil.this,"Gagal Terhubung Dengan Server", Toast.LENGTH_SHORT).show();

            }
        });

    }


}