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

public class show_goods_p extends AppCompatActivity {
    List<Goods> personList;
    MyOpenHelper mOpenHelper;
    SQLiteDatabase db;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_goods_p);

        ListView lv = (ListView) findViewById(R.id.show_goods_p);
        personList = new ArrayList<Goods>();
        // 创建MyOpenHelper实例
        mOpenHelper = new MyOpenHelper(this);
        // 得到数据库
        db = mOpenHelper.getWritableDatabase();
        // 查询数据
        Query();
        // 创建MyAdapter实例
        myAdapter = new MyAdapter(show_goods_p.this);
        // 向listview中添加Adapter
        lv.setAdapter(myAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(show_goods_p.this, "Click item" + position, Toast.LENGTH_SHORT).show();
                Goods p = personList.get(position);
                new AlertDialog.Builder(show_goods_p.this).setTitle("商品信息").setMessage(p.getGoods_describe())
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
            // 从personList取出Goods
            Goods p = personList.get(position);
            show_goods_p.ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.listitem_mygoods, null);
                viewHolder.txt_publish = (TextView) convertView
                        .findViewById(R.id.mygoods_publish);
                viewHolder.txt_name = (TextView) convertView
                        .findViewById(R.id.mygoods_name);
                viewHolder.txt_price = (TextView) convertView
                        .findViewById(R.id.mygoods_price);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (show_goods_p.ViewHolder) convertView.getTag();
            }
            //向TextView中插入数据
            viewHolder.txt_publish.setText(p.getGoods_business());
            viewHolder.txt_name.setText(p.getGoods_name());
            viewHolder.txt_price.setText(p.getGoods_price()+"");

            return convertView;
        }

    }
    class ViewHolder {
        private  TextView txt_publish;
        private TextView txt_name;
        private TextView txt_price;
        private TextView txt_describe;
    }

    // 查询mygoods数据
    /*
     String my_goods="create table my_goods"+
                "(id integer primary key autoincrement,username text,publisher_name text,goodsname text,goodsprice int,describe text)";
     */
    public void Query() {
        Cursor cursor = db.query("my_goods", new String[]{"publisher_name","goodsname","goodsprice","describe"}, "username = ?", new String[]{getIntent().getStringExtra("username")}, null, null, null, null);
        while (cursor.moveToNext()) {
            String publisher_name = cursor.getString(cursor.getColumnIndex("publisher_name"));
            String goodsname = cursor.getString(cursor.getColumnIndex("goodsname"));
            int goodsprice = cursor.getInt(cursor.getColumnIndex("goodsprice"));
            String describe = cursor.getString(cursor.getColumnIndex("describe"));
            Goods person = new Goods(publisher_name, goodsname, goodsprice, describe);
            personList.add(person);
        }
    }
}