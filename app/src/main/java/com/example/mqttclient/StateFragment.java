package com.example.mqttclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class StateFragment extends Fragment implements MQTTService.IGetMessageCallBack  {

    private StateViewModel mViewModel;
    private TextView mContentTv;
    private MQTTService mqttService;
    private boolean flage_send;

    private TimeCount timeCount;
    private ImageButton machine1,machine2,machine3,machine4,machine5,machine6,machine7,machine8;


    public static StateFragment newInstance() {
        return new StateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("test","进入state");

        return inflater.inflate(R.layout.state_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StateViewModel.class);
        // TODO: Use the ViewModel
        MyServiceConnection serviceConnection = new MyServiceConnection();//创建一个MQTT监听服务
        serviceConnection.setIGetMessageCallBack(StateFragment.this);//和
        Intent intent = new Intent(getActivity(), MQTTService.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        machine1 = (ImageButton)getView().findViewById(R.id.state_machine_1);
        machine2 = (ImageButton)getView().findViewById(R.id.state_machine_2);
        machine3 = (ImageButton)getView().findViewById(R.id.state_machine_3);
        machine4 = (ImageButton)getView().findViewById(R.id.state_machine_4);
        machine5 = (ImageButton)getView().findViewById(R.id.state_machine_5);
        machine6 = (ImageButton)getView().findViewById(R.id.state_machine_6);
        machine7 = (ImageButton)getView().findViewById(R.id.state_machine_7);
        machine8 = (ImageButton)getView().findViewById(R.id.state_machine_8);

        machine1.setImageResource(R.mipmap.machine2);
        machine2.setImageResource(R.mipmap.machine2);
        machine3.setImageResource(R.mipmap.machine2);
        machine4.setImageResource(R.mipmap.machine2);
        machine5.setImageResource(R.mipmap.machine2);
        machine6.setImageResource(R.mipmap.machine2);
        machine7.setImageResource(R.mipmap.machine2);
        machine8.setImageResource(R.mipmap.machine2);

        flage_send=true;
        timeCount = new StateFragment.TimeCount(10000, 1000);
        mViewModel.mMachineLiveData.observe(this, new Observer<Machine>() {
            @Override
            public void onChanged(Machine machine) {
                mContentTv.setText(machine.toString());

            }
        });
        timeCount.start();

//        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //点击按钮  更新User数据  观察TextView变化
//                mViewModel.doSomething();
//            }
//        });

    };

    @Override
    public void setMessage(String message) {

        Log.d("yang ", "cahange machine state");
        if(message.contains("opened")){
            machine1.setImageResource(R.mipmap.machine1);

        }
        if(message.contains("opened1")){
            machine2.setImageResource(R.mipmap.machine1);
            timeCount.onFinish();

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        timeCount.onFinish();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if(flage_send){
                MQTTService.publish("open");

            }else{
                MQTTService.publish("Zigbee1");

            }
            flage_send=!flage_send;
        }

        @Override
        public void onFinish() {

        }
    }
}
