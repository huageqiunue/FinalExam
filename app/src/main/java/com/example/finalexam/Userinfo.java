package com.example.finalexam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Userinfo extends AppCompatActivity {
    private MyOpenHelper dbHelper;
    private TextView name;
    private TextView type;
    private TextView cash;
    private Button scan;
    private Button buy;
    private Button join;
    private Button my_action;
    private Button message;
    int cash1 = 0;
    int cash2=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        dbHelper=new MyOpenHelper(this);
        name=(TextView)findViewById(R.id.text_name);
        type=(TextView)findViewById(R.id.text_type);
        cash=(TextView)findViewById(R.id.text_cash);
        final String username = getIntent().getStringExtra("username");
        System.out.println(username);
        String typee = getIntent().getStringExtra("type");
        String cashh = getIntent().getStringExtra("cash");
        name.setText(username);
        type.setText(typee);
        cash.setText(cashh);
        updateCash(username);
        scan=(Button)findViewById(R.id.scan_Qrcode);
        buy=(Button)findViewById(R.id.buy_goods);
        join=(Button)findViewById(R.id.join_action);
        my_action=(Button)findViewById(R.id.my_action);
        message=(Button)findViewById(R.id.show_message);

        cash.setText(cash1+"");
        //打開相機
        findViewById(R.id.scan_Qrcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建IntentIntegrator对象
                IntentIntegrator intentIntegrator = new IntentIntegrator(Userinfo.this);
                //提示
                intentIntegrator.setPrompt("這裏是掃描二維碼頁面");
                // 开始扫描
                intentIntegrator.initiateScan();
            }
        });

        //買商品
        findViewById(R.id.buy_goods).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Userinfo.this,buy_goods.class);
                intent.putExtra("username",username);
                intent.putExtra("isbusiness",false);
                intent.putExtra("cash",cash1);
                startActivityForResult(intent,1);
            }
        });

        //參加活動
        findViewById(R.id.join_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Userinfo.this,join_actions.class);
                intent.putExtra("username",username);
                intent.putExtra("isbusiness",false);
                intent.putExtra("cash",cash1);
                startActivityForResult(intent,1);
            }
        });

        //我的活動
        findViewById(R.id.my_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Userinfo.this,my_action.class);
                intent.putExtra("username",username);
                intent.putExtra("isbusiness",false);
                intent.putExtra("cash",cash1);
                startActivityForResult(intent,1);
            }
        });
        //顯示交易信息
        findViewById(R.id.show_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        Intent intent=new Intent(Userinfo.this,show_message.class);
        intent.putExtra("username",username);
        intent.putExtra("isbusiness",false);
        startActivityForResult(intent,1);
            }
        });

        //我的商品
        findViewById(R.id.show_goods_person).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Userinfo.this,show_goods_p.class);
                intent.putExtra("username",username);
                intent.putExtra("isbusiness",false);
                startActivity(intent);
            }
        });

    }

    //顯示現金
    private void updateCash(String username) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor;
        try {
            /*
               String usersInfo_table = "create table usertable" +
                "(id integer primary key autoincrement, username text," +
                "password text,email text,cash int default 0)";
             */
            cursor = db.query("usertable", new String[]{"id","username","password","email","cash"}, "username = ?", new String[]{username}, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                cash1 = cursor.getInt(cursor.getColumnIndex("cash"));
                Log.e("ok", "現金為"+cash1);
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e("不行", "queryDatas" + e.toString());
        }
        //关闭数据库
        db.close();
    }

    //掃描后返回觸發的事件
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode!=1&requestCode!=99){
            // 获取解析结果
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            final String other = result.getContents();
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.out.println("yes");
                    alertDialog.setMessage("你確定要進行交易嗎");
                    alertDialog.setTitle("提示");
                    alertDialog.show();
                    exchange(other);
                }
            });
            alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
//            alertDialog.setMessage("你確定要進行交易嗎");
//            alertDialog.setTitle("提示");
//            alertDialog.show();
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, "取消扫描", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "扫描内容:" + result.getContents(), Toast.LENGTH_LONG).show();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }else if (requestCode==1){
            obtainNotice();
        }else if (requestCode==99){
            finish();
            Intent intent=new Intent(Userinfo.this,LoginActivity.class);
            startActivity(intent);
        }
    }

    private void obtainNotice() {
        Intent intent=new Intent(Userinfo.this,Userinfo.class);
        intent.putExtra("username",getIntent().getStringExtra("username"));
        intent.putExtra("isbusiness",false);
        intent.putExtra("type",getIntent().getStringExtra("type"));
        startActivity(intent);
    }

    private void exchange(String other) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor;
        try {
            cursor = db.query("Businesstable", new String[]{"id","username","password","email","cash"}, "username = ?", new String[]{other}, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                 cash2 = cursor.getInt(cursor.getColumnIndex("cash"));
                Log.e("ok", "對方現金為"+cash2);
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e("不行", "queryDatas" + e.toString());
        }
        ContentValues values=new ContentValues();
        values.put("cash",cash1+100);
        db.update("usertable",values,"username=?",new String[]{getIntent().getStringExtra("username")});
        ContentValues values2=new ContentValues();
        values2.put("cash",cash2-100);
        db.update("Businesstable",values2,"username=?",new String[]{other});
        ContentValues values3 = new ContentValues();
        values3.put("myself", getIntent().getStringExtra("username"));
        values3.put("yourself", other);
        values3.put("paycash", 100);
        db.insert("information", null, values3);
        db.close();
    }
}
