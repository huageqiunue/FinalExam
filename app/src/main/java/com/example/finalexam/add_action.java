package com.example.finalexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class add_action extends AppCompatActivity {
    private MyOpenHelper dbHelper;
    private EditText action_name;
    private EditText action_package;
    private EditText action_describe;
    private EditText action_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_action);
        //定義
        dbHelper=new MyOpenHelper(this);
        action_name=(EditText)findViewById(R.id.action_name);
        action_package=(EditText)findViewById(R.id.action_package);
        action_describe=(EditText)findViewById(R.id.action_describe);
        action_data=(EditText)findViewById(R.id.action_data);
        findViewById(R.id.upload_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert_action();
                Toast.makeText(add_action.this, "添加活動成功", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void insert_action() {
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("publisher_name", getIntent().getStringExtra("username"));
        values.put("active_name", action_name.getText().toString());
        values.put("active_award", action_package.getText().toString());
        values.put("describe", action_describe.getText().toString());
        values.put("date", action_data.getText().toString());
        db.insert("active", null, values);
        db.close();
        System.out.println("插入成功");
    }
}
