package com.example.mqttclient;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class TrainFragment extends Fragment  implements MQTTService.IGetMessageCallBack {

    private TrainViewModel mViewModel;
    private TextView textView,textViewName,textViewSex,textViewAge,textViewIllness;
    private Button onOffTrain,voiceButton;
    private TextView countTime,trainSpeed;
    private TimeCount timeCount;
    private MQTTService mqttService;
    private RadioGroup trainModel;
    private View trainPage1,trainPage2,trainPage3;

    private String dataModel="主动训练";
    private String dataNumber="null";
    private String dataMachine="null";
    private int dataStrength=0;
    private int dataTime=0;
    private int dataSpeed=0;
    private TextView trainLevel;
    private long tempSpeed;
    private long tempTime;
    private long tempCount=0;
    private TextView voiceText1,voiceText2,voiceText3,voiceText4,voiceText5;
    private String voiceTemp="加油";
    private Button voiceSendButton;
    private RadioGroup getText;
    private ImageButton changeMachine;
    private TextView machinID;


    private int numberMachinID;
    private int flagRequest;


    DBOpenHelper data;
    private DBOpenHelper2 trainData;

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

    @SuppressLint("WrongViewCast")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        trainData = new DBOpenHelper2(getActivity(), "train.db", null, 1);//新建数据库实例

        mViewModel = ViewModelProviders.of(this).get(TrainViewModel.class);
        MyServiceConnection serviceConnection = new MyServiceConnection();//创建一个MQTT监听服务
        serviceConnection.setIGetMessageCallBack(TrainFragment.this);//和
        Intent intent = new Intent(getActivity(), MQTTService.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        // TODO: Use the ViewModel
        spinner = (Spinner) getView().findViewById(R.id.spinner);
        textView = (TextView) getView().findViewById(R.id.textView);
        textViewName = (TextView) getView().findViewById(R.id.name_patient);
        textViewSex = (TextView) getView().findViewById(R.id.sex_patient);
        textViewAge = (TextView) getView().findViewById(R.id.age_patient);
        textViewIllness = (TextView) getView().findViewById(R.id.illness__patient);
        onOffTrain =(Button)getView().findViewById(R.id.onofftrain);
        countTime = (TextView)getView().findViewById(R.id.RTtime);
        trainSpeed = (TextView)getView().findViewById(R.id.RTspeed);
        trainModel = (RadioGroup)getView().findViewById(R.id.RTmodel);
        trainLevel = (TextView) getView().findViewById(R.id.trainLevel);
        voiceButton = (Button)getView().findViewById(R.id.button7);
        trainPage1 = (View)getView().findViewById(R.id.page1);
        trainPage2=(View)getView().findViewById(R.id.page2);
        voiceText1 =(TextView) getView().findViewById(R.id.editText1);
        voiceText2 =(TextView) getView().findViewById(R.id.editText2);
        voiceText3 =(TextView) getView().findViewById(R.id.editText3);
        voiceText4 =(TextView) getView().findViewById(R.id.editText4);
        voiceText5 =(TextView) getView().findViewById(R.id.editText5);
        voiceSendButton=(Button) getView().findViewById(R.id.button5);
        changeMachine = (ImageButton)getView().findViewById(R.id.changeMachineButton);
        machinID = (TextView) getView().findViewById(R.id.machinID);
        numberMachinID = 1;
        flagRequest=0;

        getText=(RadioGroup)getView().findViewById(R.id.radiogroup);

        onOffTrain.setText("开始训练");
        voiceButton.setText("语音提示");
        trainPage1.setVisibility(View.VISIBLE);
        trainPage2.setVisibility(View.INVISIBLE);

        timeCount = new TrainFragment.TimeCount(1200000, 1000);
        //为dataList赋值，将下面这些数据添加到数据源中
        data = new DBOpenHelper(getActivity(),"hospital.db",null,1);//新建数据库实例

        final ArrayList<String>[] dataList = new ArrayList[]{new ArrayList<String>()};

        dataList[0] = (ArrayList<String>) mViewModel.numPatient(data);
        /*为spinner定义适配器，也就是将数据源存入adapter，这里需要三个参数
        1. 第一个是Context（当前上下文），这里就是this
        2. 第二个是spinner的布局样式，这里用android系统提供的一个样式
        3. 第三个就是spinner的数据源，这里就是dataList*/
        adapter = new ArrayAdapter<String>(this.getContext(),  android.R.layout.simple_spinner_item, dataList[0]);

        //为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //为spinner绑定我们定义好的数据适配器
        spinner.setAdapter(adapter);

        //为spinner绑定监听器，这里我们使用匿名内部类的方式实现监听器
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String dataTemp;
                String numberTemp;
                numberTemp = adapter.getItem(position);
                dataNumber=numberTemp;
                textView.setText("病例号：");
                dataTemp = mViewModel.Patient(data,numberTemp,"name");
                textViewName.setText(dataTemp);
                dataTemp = mViewModel.Patient(data,numberTemp,"sex");
                textViewSex.setText(dataTemp);
                dataTemp = mViewModel.Patient(data,numberTemp,"age");
                textViewAge.setText(dataTemp);
                dataTemp = mViewModel.Patient(data,numberTemp,"illness");
                textViewIllness.setText(dataTemp);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textView.setText("null");

            }
        });

        onOffTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainPage1.setVisibility(View.VISIBLE);
                trainPage2.setVisibility(View.INVISIBLE);


                if(onOffTrain.getText().toString()=="开始训练"){
                    tempSpeed=0;
                    timeCount.start();
                    tempCount=0;
                    flagRequest=1;
                    onOffTrain.setText("结束训练");
                    dataStrength = Integer.parseInt(trainLevel.getText().toString());
                    trainLevel.setFocusable(false);//设置不可编辑
                    Snackbar.make(v, "请确保设备在线", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }
                else {
                    dataTime= (int) tempTime;
//                    tempSpeed= tempSpeed/tempCount;
                    flagRequest=0;

                    dataSpeed= (int) tempSpeed;
                    timeCount.cancel();
                    onOffTrain.setText("开始训练");
                    Snackbar.make(v, dataNumber+dataModel+dataStrength+dataTime+dataSpeed+dataMachine, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    writeData2(trainData.getWritableDatabase(), dataNumber, dataModel,dataStrength,dataTime,dataSpeed,dataMachine);   //调用存储方法
                    Log.d("数据2库相关", "结束 ");

                }

            }
        });
        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceTemp = voiceText1.getText().toString();

                if (voiceButton.getText().toString()=="语音提示"){
                    trainPage2.setVisibility(View.VISIBLE);
                    trainPage1.setVisibility(View.INVISIBLE);
                    onOffTrain.setClickable(false);
                    voiceButton.setText("退出语音");
                    timeCount.onFinish();

                }
                else {
                    timeCount.onFinish();
                    onOffTrain.setClickable(true);

                    trainPage1.setVisibility(View.VISIBLE);
                    trainPage2.setVisibility(View.INVISIBLE);
                    voiceButton.setText("语音提示");
                }
            }
        });
        voiceSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(numberMachinID==1){
                    MQTTService.publish(voiceTemp+"YH");
                }else if(numberMachinID==2){
                    MQTTService.publish(voiceTemp+"YZ1");

                }

                    Snackbar.make(v, "发送语音:"+voiceTemp, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

            }
        });
        getText.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton1:
                        voiceTemp=voiceText1.getText().toString();
                        break;
                    case R.id.radioButton2:
                        voiceTemp=voiceText2.getText().toString();
                        break;
                    case R.id.radioButton3:
                        voiceTemp=voiceText3.getText().toString();
                        break;
                    case R.id.radioButton4:
                        voiceTemp=voiceText4.getText().toString();
                        break;
                    case R.id.radioButton5:
                        voiceTemp=voiceText5.getText().toString();
                        break;
                }
            }
        });
        trainModel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                case R.id.RTmodel1:
                    dataModel="主动训练";
                    break;
                case R.id.RTmodel2:
                    dataModel="被动训练";
                    break;
                case R.id.RTmodel3:
                    dataModel="助力训练";
                    break;
                }
            }
        });
        changeMachine.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view){
                numberMachinID = numberMachinID%8;
                machinID.setText(Integer.toString(numberMachinID+1));
                numberMachinID++;
                changeMachine.setImageResource(R.mipmap.machine2);
                if(numberMachinID==1)MQTTService.publish("open");
                if(numberMachinID==2)MQTTService.publish("Zigbee1");
            }

        });




    }

    @Override
    public void setMessage(String message) {
        String temp;
        Log.d("may", message);

        if(message.contains("opened")){
            changeMachine.setImageResource(R.mipmap.machine1);
        }
        if(message.contains(";")){
            temp=message.split(";")[0];
            tempCount++;
            tempSpeed+=Integer.parseInt(temp);
            trainSpeed.setText(temp);
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tempTime=1200-millisUntilFinished/1000;
            countTime.setText(tempTime+" ");
            if(flagRequest==1)
            MQTTService.publish("request");

        }

        @Override
        public void onFinish() {

//            buttonTemp.setBackgroundColor(Color.parseColor("#FF33B5E5"));
//            button4.setClickable(true);
//            button4.setBackgroundColor(Color.parseColor("#FF33B5E5"));
        }
    }


    //存储数据
    private void writeData2(SQLiteDatabase sqLiteDatabase, String num, String model, int Strength, int time, int speed,String machine){
        ContentValues values = new ContentValues();
        values.put("number",num);
        values.put("model",model);
        values.put("strength",Strength);
        values.put("time",time);
        values.put("speed",speed);
        values.put("machine",machine);
        Log.d("数据2库相关", "过程 ");

        sqLiteDatabase.insert("tb_train",null,values);//保存功能
        Log.d("数据2库相关", "过程结束 ");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(trainData != null){
            trainData.close();//关闭数据库连接
        }
    }

}
