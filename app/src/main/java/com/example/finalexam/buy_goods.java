package com.example.finalexam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
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

public class buy_goods extends AppCompatActivity {
    List<Goods> personList;
    MyOpenHelper mOpenHelper;
    SQLiteDatabase db;
    MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_goods);
        ListView lv = (ListView) findViewById(R.id.Goods_list);
        personList = new ArrayList<Goods>();
        // 创建MyOpenHelper实例
        mOpenHelper = new MyOpenHelper(this);
        // 得到数据库
        db = mOpenHelper.getWritableDatabase();
        final String detail;
        // 查询数据
        Query();
        // 创建MyAdapter实例
        myAdapter = new MyAdapter(buy_goods.this);
        // 向listview中添加Adapter
        lv.setAdapter(myAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(buy_goods.this, "Click item" + position, Toast.LENGTH_SHORT).show();
                Goods p = personList.get(position);
                new AlertDialog.Builder(buy_goods.this).setTitle("商品信息").setMessage(p.getGoods_describe())
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
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.listitem_layout, null);
                viewHolder.txt_publish = (TextView) convertView
                        .findViewById(R.id.goods_publish);
                viewHolder.txt_name = (TextView) convertView
                        .findViewById(R.id.goods_name);
                viewHolder.txt_price = (TextView) convertView
                        .findViewById(R.id.goods_price);
                viewHolder.mButton = (Button) convertView.findViewById(R.id.goods_add_button);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //向TextView中插入数据
            viewHolder.txt_publish.setText(p.getGoods_business());
            viewHolder.txt_name.setText(p.getGoods_name());
            viewHolder.txt_price.setText(p.getGoods_price()+"");
            viewHolder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //my_goods 數據存儲
                    buy(position);

                    myAdapter.notifyDataSetChanged();
                }
            });

            return convertView;
        }

    }
//買商品--》database：my_goods

    private void buy( final int position) {
        if (getIntent().getIntExtra("cash",0)-personList.get(position).getGoods_price()>=0) {
            int cash2 = 0;
            //查詢
            Cursor cursor;
            try {
                cursor = db.query("Businesstable", new String[]{"id", "username", "password", "email", "cash"}, "username = ?", new String[]{personList.get(position).getGoods_business()}, null, null, null, null);
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
            values.put("cash", getIntent().getIntExtra("cash", 0) - personList.get(position).getGoods_price());
            db.update("usertable", values, "username=?", new String[]{getIntent().getStringExtra("username")});
            ContentValues values2 = new ContentValues();
            values2.put("cash", cash2 + personList.get(position).getGoods_price());
            db.update("Businesstable", values2, "username=?", new String[]{personList.get(position).getGoods_business()});

            //插入進business消息記錄
            ContentValues contentValues5 = new ContentValues();
            contentValues5.put("buyer", getIntent().getStringExtra("username"));
            contentValues5.put("you", personList.get(position).getGoods_business());
            contentValues5.put("goodsname", personList.get(position).getGoods_name());
            contentValues5.put("goodsprice", personList.get(position).getGoods_price());
            db.insert("goods_message", null, contentValues5);

            //在goods表中刪除商品
            db.delete("goodslist", "goodsname=?", new String[]{personList.get(position).getGoods_name()});

            //插入交易數據
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", getIntent().getStringExtra("username"));
            contentValues.put("publisher_name", personList.get(position).getGoods_business());
            contentValues.put("goodsname", personList.get(position).getGoods_name());
            contentValues.put("goodsprice", personList.get(position).getGoods_price());
            contentValues.put("describe", personList.get(position).getGoods_describe());
            db.insert("my_goods", null, contentValues);
            personList.remove(position);
            Toast.makeText(buy_goods.this, "已經購買,交易成功", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(buy_goods.this, "不夠錢", Toast.LENGTH_SHORT).show();
        }
    }





    class ViewHolder {
        private  TextView txt_publish;
        private TextView txt_name;
        private TextView txt_price;
        private TextView txt_describe;
        private Button mButton;
    }

    // 查询数据
    public void Query() {
        Cursor cursor = db.query("goodslist", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String publisher_name = cursor.getString(1);
            String goodsname = cursor.getString(2);
            int goodsprice = cursor.getInt(3);
            String describe = cursor.getString(4);
            Goods person = new Goods(publisher_name, goodsname, goodsprice, describe);
            personList.add(person);
        }
    }

}