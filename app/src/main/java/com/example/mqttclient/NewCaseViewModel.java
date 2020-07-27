package com.example.mqttclient;

import android.database.Cursor;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewCaseViewModel extends ViewModel {

    // TODO: Implement the ViewModel
    //实例化DBOpenHelper对象，用来创建数据库及表，参数：本页面，表名，null，版本号
    String temp1,temp2;
    //定义一个String类型的List数组作为数据源
    private List<String> dataList,dataDetail;
    // TODO: Implement the ViewModel
    public List<String> numPatient(DBOpenHelper dbOpenHelper){
        Cursor cursor = dbOpenHelper.getReadableDatabase().query("tb_patient",new String[] { "number",
                "name" },null,null,null,null,null);
        //为dataList赋值，将下面这些数据添加到数据源中
        dataList = new ArrayList<String>();

        while(cursor.moveToNext()){
            //利用getColumnIndex：String 来获取列的下标，再根据下标获取cursor的值
            temp1 = cursor.getString(cursor.getColumnIndex("number"));
            //temp2 = cursor.getString(cursor.getColumnIndex("name"));
            dataList.add(temp1);
            //dataList.add(temp2);

            Log.d("database", "numPatient: ");
        }


        return dataList;
    }


    public  String Patient(DBOpenHelper dbOpenHelper,String id,String type){
        Cursor cursor = dbOpenHelper.getReadableDatabase().query("tb_patient",new String[] { "number",
                "name","sex","age","illness" },"number=?",new String[]{id},null,null,null);
        while(cursor.moveToNext()){
            //利用getColumnIndex：String 来获取列的下标，再根据下标获取cursor的值
            temp2 = cursor.getString(cursor.getColumnIndex(type));
            //dataList.add(temp2);
        }
        return temp2;
    }

}

