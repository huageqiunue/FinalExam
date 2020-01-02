package com.example.finalexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//goods
public class add_shop extends AppCompatActivity {
    private MyOpenHelper dbHelper;
    private EditText shop_name;
    private EditText shop_price;
    private EditText shop_describe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);
        dbHelper=new MyOpenHelper(this);
        shop_name=(EditText)findViewById(R.id.shop_name);
        shop_price=(EditText)findViewById(R.id.shop_price);
        shop_describe=(EditText)findViewById(R.id.shop_describe);
        findViewById(R.id.upload_shop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert_shop();
                Toast.makeText(add_shop.this, "添加商品成功", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void insert_shop() {
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("publisher_name", getIntent().getStringExtra("username"));
        values.put("goodsname", shop_name.getText().toString());
        values.put("goodsprice", shop_price.getText().toString());
        values.put("describe", shop_describe.getText().toString());
        db.insert("goodslist", null, values);
        db.close();
    }
}
