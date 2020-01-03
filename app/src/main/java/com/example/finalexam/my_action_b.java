package com.example.finalexam;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

public class my_action_b extends AppCompatActivity {
    private MyOpenHelper dbHelper;
    private TextView show_member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_action_b);
        dbHelper=new MyOpenHelper(this);
        String username=getIntent().getStringExtra("username");
        Boolean isbusiness=getIntent().getBooleanExtra("isbusiness",false);
        dbHelper=new MyOpenHelper(this);
        System.out.println(username);
        String message=serach(username);
        show_member=(TextView)findViewById(R.id.member);
        show_member.setMovementMethod(ScrollingMovementMethod.getInstance());
        show_member.setText(message);
    }
    public String serach(String name){
        String active_name;
        String describe;
        int date;
        String place;
        String user;
        StringBuffer sb=new StringBuffer();
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor;
        Cursor cursor1;
        /*
         String transaction="create table active"+
                "(id integer primary key autoincrement,publisher_name text,active_name text,active_award int,describe text,date text)";
         */
            cursor = db.query("active", new String[]{"id","publisher_name","active_name","active_award","describe", "date","place"}, "publisher_name=?", new String[]{name}, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                System.out.println("有了");
                    active_name = cursor.getString(cursor.getColumnIndex("active_name"));
                    describe = cursor.getString(cursor.getColumnIndex("describe"));
                    date = cursor.getInt(cursor.getColumnIndex("date"));
                    place=cursor.getString(cursor.getColumnIndex("place"));
                    sb.append("活動名稱：" + active_name + "\n");
                    sb.append("活動時間：" + date + "\n");
                    sb.append("活動詳情：" + describe + "\n");
                    sb.append("活動地點：" + place + "\n\n");
                    sb.append("以下是成員信息：\n");
                    System.out.println("ok now");
                        cursor1 = db.query("action_member", new String[]{"id","username","active_name"}, "active_name=?", new String[]{active_name}, null, null, null);
                        if (cursor1 != null && cursor1.getCount() > 0) {
                            while (cursor1.moveToNext()) {
                                user = cursor1.getString(cursor1.getColumnIndex("username"));
                                sb.append("用戶-->" + user + "\n");
                            }
                            sb.append("\n\n");
                        }
                        while (cursor.moveToNext()){
                            active_name = cursor.getString(cursor.getColumnIndex("active_name"));
                            describe = cursor.getString(cursor.getColumnIndex("describe"));
                            date = cursor.getInt(cursor.getColumnIndex("date"));
                            place = cursor.getString(cursor.getColumnIndex("place"));
                            sb.append("活動名稱：" + active_name + "\n");
                            sb.append("活動時間：" + date + "\n");
                            sb.append("活動詳情：" + describe + "\n\n");
                            sb.append("活動地點為：" + place + "\n\n");
                            sb.append("以下是成員信息：\n");
                            System.out.println("ok now");
                            cursor1 = db.query("action_member", new String[]{"id","username","active_name"}, "active_name=?", new String[]{active_name}, null, null, null);
                            if (cursor1 != null && cursor1.getCount() > 0) {
                                while (cursor1.moveToNext()) {
                                    user = cursor1.getString(cursor1.getColumnIndex("username"));
                                    sb.append("用戶-->" + user + "\n");
                                }
                            }
                        }
                if (cursor1 != null){
                    cursor1.close();
                }
            }
            if (cursor != null){
                cursor.close();
            }

//        关闭数据库
        db.close();
        return sb.toString();
    }
}

