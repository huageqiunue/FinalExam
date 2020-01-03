package com.example.finalexam;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyOpenHelper extends SQLiteOpenHelper {
    private static final String name = "/mnt/sdcard/temp/database.db"; //数据库路径及名称
    public MyOpenHelper(Context context) {
        //创建数据库
//        super(context,  context.getExternalFilesDir(null).getAbsolutePath() + "/" +"final.db", null, 1);
        super(context,  "fix.db", null, 1);
        // TODO Auto-generated constructor stub
        System.out.println("MyOpenHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        //创建表
        //db.execSQL("create table person(_id integer primary key autoincrement, name char(10), salary char(20), phone integer(20) )");
        //用戶表
        String usersInfo_table = "create table usertable" +
                "(id integer primary key autoincrement, username text," +
                "password text,email text,cash int default 0)";
        //商家表
        String BusinessInfo_table = "create table Businesstable" +
                "(id integer primary key autoincrement, username text," +
                "password text,email text,cash int default 10000)";
        //交易信息表
        String information_table="create table information"+
                "(id integer primary key autoincrement ,myself text,yourself text"+
                ",paycash int)";
        //活動列表
        String transaction="create table active"+
                "(id integer primary key autoincrement,publisher_name text,active_name text,active_award int,describe text,date text,place text)";
        //商品表
        String goods="create table goodslist"+
                "(id integer primary key autoincrement,publisher_name text,goodsname text,goodsprice int,describe text)";
        //我的商品
        String my_goods="create table my_goods"+
                "(id integer primary key autoincrement,username text,publisher_name text,goodsname text,goodsprice int,describe text,date text)";
        //我的賣出商品
        String goods_message="create table goods_message"+
                "(id integer primary key autoincrement,buyer text,you text,goodsname text,goodsprice int,date text)";
        //活動信息表
        String action_message="create table action_message"+
                "(id integer primary key autoincrement,publisher_name text,active_name text,active_award int,describe text,date text)";
        //活動成員
        String action_member="create table action_member"+
                "(id integer primary key autoincrement,username text,active_name text)";

        db.execSQL(usersInfo_table);
        db.execSQL(BusinessInfo_table);
        db.execSQL(information_table);
        db.execSQL(transaction);
        db.execSQL(goods);
        db.execSQL(my_goods);
        db.execSQL(goods_message);
        db.execSQL(action_message);
        db.execSQL(action_member);
        System.out.println(db.getPath());
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table if exists usertable");
//        db.execSQL("drop table if exists Businesstable");
//        db.execSQL("drop table if exists information");
//        db.execSQL("drop table if exists goodslist");
//        db.execSQL("drop table if exists my_goods");
//        db.execSQL("drop table if exists goods_message");
//        db.execSQL("drop table if exists action_message");
//        db.execSQL("drop table if exists action_member");
//        onCreate(db);
    }


//    private SQLiteDatabase openDatabase() {
////      String dbFd= "/data"+Environment.getDataDirectory().getAbsolutePath() + "/" + getPackageName()+"/databases/";
//        String dbFd= getFilesDir().getAbsolutePath();
//        String dbfile= dbFd+"/test_db";
//
//        try {
//            File fd=new File(dbFd);
//
//            if(!fd.exists()){
//                fd.mkdirs();
//            }
//
//            File file=new File(dbfile);
//            if (!file.exists()) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
//
////                InputStream is = getResources().openRawResource(R.raw.countries); //欲导入的数据库
//                InputStream is=getAssets().open("test_db");
//                FileOutputStream fos = new FileOutputStream(dbfile);
//                byte[] buffer = new byte[400000];
//                int count = 0;
//                while ((count = is.read(buffer)) > 0) {
//                    fos.write(buffer, 0, count);
//                }
//                fos.close();
//                is.close();
//            }
//
//            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
//
//            return db;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//
//    }

}