package com.example.mqttclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class EvaluateFragment extends Fragment implements MQTTService.IGetMessageCallBack {
    Button button1, button2, button3,button4,buttonTemp;
    TextView textRealTimeSpeed,textRealTimeTime;
    private MQTTService mqttService;

    private TimeCount time1,time2,time3,timeTemp;

    private EvaluateViewModel mViewModel;

    public static EvaluateFragment newInstance() {
        return new EvaluateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.evaluate_fragment, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(EvaluateViewModel.class);
        MyServiceConnection serviceConnection = new MyServiceConnection();//创建一个MQTT监听服务
        serviceConnection.setIGetMessageCallBack(EvaluateFragment.this);//和
        Intent intent = new Intent(getActivity(), MQTTService.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        // TODO: Use the ViewModel
        button1 = (Button)getView().findViewById(R.id.button2);
        button2 = (Button)getView().findViewById(R.id.button3);
        button3 = (Button)getView().findViewById(R.id.button4);
        button4 = (Button)getView().findViewById(R.id.buttonStartTest);
        RadioGroup radioRTModel = (RadioGroup)getView().findViewById(R.id.RTmodel);
        final RadioButton radioETModel1 = (RadioButton)getView().findViewById(R.id.ETmodel1);
        final RadioButton radioETModel2 = (RadioButton)getView().findViewById(R.id.ETmodel2);
        final RadioButton radioETModel3 = (RadioButton)getView().findViewById(R.id.ETmodel3);

        textRealTimeSpeed = (TextView)getView().findViewById(R.id.RTspeed);
        textRealTimeTime = (TextView)getView().findViewById(R.id.RTtime);

        time1 = new TimeCount(180000, 1000);
        time2 = new TimeCount(600000, 1000);
        time3 = new TimeCount(1200000, 1000);

        radioRTModel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                radioETModel1.setChecked(false);
                radioETModel2.setChecked(false);
                radioETModel3.setChecked(false);
                switch (checkedId){
                    case R.id.RTmodel1:radioETModel1.setChecked(true);
                        break;
                    case R.id.RTmodel2:radioETModel2.setChecked(true);
                        break;
                    case R.id.RTmodel3:radioETModel3.setChecked(true);
                        break;
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeTemp=time1;
                buttonTemp=button1;
                button1.setBackgroundColor(Color.parseColor("#B6B6D8"));
                button1.setClickable(false);
                button2.setClickable(true);
                button2.setBackgroundColor(Color.parseColor("#FF33B5E5"));
                button3.setClickable(true);
                button3.setBackgroundColor(Color.parseColor("#FF33B5E5"));
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeTemp=time2;
                buttonTemp=button2;
                button2.setBackgroundColor(Color.parseColor("#B6B6D8"));
                button2.setClickable(false);
                button1.setClickable(true);
                button1.setBackgroundColor(Color.parseColor("#FF33B5E5"));
                button3.setClickable(true);
                button3.setBackgroundColor(Color.parseColor("#FF33B5E5"));
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeTemp=time3;
                buttonTemp=button3;
                button3.setBackgroundColor(Color.parseColor("#B6B6D8"));
                button3.setClickable(false);
                button2.setClickable(true);
                button2.setBackgroundColor(Color.parseColor("#FF33B5E5"));
                button1.setClickable(true);
                button1.setBackgroundColor(Color.parseColor("#FF33B5E5"));
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeTemp.start();
                button4.setBackgroundColor(Color.parseColor("#B6B6D8"));
                button4.setClickable(false);
                button3.setClickable(false);
                button2.setClickable(false);
                button1.setClickable(false);

            }
        });




    }

    @Override
    public void setMessage(String message) {
        String temp;
        if(message.contains(";")){
        temp=message.split(";")[0];
        textRealTimeSpeed.setText(temp);
        }
    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            textRealTimeTime.setText(millisUntilFinished/1000+" ");
            MQTTService.publish("request");

        }

        @Override
        public void onFinish() {
            buttonTemp.setClickable(true);
            buttonTemp.setBackgroundColor(Color.parseColor("#FF33B5E5"));
            button4.setClickable(true);
            button4.setBackgroundColor(Color.parseColor("#FF33B5E5"));
        }
    }


}
