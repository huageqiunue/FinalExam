package com.example.finalexam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
        String transaction="create table active"+
                "(id integer primary key autoincrement,publisher_name text,active_name text,active_award int,describe text,date text)";
 */
public class join_actions extends AppCompatActivity {
    List<Actions> personList;
    MyOpenHelper mOpenHelper;
    SQLiteDatabase db;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_actions);

        final ListView lv = (ListView) findViewById(R.id.join_act);
        personList = new ArrayList<Actions>();
        // 创建MyOpenHelper实例
        mOpenHelper = new MyOpenHelper(this);
        // 得到数据库
        db = mOpenHelper.getWritableDatabase();
        // 查询数据
        Query();
        // 创建MyAdapter实例
        myAdapter = new MyAdapter(join_actions.this);
        // 向listview中添加Adapter
        lv.setAdapter(myAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(join_actions.this, "Click item" + position, Toast.LENGTH_SHORT).show();
                Actions p = personList.get(position);
                new AlertDialog.Builder(join_actions.this).setTitle("活動信息").setMessage(p.getAction_describe()+"\n活動地點："+p.getPlace())
                        .setPositiveButton("關閉", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });
    }

    // 创建MyAdapter继承BaseAdapter
    class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        public MyAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return personList.size();
        }

        @Override
        public Object getItem(int position) {
            return personList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // 从personList取出Actions

            Actions p = personList.get(position);
            join_actions.ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.listitem_join_action, null);
                viewHolder.txt_publish = (TextView) convertView
                        .findViewById(R.id.act_publish);
                viewHolder.txt_name = (TextView) convertView
                        .findViewById(R.id.act_name);
                viewHolder.txt_package = (TextView) convertView
                        .findViewById(R.id.act_package);
                viewHolder.txt_time = (TextView) convertView
                        .findViewById(R.id.act_time);
                viewHolder.mButton = (Button) convertView.findViewById(R.id.goods_add_button);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (join_actions.ViewHolder) convertView.getTag();
            }
            //向TextView中插入数据
            viewHolder.txt_publish.setText(p.getAction_business());
            viewHolder.txt_name.setText(p.getAction_name());
            viewHolder.txt_package.setText(p.getAction_package() + "");
            viewHolder.txt_time.setText(p.getTime());
                viewHolder.mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //my_goods 數據存儲
                        join(position);
                        myAdapter.notifyDataSetChanged();
                    }
                });

            //已經添加活動

            return convertView;
        }

    }
//買商品--》database：my_goods

    private void join(final int position) {
        int cash2 = 0;
        //查詢
        Cursor cursor;
        try {
            cursor = db.query("Businesstable", new String[]{"id", "username", "password", "email", "cash"}, "username = ?", new String[]{personList.get(position).getAction_business()}, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                cash2 = cursor.getInt(cursor.getColumnIndex("cash"));
                Log.e("ok", "對方現金為" + cash2);
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e("出錯誤", "queryDatas" + e.toString());
        }
        if (cash2 - personList.get(position).getAction_package() >= 0) {
            ContentValues values = new ContentValues();
            values.put("cash", getIntent().getIntExtra("cash", 0) + personList.get(position).getAction_package());
            db.update("usertable", values, "username=?", new String[]{getIntent().getStringExtra("username")});
            ContentValues values2 = new ContentValues();
            values2.put("cash", cash2 - personList.get(position).getAction_package());
            db.update("Businesstable", values2, "username=?", new String[]{personList.get(position).getAction_business()});

            //插入進business消息記錄
        /*
         String action_message="create table action_message"+
                "(id integer primary key autoincrement,publisher_name text,active_name text,active_award int,describe text,date text)";
         */
//        ContentValues contentValues5=new ContentValues();
//        contentValues5.put("publisher_name", personList.get(position).getAction_business());
//        contentValues5.put("active_name", personList.get(position).getAction_name());
//        contentValues5.put("active_award", personList.get(position).getAction_package());
//        contentValues5.put("describe", personList.get(position).getAction_describe());
//        contentValues5.put("date", personList.get(position).getTime());
//        db.insert("action_message",null,contentValues5);

            //插入活動成員表
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", getIntent().getStringExtra("username"));
            contentValues.put("active_name", personList.get(position).getAction_name());
            db.insert("action_member", null, contentValues);
            Toast.makeText(join_actions.this, "已經加入活動，請準時參加", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(join_actions.this, "對方的錢不足，活動無法參加", Toast.LENGTH_SHORT).show();
        }
    }


    class ViewHolder {
        private TextView txt_publish;
        private TextView txt_name;
        private TextView txt_package;
        private TextView txt_describe;
        private TextView txt_time;
        private Button mButton;
    }

    // 查询数据
    /*
     String transaction="create table active"+
                "(id integer primary key autoincrement,publisher_name text,active_name text,active_award int,describe text,date text)";
     */
    public void Query() {
        Cursor cursor = db.query("active", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String publisher_name = cursor.getString(1);
            String active_name = cursor.getString(2);
            int active_award = cursor.getInt(3);
            String describe = cursor.getString(4);
            String date = cursor.getString(5);
            String place = cursor.getString(6);
            Actions person = new Actions(publisher_name, active_name, active_award, describe, date,place);
            personList.add(person);
        }
    }
}
