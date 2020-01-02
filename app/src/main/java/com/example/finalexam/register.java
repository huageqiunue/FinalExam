package com.example.finalexam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class register extends AppCompatActivity implements View.OnClickListener{
    private EditText edit_register, edit_setpassword, edit_email;
    private Button btn_yes, btn_cancel;
    private MyOpenHelper dbHelper;
    private CheckBox isBusiness;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        dbHelper = new MyOpenHelper(this);
    }


    protected void init() {
        edit_register = (EditText) findViewById(R.id.edit_register);
        edit_setpassword = (EditText) findViewById(R.id.edit_setpassword);
        edit_email = (EditText) findViewById(R.id.edit_email);
        btn_yes = (Button) findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(this);
        btn_cancel = (Button) findViewById(R.id.btn_cancle);
        btn_cancel.setOnClickListener(this);
        isBusiness=(CheckBox)findViewById(R.id.isBusiness);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                if (CheckIsDataAlreadyInDBorNot(edit_register.getText().toString(),isBusiness.isChecked())) {
                    Toast.makeText(this, "该用户名已被注册，注册失败", Toast.LENGTH_SHORT).show();
                } else {
                    registerUserInfo(edit_register.getText().toString(),
                            edit_setpassword.getText().toString(),edit_email.getText().toString(),isBusiness.isChecked());
                    Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();
                    Intent register_intent = new Intent(register.this,
                            LoginActivity.class);
                    startActivity(register_intent);
                }
                break;
            case R.id.btn_cancle:
                Intent login_intent = new Intent(register.this, LoginActivity.class);
                startActivity(login_intent);
                break;
            default:
                break;
        }
    }


    /**
     * 利用SharedPreferences进行默认登陆设置
     */
    private void saveUsersInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("UsersInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", edit_register.getText().toString());
        //判断注册时的两次密码是否相同
        if (edit_setpassword.getText().toString().equals(edit_email.getText().toString())) {
            editor.putString("password", edit_setpassword.getText().toString());
        }
        editor.commit();
    }

    /**
     * 利用sql创建嵌入式数据库进行注册访问
     */
    private void registerUserInfo(String username, String userpassword,String email,Boolean ischeck) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", userpassword);
        values.put("email", email);
        if (ischeck){
            db.insert("Businesstable", null, values);
        }else {
            db.insert("usertable", null, values);
        }
        db.close();
    }

    /**
     * 检验用户名是否已经注册
     */
    public boolean CheckIsDataAlreadyInDBorNot(String value,Boolean ischeck) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //如果是商家
        if(ischeck){
            String Query = "Select * from Businesstable where username =?";
            Cursor cursor = db.rawQuery(Query, new String[]{value});
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
            return false;
        }else {
            //如果是顧客
            String Query = "Select * from usertable where username =?";
            Cursor cursor = db.rawQuery(Query, new String[]{value});
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
            return false;
        }
    }
}
