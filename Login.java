package com.asah.pemancingan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.asah.pemancingan.modal.ResponsePelanggan;
import com.asah.pemancingan.rest.ApiRequest;
import com.asah.pemancingan.rest.Retro_server;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private EditText username,password;
    private CardView login,daftarpelanggan;
    private CheckBox showpass;
    SharedPreferences sharedPreferences;

    private String user,passpelanggan;
    private static final String filename = "login";
    private static final String Username = "username";
    private static final String Admin = "admin";
    private static final String adminPassword = "password";
    private static final String Password = "password";
    private LottieAnimationView loadinglogin;
    private static final int REQUEST_CODE = 10;
    private String userpelanggan;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        username = findViewById(R.id.usernamelogin);
        password = findViewById(R.id.passwordlogin);
        showpass = findViewById(R.id.showPasslogin);
        login = findViewById(R.id.login);
        daftarpelanggan = findViewById(R.id.daftarlogin);
        showpass = findViewById(R.id.showPasslogin);
        loadinglogin = findViewById(R.id.loadinglogin);



        sharedPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(Username)){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);

        }else if (getIntent().getBundleExtra("usernamepelanggan")!=null) {
            Bundle bundle = getIntent().getBundleExtra("usernamepelanggan");
            userpelanggan = bundle.getString("username");
            username.setText(userpelanggan);
        }else{
            Toast.makeText(Login.this, "Belum Memiliki Akun", Toast.LENGTH_SHORT).show();
        }


        daftarpelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Pendaftaran.class);
                startActivity(intent);
            }
        });

        showpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showpass.isChecked()){
                    //Saat Checkbox dalam keadaan Checked, maka password akan di tampilkan
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    //Jika tidak, maka password akan di sembuyikan
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userpelanggan = username.getText().toString();
                String passpelanggan = password.getText().toString();

                if (TextUtils.isEmpty(userpelanggan)){
                    username.setError("Kolom Username Tidak Boleh Kosong");
                    username.requestFocus();
                }else if(TextUtils.isEmpty(passpelanggan)){
                    username.setError("Kolom Password Tidak Boleh Kosong");
                    username.requestFocus();
                }
                else{
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Username,userpelanggan);
                    editor.putString(Password,passpelanggan);
                    editor.commit();
                    login_pelanggan();

                }
            }
        });
    }

    private void login_pelanggan() {
        user = username.getText().toString();
        passpelanggan = password.getText().toString();

        ApiRequest logUser = Retro_server.konekRtrofit().create(ApiRequest.class);
        Call<ResponsePelanggan> tampilObat = logUser.ardLogin(user,passpelanggan);
        tampilObat.enqueue(new Callback<ResponsePelanggan>() {
            @Override
            public void onResponse(Call<ResponsePelanggan> call, Response<ResponsePelanggan> response) {
                String pesan = response.body().getPesan();

                if(response.body().getPesan() != null){
                    loadinglogin.setVisibility(View.VISIBLE);
                    ResponsePelanggan pelangganModel = response.body();
                    if (pelangganModel.getKode() == 1){
                        loadinglogin.setVisibility(View.INVISIBLE);

                        Toast.makeText(Login.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }else{

                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setMessage("Login Gagal!")
                                .setNegativeButton("Login Kembali", null).create().show();
                        username.setText("");
                        password.setText("");

                        sharedPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        editor.commit();

                    }
                }else{
                    Toast.makeText(Login.this, "Belum Memiliki Akun", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Pendaftaran.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResponsePelanggan> call, Throwable t) {

                Toast.makeText(Login.this,"Gagal Terhubung Dengan Server", Toast.LENGTH_SHORT).show();

                sharedPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                editor.commit();
            }
        });
    }


}