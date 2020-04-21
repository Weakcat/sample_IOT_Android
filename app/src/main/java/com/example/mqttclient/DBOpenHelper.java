package com.example.mqttclient;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
    //定义创建数据表的sql语句
    final String Create_Table_SQL="create table tb_patient (_id integer PRIMARY KEY AUTOINCREMENT,number text,name text,sex text,age text,illness text)";

    //把第三个工厂设为null
    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("数据库相关", "开始 ");
        sqLiteDatabase.execSQL(Create_Table_SQL);//创建单词的数据表
        Log.d("数据库相关", "成功 ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL("drop table if exists tb_patient");
        onCreate(db);
    }
}
