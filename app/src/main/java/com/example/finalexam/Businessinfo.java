package com.example.finalexam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class Businessinfo extends AppCompatActivity {
    private MyOpenHelper dbHelper;
    private TextView name;
    private TextView type;
    private TextView cash;
    private Button show_scan;
    private Button show_message1;
    private Button add_goods;
    private Button add_action;
    private Button my_action_B;
    private Button show_goods_business;
    int cash1;
    int cash2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businessinfo);

        dbHelper = new MyOpenHelper(this);
        name = (TextView) findViewById(R.id.text_name);
        type = (TextView) findViewById(R.id.text_type);
        cash = (TextView) findViewById(R.id.text_cash);
        //個人信息
        final String username = getIntent().getStringExtra("username");
        String typee = getIntent().getStringExtra("type");
        String cashh = getIntent().getStringExtra("cash");
        name.setText(username);
        type.setText(typee);
        cash.setText(cashh);
        dbHelper = new MyOpenHelper(this);
        updateCash(username);
        cash.setText(cash1 + "");
        //scan
        show_scan = (Button) findViewById(R.id.show_Qrcode);
        show_message1 = (Button) findViewById(R.id.show_message_B);
        add_goods = (Button) findViewById(R.id.add_goods);
        add_action = (Button) findViewById(R.id.add_action);
        my_action_B = (Button) findViewById(R.id.my_action_B);
        show_goods_business = (Button) findViewById(R.id.show_goods_business);
        init(username);
    }

    private void init(final String username) {

        show_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Businessinfo.this, show_scan.class);
                intent.putExtra("username", getIntent().getStringExtra("username"));
                startActivity(intent);
            }
        });

        //顯示交易信息
        show_message1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Businessinfo.this, show_message.class);
                intent.putExtra("username", username);
                intent.putExtra("isbusiness", true);
                startActivity(intent);
            }
        });

        //添加商品
        add_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Businessinfo.this, add_shop.class);
                intent.putExtra("username", username);
                intent.putExtra("isbusiness", true);
                startActivity(intent);
            }
        });
        //添加活動
        add_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Businessinfo.this, add_action.class);
                intent.putExtra("username", username);
                intent.putExtra("isbusiness", true);
                startActivity(intent);
            }
        });
        //查詢我的活動
        my_action_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Businessinfo.this, my_action_b.class);
                intent.putExtra("username", username);
                intent.putExtra("isbusiness", true);
                startActivity(intent);
            }
        });
        //查看我的商品情況
        show_goods_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Businessinfo.this, show_goods_business.class);
                intent.putExtra("username", username);
                intent.putExtra("isbusiness", true);
                startActivity(intent);
            }
        });

    }

    //設置info的金額顯示
    private Integer updateCash(String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor;
        try {
            /*
             String BusinessInfo_table = "create table Businesstable" +
                "(id integer primary key autoincrement, username text," +
                "password text,email text,cash int default 500)";
             */
            cursor = db.query("Businesstable", new String[]{"id", "username", "password", "email", "cash"}, "username = ?", new String[]{username}, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                cash1 = cursor.getInt(cursor.getColumnIndex("cash"));
                Log.e("ok", "現金為" + cash1);
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e("不行", "queryDatas" + e.toString());
        }
        //关闭数据库
        db.close();
        return cash1;
    }


    //處理QRcode
    private Button.OnClickListener onscan = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Businessinfo.this, show_scan.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99) {
            finish();
            Intent intent = new Intent(Businessinfo.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 按下BACK，同时没有重复
            finish();
            Intent intent = new Intent(Businessinfo.this, LoginActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
