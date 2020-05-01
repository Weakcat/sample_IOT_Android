package com.example.mqttclient;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper2 extends SQLiteOpenHelper {
    //定义创建数据表的sql语句
    final String Create_Table_SQL="create table tb_train (_id integer PRIMARY KEY AUTOINCREMENT,number text,model text,strength integer,time integer,speed integer,machine text)";

    //把第三个工厂设为null
    public DBOpenHelper2(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("数据库2相关", "开始 ");
        sqLiteDatabase.execSQL(Create_Table_SQL);//创建单词的数据表
        Log.d("数据2库相关", "成功 ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL("drop table if exists tb_train");
        onCreate(db);
    }
}
