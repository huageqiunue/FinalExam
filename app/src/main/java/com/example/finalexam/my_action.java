package com.example.finalexam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class my_action extends AppCompatActivity {
    List<Actions> personList;
    MyOpenHelper mOpenHelper;
    SQLiteDatabase db;
    my_action.MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_action);
        ListView lv = (ListView) findViewById(R.id.my_action_p);
        personList = new ArrayList<Actions>();
        // 创建MyOpenHelper实例
        mOpenHelper = new MyOpenHelper(this);
        // 得到数据库
        db = mOpenHelper.getWritableDatabase();
        // 查询数据
        Query();
        // 创建MyAdapter实例
        myAdapter = new MyAdapter(my_action.this);

        // 向listview中添加Adapter
        lv.setAdapter(myAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(my_action.this, "Click item" + position, Toast.LENGTH_SHORT).show();
                Actions p = personList.get(position);
                new AlertDialog.Builder(my_action.this).setTitle("活動信息").setMessage(p.getAction_describe())
                        .setPositiveButton("關閉" ,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
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
            my_action.ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.listitem_my_action, null);
                viewHolder.txt_publish = (TextView) convertView
                        .findViewById(R.id.act_publish_p);
                viewHolder.txt_name = (TextView) convertView
                        .findViewById(R.id.act_name_p);
                viewHolder.txt_package = (TextView) convertView
                        .findViewById(R.id.act_package_p);
                viewHolder.txt_time= (TextView) convertView
                        .findViewById(R.id.act_time_p);
                viewHolder.mButton = (Button) convertView.findViewById(R.id.action_del);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (my_action.ViewHolder) convertView.getTag();
            }
            //向TextView中插入数据
            viewHolder.txt_publish.setText(p.getAction_business());
            viewHolder.txt_name.setText(p.getAction_name());
            viewHolder.txt_package.setText(p.getAction_package()+"");
            viewHolder.txt_time.setText(p.getTime());

            viewHolder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dele(position);
                    myAdapter.notifyDataSetChanged();
                }
            });
            //已經添加活動

            return convertView;
        }

    }

    private void dele( final int position) {
        int cash2 = 0;
        //查詢
        Cursor cursor;
        if (getIntent().getIntExtra("cash", 0) - personList.get(position).getAction_package() >= 0) {
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
            ContentValues values = new ContentValues();
            values.put("cash", getIntent().getIntExtra("cash", 0) - personList.get(position).getAction_package());
            db.update("usertable", values, "username=?", new String[]{getIntent().getStringExtra("username")});
            ContentValues values2 = new ContentValues();
            values2.put("cash", cash2 + personList.get(position).getAction_package());
            db.update("Businesstable", values2, "username=?", new String[]{personList.get(position).getAction_business()});
            db.delete("action_member", "username=? and active_name=?", new String[]{getIntent().getStringExtra("username"), personList.get(position).getAction_name()});
            join_actions.isclicked = false;
            personList.remove(position);
            Toast.makeText(my_action.this, "活動已經取消", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(my_action.this, "活動無法取消，你的現金不夠", Toast.LENGTH_SHORT).show();
        }
    }





    class ViewHolder {
        private  TextView txt_publish;
        private TextView txt_name;
        private TextView txt_package;
        private TextView txt_describe;
        private TextView txt_time;
        private Button mButton;
    }

    // 查询数据
    /*
     String action_message="create table action_message"+
                "(id integer primary key autoincrement,join_user text,publisher_name text,active_name text,active_award int,describe text,date text)";
        */
    public void Query() {
        String active_name_get="";
        String username_get;
        Cursor cursor = db.query("action_member", new String[]{"active_name","username"}, "username = ?", new String[]{getIntent().getStringExtra("username")}, null, null, null, null);
        if (cursor!=null&&cursor.getCount()>0){
            cursor.moveToFirst();
             active_name_get = cursor.getString(cursor.getColumnIndex("active_name"));
             username_get = cursor.getString(cursor.getColumnIndex("username"));
        }else if (cursor!=null){
            cursor.close();
        }
        Cursor cursor1 = db.query("active", new String[]{"id","publisher_name","active_name","active_award","describe","date"}, "active_name = ?", new String[]{active_name_get}, null, null, null, null);
        if (cursor1 != null && cursor1.getCount() > 0) {
            cursor1.moveToFirst();
            String publisher_name = cursor1.getString(cursor1.getColumnIndex("publisher_name"));
            String active_name = cursor1.getString(cursor1.getColumnIndex("active_name"));
            int active_award = cursor1.getInt(cursor1.getColumnIndex("active_award"));
            String describe = cursor1.getString(cursor1.getColumnIndex("describe"));
            String date = cursor1.getString(cursor1.getColumnIndex("date"));
            Actions actions = new Actions(publisher_name, active_name, active_award, describe, date);
            personList.add(actions);
            while (cursor1.moveToNext()) {
                 publisher_name = cursor1.getString(cursor1.getColumnIndex("publisher_name"));
                 active_name = cursor1.getString(cursor1.getColumnIndex("active_name"));
                 active_award = cursor1.getInt(cursor1.getColumnIndex("active_award"));
                 describe = cursor1.getString(cursor1.getColumnIndex("describe"));
                 date = cursor1.getString(cursor1.getColumnIndex("date"));
                 actions = new Actions(publisher_name, active_name, active_award, describe, date);
                personList.add(actions);
            }
        }
        cursor1.close();
    }
}