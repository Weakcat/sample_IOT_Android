package com.example.mqttclient;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class TrainViewModel extends ViewModel {
    //定义一个String类型的List数组作为数据源
    private List<String> dataList;
    // TODO: Implement the ViewModel
    public List<String> numPatient(){
        //为dataList赋值，将下面这些数据添加到数据源中
        dataList = new ArrayList<String>();
        dataList.add("0001");
        dataList.add("0002");
        dataList.add("0003");
        dataList.add("0004");
        dataList.add("0005");
        return dataList;
    }
}
