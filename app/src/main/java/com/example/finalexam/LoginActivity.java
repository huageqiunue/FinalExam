package com.example.finalexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener  {
    private EditText edit_account, edit_password;
    private Button btn_login, btn_register;
    private ImageButton openpwd;
    private boolean flag = false;
    private String account, password;
    private MyOpenHelper dbHelper;
    private CheckBox isBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

    }

    private void init() {
        edit_account = (EditText) findViewById(R.id.edit_account);
        edit_password = (EditText) findViewById(R.id.edit_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        openpwd = (ImageButton) findViewById(R.id.btn_openpwd);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        openpwd.setOnClickListener(this);
        dbHelper = new MyOpenHelper(this);
        isBusiness=(CheckBox)findViewById(R.id.Business);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (edit_account.getText().toString().trim().equals("") | edit_password.getText().
                        toString().trim().equals("")) {
                    Toast.makeText(this, "请输入账号或者注册账号！", Toast.LENGTH_SHORT).show();
                } else {
                    readUserInfo();
                }
                break;
            case R.id.btn_register:
                Intent intent = new Intent(LoginActivity.this, register.class);
                startActivity(intent);
                break;
            case R.id.btn_openpwd:
                if (flag == true) {//不可见
                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = false;
                    openpwd.setBackgroundResource(R.drawable.invisible);
                } else {
                    edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = true;
                    openpwd.setBackgroundResource(R.drawable.visible);
                }
                break;
        }
    }

    /**
     * 读取SharedPreferences存储的键值对
     * */
    public void readUsersInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences("UsersInfo",MODE_PRIVATE);
        account = sharedPreferences.getString("username","");
        password = sharedPreferences.getString("password","");
    }
    /**
     * 读取UserData.db中的用户信息
     * */
    protected void readUserInfo() {
        if (login(edit_account.getText().toString(), edit_password.getText().toString(),isBusiness.isChecked())) {
            Toast.makeText(this, "登陆成功！", Toast.LENGTH_SHORT).show();
            if(isBusiness.isChecked()){
                Intent intent = new Intent(LoginActivity.this, Businessinfo.class);
                intent.putExtra("username", edit_account.getText().toString());
                intent.putExtra("type","商家");
                intent.putExtra("cash",500);
                startActivity(intent);
            }else {
                Intent intent = new Intent(LoginActivity.this, Userinfo.class);
                intent.putExtra("username", edit_account.getText().toString());
                intent.putExtra("type","用戶");
                intent.putExtra("cash",0);
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, "账户或密码错误，请重新输入！！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 验证登录信息
     * */
    public boolean login(String username, String password,Boolean isBusiness) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(isBusiness){
            String sql = "Select * from Businesstable where username=? and password=?";
            Cursor cursor = db.rawQuery(sql, new String[]{username, password});
            if (cursor.moveToFirst()) {
                cursor.close();
                return true;
            }
            return false;
        }else {
            String sql = "Select * from usertable where username=? and password=?";
            Cursor cursor = db.rawQuery(sql, new String[]{username, password});
            if (cursor.moveToFirst()) {
                cursor.close();
                return true;
            }
            return false;
        }
    }
}
