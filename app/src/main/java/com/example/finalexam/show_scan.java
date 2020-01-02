package com.example.finalexam;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class show_scan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_scan);
        String name=getIntent().getStringExtra("username");
        genCode(name);
    }

    public void genCode(String name) {
        //把名字傳過去
        ImageView image_view = (ImageView) findViewById(R.id.image_view);
        BarcodeEncoder encoder = new BarcodeEncoder();
        try {
            Bitmap bit = encoder.encodeBitmap(name, BarcodeFormat.QR_CODE,
                    250, 250);
            image_view.setImageBitmap(bit);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    public void leave(View view) {
        finish();
    }
}
