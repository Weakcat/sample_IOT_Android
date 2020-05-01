package com.example.mqttclient;

import android.database.Cursor;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class StatisticViewModel extends ViewModel {
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

    public List<Integer> myTrainData(DBOpenHelper2 dbOpenHelper, String id, String type){
        Cursor cursor = dbOpenHelper.getReadableDatabase().query("tb_train",new String[] { "number",
                "model","strength","time","speed","machine" },"number=?",new String[]{id},null,null,null);
        //为dataList赋值，将下面这些数据添加到数据源中
        ArrayList<Integer> temp;
        int myTemp1,myTemp2;
        temp = new ArrayList<Integer>();

        while(cursor.moveToNext()){
            //利用getColumnIndex：String 来获取列的下标，再根据下标获取cursor的值
            myTemp1 = cursor.getInt(cursor.getColumnIndex(type));
            //temp2 = cursor.getString(cursor.getColumnIndex("name"));
            temp.add(myTemp1);
            //dataList.add(temp2);

        }


        return temp;
    }
}
