package com.example.finalexam;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class show_message extends AppCompatActivity {
    private MyOpenHelper dbHelper;
    private TextView show_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_message);
        String username=getIntent().getStringExtra("username");
        Boolean isbusiness=getIntent().getBooleanExtra("isbusiness",false);
        dbHelper=new MyOpenHelper(this);
        String message=serach(username,isbusiness);
        show_text=(TextView)findViewById(R.id.show);
        show_text.setText(message);
        }
    public String serach(String name,Boolean isb){
        String me;
        String you;
        int money;
        StringBuffer sb=new StringBuffer();
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor;
        sb.append("信  息  記  錄\n");
        if (isb){
            try {
                cursor = db.query("information", new String[]{"myself","yourself","paycash"}, "yourself = ?", new String[]{name}, null, null, null, null);
            /*
            取出信息（可能有些錯誤）
             */
                if (cursor != null&& cursor.getCount() > 0) {
                    while (cursor.moveToNext()){
                        me = cursor.getString(cursor.getColumnIndex("myself"));
                        you=cursor.getString(cursor.getColumnIndex("yourself"));
                        money = cursor.getInt(cursor.getColumnIndex("paycash"));
                        sb.append("(" +you+ ")  你給了用戶 "+me+"  "+money+" 的紅包\n");
                    }
                }else if (cursor!=null){
                    cursor.close();
                }
            } catch (SQLException e) {
                Log.e("不行", "queryDatas" + e.toString());
            }
        }else {
            try {
                cursor = db.query("information", new String[]{"myself","yourself","paycash"}, "myself = ?", new String[]{name}, null, null, null, null);
            /*
            取出信息（可能有些錯誤）
             */
                if (cursor != null&& cursor.getCount() > 0) {
                    while (cursor.moveToNext()){
                        me = cursor.getString(cursor.getColumnIndex("myself"));
                        you=cursor.getString(cursor.getColumnIndex("yourself"));
                        money = cursor.getInt(cursor.getColumnIndex("paycash"));
                        sb.append("(" +me+ ")  你收了商家 "+you+" 的 "+money+" 紅包\n");
                    }
                }else if (cursor!=null){
                    cursor.close();
                }
            } catch (SQLException e) {
                Log.e("不行", "queryDatas" + e.toString());
            }
        }
        //关闭数据库
        db.close();
        return sb.toString();
    }
    }

