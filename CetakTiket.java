package com.asah.pemancingan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class CetakTiket extends AppCompatActivity {

    QRGEncoder qrgEncoder;
    Bitmap qrcode_b;
    private TextView kodecetak,namapenggguna;
    private ImageView qrtiket;
    private String kodetiket;
    SharedPreferences sharedPreferences;
    private static final int REQUEST_CODE = 10;
    Context context;
    private static final String filename = "login";
    private static final String Username = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cetak_tiket);

        kodecetak = findViewById(R.id.kodecetak);
        namapenggguna = findViewById(R.id.namapenguna);
        qrtiket = findViewById(R.id.qrtiket);


        sharedPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(Username)) {
            namapenggguna.setText("Nama Pengguna : " + sharedPreferences.getString(Username, ""));
        }

        if (getIntent().getBundleExtra("userdata")!=null) {
            Bundle bundle = getIntent().getBundleExtra("userdata");
            kodetiket = bundle.getString("kode");

        }
        kodecetak.setText(String.valueOf(kodetiket));

        String inputvalue = kodecetak.getText().toString();
        if (inputvalue.length() > 0) {
            WindowManager manager = (WindowManager) CetakTiket.this.getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;
            qrgEncoder = new QRGEncoder(inputvalue, null, QRGContents.Type.TEXT, smallerDimension);
            try {
                qrcode_b = qrgEncoder.encodeAsBitmap();
                qrtiket.setImageBitmap(qrcode_b);

            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(CetakTiket.this);
        builder.setMessage("Anda Ingin Keluar Dari Cetak Tiket?");
        builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(CetakTiket.this,MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        builder.setNegativeButton("Batal",null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
