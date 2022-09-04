package com.asah.pemancingan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.asah.pemancingan.modal.DataModel;
import com.asah.pemancingan.modal.ResponseBoking;
import com.asah.pemancingan.modal.ResponsePaket;
import com.asah.pemancingan.modal.ResponsePelanggan;
import com.asah.pemancingan.modal.Responseberita;
import com.asah.pemancingan.rest.ApiRequest;
import com.asah.pemancingan.rest.Retro_server;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Pembayaran extends AppCompatActivity {


    private TextView kode_trans;
    final int min = 1000;
    final int max = 9999;
    SharedPreferences sharedPreferences;
    private static final int REQUEST_CODE = 10;
    Context context;
    private static final String filename = "login";
    private static final String Username = "username";
    private  String namasesi, hrga ,pesan;
    private int jmlhsesi;
    private TextView nama,jmlhs,harga,jmlhps;
    private ImageView btntambah,btnkuran;
    int quantity = 0;
    private CardView prosesbayar,cancel;
    int Quantity = 0;
    private Spinner pilih_bayar,no_tf;
    private TextView ket_bayar,no_tf2;
    private int idx;
    private TextView idpncing;
    private String kd,idpan,user,nmapan,by,jm,mt,rk;
    private List<DataModel> listmancing;
    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);


        kode_trans = findViewById(R.id.kode_trans);
        nama = findViewById(R.id.nama_pelbayar);
        jmlhs = findViewById(R.id.jumlahbayar);
        harga = findViewById(R.id.totalbayar);
        jmlhps = findViewById(R.id.jmlhpesan);
        btntambah = findViewById(R.id.btntambahkrj);
        btnkuran = findViewById(R.id.btnkurangkrj);
        prosesbayar = findViewById(R.id.prosesbyr);
        pilih_bayar = findViewById(R.id.pilih_bayar);
        no_tf = findViewById(R.id.no_tf);
        no_tf2 = findViewById(R.id.no_tf2);
        idpncing = findViewById(R.id.idpncing);
        ket_bayar = findViewById(R.id.ket_bayar);
        cancel = findViewById(R.id.cancel);


        pilih_bayar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(pilih_bayar.getSelectedItem().equals("")){
                    no_tf2.setVisibility(View.INVISIBLE);
                    no_tf.setVisibility(View.INVISIBLE);

                }
                else if (pilih_bayar.getSelectedItem().equals("PILIH PEMBAYARAN")){
                    no_tf2.setVisibility(View.INVISIBLE);
                    no_tf.setVisibility(View.INVISIBLE);


                }else if(pilih_bayar.getSelectedItem().toString().equals("TRANSFER")){
                    ket_bayar.setText("Silahkan Transfer DiNomor Rekening Dibawah ini Atas Nama Madiasha ");
                    no_tf.setVisibility(View.VISIBLE);
                    no_tf2.setVisibility(View.INVISIBLE);
                    ket_bayar.setVisibility(View.VISIBLE);

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
             prosesbayar.setVisibility(View.GONE);
            }
        });

        no_tf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(no_tf.getSelectedItem().toString().equals("")){
                    prosesbayar.setVisibility(View.GONE);
                }
               else if(no_tf.getSelectedItem().toString().equals("BCA A.N MADIASHA - 6041570447")){
                    no_tf2.setText("BCA A.N MADIASHA - 6041570447");
                    prosesbayar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                prosesbayar.setVisibility(View.GONE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Pembayaran.this,Boking.class);
                startActivity(intent);
                finishAffinity();
            }
        });






        int number = new Random().nextInt((max-min) + 1) +min;

        kode_trans.setText("AXYS"+String.valueOf(number));

        sharedPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(Username)) {
            nama.setText("" + sharedPreferences.getString(Username, ""));
        }

        if (getIntent().getBundleExtra("userdata")!=null) {
            Bundle bundle = getIntent().getBundleExtra("userdata");
            namasesi = bundle.getString("nama_mancing");
            hrga = bundle.getString("harga");
            jmlhsesi = bundle.getInt("1");
            idx = bundle.getInt("id");
        }

        jmlhs.setText("" + namasesi);

        jmlhps.setText(""+jmlhsesi);
        idpncing.setText(String.valueOf(idx));
        harga.setText( formatRupiah(Double.parseDouble(String.valueOf(String.valueOf(hrga)))));


        btntambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity==100){
                    Toast.makeText(Pembayaran.this,"pesanan maximal 100", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    int jumlah = Integer.parseInt(jmlhps.getText().toString());
                    quantity = jumlah+1;
                    jmlhps.setText(String.valueOf(quantity));
                     int hg = Integer.parseInt(hrga);
                     int total = quantity * hg;
                    harga.setText( formatRupiah(Double.parseDouble(String.valueOf(total))));
                }
            }
        });
        btnkuran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity == 1 ){

                    Toast.makeText(Pembayaran.this,"pesanan minimal 1",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    int jmlh = Integer.parseInt(jmlhps.getText().toString());
                    quantity = jmlh-1;
                   jmlhps.setText(String.valueOf(quantity));
                    int hgr = Integer.parseInt(hrga);
                    int totalr = quantity * hgr;

                    harga.setText( formatRupiah(Double.parseDouble(String.valueOf(totalr))));

                }
            }
        });


        prosesbayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();

                bundle.putString("kode", kode_trans.getText().toString());
                bundle.putString("jumlah", jmlhps.getText().toString());
                bundle.putString("bayar", pilih_bayar.getSelectedItem().toString());
                bundle.putString("harga",harga.getText().toString());


                idpan = idpncing.getText().toString();
                user = nama.getText().toString();
                nmapan = jmlhs.getText().toString();
                kd = kode_trans.getText().toString();
                by = harga.getText().toString();
                jm = jmlhps.getText().toString();
                mt = pilih_bayar.getSelectedItem().toString();
                rk = no_tf2.getText().toString();




                ApiRequest ardTambah = Retro_server.konekRtrofit().create(ApiRequest.class);
                Call<ResponseBoking> simpanData = ardTambah.ardBayar( idpan,user,nmapan,kd,by,jm,mt,rk);

                simpanData.enqueue(new Callback<ResponseBoking>() {
                    @Override
                    public void onResponse(Call<ResponseBoking> call, retrofit2.Response<ResponseBoking> response) {
                        String pesan = response.body().getPesan();

                        update_stok();
                        Toast.makeText(Pembayaran.this,"Pembayaran Berhasil", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<ResponseBoking> call, Throwable t) {
                        Toast.makeText(Pembayaran.this,"Gagal Terhubung Dengan Server", Toast.LENGTH_SHORT).show();
                    }
                });
                Intent intent1 = new Intent(Pembayaran.this, Konfirmasi.class);
                intent1.putExtra("userdata", bundle);
                startActivity(intent1);
            }
        });

    }





    private void update_stok() {

        ApiRequest getdata = Retro_server.konekRtrofit().create(ApiRequest.class);
        Call<ResponsePaket> get = getdata.ardTampil(idpan);

        get.enqueue(new Callback<ResponsePaket>() {
            @Override
            public void onResponse(Call<ResponsePaket> call, Response<ResponsePaket> response) {

                int kode = response.body().getKode();
                String pesan = response.body().getPesan();
                listmancing = response.body().getData();

                int id = listmancing.get(0).getId_pancing();

                int b;
                b = Integer.parseInt(listmancing.get(0).getStok());
                quantity = b-1;

                ApiRequest ardKK = Retro_server.konekRtrofit().create(ApiRequest.class);
                Call<ResponsePaket> update_kk = ardKK.ardUpdatePancing(id,quantity);

                update_kk.enqueue(new Callback<ResponsePaket>() {
                    @Override
                    public void onResponse(Call<ResponsePaket> call, retrofit2.Response<ResponsePaket> response) {
                        String pesan = response.body().getPesan();

                    }

                    @Override
                    public void onFailure(Call<ResponsePaket> call, Throwable t) {

                    }
                });
            }
            @Override
            public void onFailure(Call<ResponsePaket> call, Throwable t) {

            }
        });






    }


    private String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }
}