package com.example.mqttclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

public class TrainFragment extends Fragment {

    private TrainViewModel mViewModel;
    private TextView textView;

    //首先还是先声明这个Spinner控件
    private Spinner spinner;

    //定义一个ArrayAdapter适配器作为spinner的数据适配器
    private ArrayAdapter<String> adapter;



    
    public static TrainFragment newInstance() {
        return new TrainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.train_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TrainViewModel.class);
        // TODO: Use the ViewModel
        spinner = (Spinner) getView().findViewById(R.id.spinner);
        textView = (TextView) getView().findViewById(R.id.textView);

        //为dataList赋值，将下面这些数据添加到数据源中
        ArrayList<String> dataList = new ArrayList<String>();
        dataList = (ArrayList<String>) mViewModel.numPatient();
        /*为spinner定义适配器，也就是将数据源存入adapter，这里需要三个参数
        1. 第一个是Context（当前上下文），这里就是this
        2. 第二个是spinner的布局样式，这里用android系统提供的一个样式
        3. 第三个就是spinner的数据源，这里就是dataList*/
        adapter = new ArrayAdapter<String>(this.getContext(),  android.R.layout.simple_spinner_item, dataList);

        //为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //为spinner绑定我们定义好的数据适配器
        spinner.setAdapter(adapter);

        //为spinner绑定监听器，这里我们使用匿名内部类的方式实现监听器
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textView.setText("您当前选择的是："+adapter.getItem(position)+"号病人");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textView.setText("请选择接受训练的病人");

            }
        });


    }



}
