package com.asah.pemancingan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.asah.pemancingan.modal.ResponsePelanggan;
import com.asah.pemancingan.rest.ApiRequest;
import com.asah.pemancingan.rest.Retro_server;

import retrofit2.Call;
import retrofit2.Callback;

public class Pendaftaran extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    private CardView daftar;
    private EditText namapel, username, password, telepon, alamat;
    private CheckBox showPassdaftar;
    private String nama, user, tel, pass, almt;
    private static final String filename = "login";
    private static final String Username = "username";
    private static final String Admin = "admin";
    private static final String adminPassword = "password";
    private static final String Password = "password";

    private static final int REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendaftaran);


        namapel = findViewById(R.id.ednama_pelanggan);
        username = findViewById(R.id.ed_username);
        password = findViewById(R.id.ed_password);
        alamat = findViewById(R.id.ed_alamat);
        telepon = findViewById(R.id.edno_telepon);
        daftar = findViewById(R.id.daftarmasuk);
        showPassdaftar = findViewById(R.id.showPassdaftar);




        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama_k = namapel.getText().toString();
                String user_k = username.getText().toString();
                String telpon_k = telepon.getText().toString();
                String pass_k = password.getText().toString();
                String alamat_k = alamat.getText().toString();


                if(TextUtils.isEmpty(nama_k)){
                    namapel.setError("Kolom Tidak Boleh Kosong");
                    namapel.requestFocus();
                }
                else if(TextUtils.isEmpty(user_k)){
                    username.setError("Kolom Tidak Boleh Kosong");
                    username.requestFocus();
                }
                else if(TextUtils.isEmpty(telpon_k)){
                    telepon.setError("Kolom Tidak Boleh Kosong");
                    telepon.requestFocus();
                }
                else if(TextUtils.isEmpty(pass_k)){
                    password.setError("Kolom Tidak Boleh Kosong");
                    password.requestFocus();
                }
                else if(TextUtils.isEmpty(alamat_k)){
                    alamat.setError("Kolom Tidak Boleh Kosong");
                    alamat.requestFocus();
                }else{
                    DaftarPelanggan();
                }
            }
        });

        showPassdaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showPassdaftar.isChecked()){
                    //Saat Checkbox dalam keadaan Checked, maka password akan di tampilkan
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    //Jika tidak, maka password akan di sembuyikan
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }

    private void DaftarPelanggan() {
        nama = namapel.getText().toString();
        tel = telepon.getText().toString();
        user = username.getText().toString();
        pass = password.getText().toString();
        almt = alamat.getText().toString();

        ApiRequest ardTambah = Retro_server.konekRtrofit().create(ApiRequest.class);
        Call<ResponsePelanggan> simpanData = ardTambah.ardTambahPelanggan( nama, user, pass, almt, tel);

        simpanData.enqueue(new Callback<ResponsePelanggan>() {
            @Override
            public void onResponse(Call<ResponsePelanggan> call, retrofit2.Response<ResponsePelanggan> response) {
                String pesan = response.body().getPesan();

                Bundle bundle = new Bundle();
                bundle.putString("username", username.getText().toString());

                Intent intent = new Intent(Pendaftaran.this, Login.class);
                intent.putExtra("usernamepelanggan",bundle);
                startActivity(intent);
                Toast.makeText(Pendaftaran.this,"Daftar Pelanggan Telah Berhasil", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ResponsePelanggan> call, Throwable t) {
                Toast.makeText(Pendaftaran.this,"Gagal Terhubung Dengan Server", Toast.LENGTH_SHORT).show();
            }
        });
    }

}