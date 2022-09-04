package com.asah.pemancingan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.asah.pemancingan.modal.DataPelanggan;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    SliderView sliderView;
    final int min = 1000;
    final int max = 9999;
    int[] images={R.drawable.gambar1,
            R.drawable.gambar2,
            R.drawable.gambar3,
            R.drawable.gambar4,
            R.drawable.gambar5

    };

    private LottieAnimationView loadingber,menunav;
    final Handler handler = new Handler();
    private RelativeLayout halpancing,halinfo,halnews;
    private BottomNavigationView navigation;
    private  NavigationView nav;
    private TextView namauser;
    private List<DataPelanggan> listKaryawan = new ArrayList<>();
    private List<DataPelanggan> listKar = new ArrayList<>();
    SharedPreferences sharedPreferences;
    private static final int REQUEST_CODE = 10;
    Context context;
    private static final String filename = "login";
    private static final String Username = "username";
    private  String userp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DrawerLayout drawerLayout = findViewById(R.id.drawerlayout);


        sliderView= findViewById(R.id.imagesilder);
        slideadapter slideradapter = new slideadapter(images);
        sliderView.setSliderAdapter(slideradapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

        loadingber = findViewById(R.id.loadingberanda);
        halpancing = findViewById(R.id.halamanboking);
        halinfo = findViewById(R.id.halamaninfo);
        halnews = findViewById(R.id.halamannews);
        menunav = findViewById(R.id.menunav);
        nav = findViewById(R.id.navigationview);
        navigation = findViewById(R.id.navigation);
        namauser = findViewById(R.id.namauser);


        sharedPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(Username)) {
            namauser.setText("" + sharedPreferences.getString(Username, ""));
        } else {
            Toast.makeText(MainActivity.this, "Anda Belum Memiliki Akun", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }
        userp = namauser.getText().toString();



        Runnable refresh1 = new Runnable() {
            @Override
            public void run() {
                // data request

                loadingber.setVisibility(View.GONE);
            }
        };
        handler.postDelayed(refresh1, 3000);


        menunav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationview);
        navigationView.setItemIconTintList(null);



        halpancing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Boking.class);
                startActivity(intent);
            }
        });

        halinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Infopemancingan.class);
                startActivity(intent);
            }
        });


        halnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Beritapancing.class);
                startActivity(intent);
            }
        });

      nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              switch (item.getItemId()){
                  case R.id.navi_profil:
                      Bundle bundle = new Bundle();

                      bundle.putString("username", namauser.getText().toString());
                      Intent intent1 = new Intent(MainActivity.this, Profil.class);
                      intent1.putExtra("userdata", bundle);
                      startActivity(intent1);
                      finishAffinity();
                      break;
                  case R.id.navi_home:
                      Intent intentbackup2 = new Intent(MainActivity.this, MainActivity.class);
                      startActivity(intentbackup2);
                      finishAffinity();
                      break;
                  case R.id.navi_riwayat:
                      Bundle bundle1 = new Bundle();

                      bundle1.putString("user", namauser.getText().toString());
                      Intent intent2 = new Intent(MainActivity.this, Riwayat.class);
                      intent2.putExtra("userdata", bundle1);
                      startActivity(intent2);
                      finishAffinity();
                      break;
                  case R.id.navi_logout:
                      sharedPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
                      SharedPreferences.Editor editor = sharedPreferences.edit();
                      editor.clear();
                      editor.apply();
                      editor.commit();
                      Intent intent = new Intent(MainActivity.this, Login.class);
                      startActivity(intent);
                      Toast.makeText(getApplicationContext(), "Anda Telah Keluar", Toast.LENGTH_SHORT).show();
                      finishAffinity();
                      break;
                  default:

              }
              return false;
          }
      });

        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_profil:
                        Intent intentbackup = new Intent(MainActivity.this, Profil.class);
                        startActivity(intentbackup);
                        navigation.cancelLongPress();
                        finishAffinity();
                        break;
                    case R.id.nav_home:
                        Runnable refresh2 = new Runnable() {
                            @Override
                            public void run() {
                                handler.postDelayed(this, 3000);

                                loadingber.setVisibility(View.GONE);
                            }
                        };
                        handler.postDelayed(refresh2, 3000);
                        break;
                    case R.id.nav_riwayat:
                        Intent intentriwayat = new Intent(MainActivity.this, Riwayat.class);
                        startActivity(intentriwayat);
                        finishAffinity();
                        break;
                    default:

                }
                return false;
            }
        });



    }




}