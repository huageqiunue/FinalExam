package com.example.finalexam;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class show_goods_business extends AppCompatActivity {
    MyOpenHelper mOpenHelper;
     TextView show_goodsinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_goods_business);
        String username=getIntent().getStringExtra("username");
        mOpenHelper=new MyOpenHelper(this);
        String message=serach(username);
        show_goodsinfo=(TextView)findViewById(R.id.show_goods_busin);
        show_goodsinfo.setText(message);
    }
    public String serach(String name){
        String buyer;
        String goodsname;
        int goodsprice;
        StringBuffer sb=new StringBuffer();
        SQLiteDatabase db=mOpenHelper.getWritableDatabase();
        Cursor cursor;
        sb.append("信  息  記  錄\n");
            try {
                cursor = db.query("goods_message", new String[]{"buyer","you","goodsname","goodsprice"}, "you = ?", new String[]{name}, null, null, null, null);
            /*
            取出信息（可能有些錯誤）
             */
                if (cursor != null&& cursor.getCount() > 0) {
                    while (cursor.moveToNext()){
                        buyer = cursor.getString(cursor.getColumnIndex("buyer"));
                        goodsname=cursor.getString(cursor.getColumnIndex("goodsname"));
                        goodsprice = cursor.getInt(cursor.getColumnIndex("goodsprice"));
                        sb.append(buyer+" 買了你的 "+goodsname+" ，價格爲 "+goodsprice+"\n");
                    }
                }else if (cursor!=null){
                    cursor.close();
                }
            } catch (SQLException e) {
                Log.e("不行", "queryDatas" + e.toString());
            }
        //关闭数据库
        db.close();
        return sb.toString();
    }
}

