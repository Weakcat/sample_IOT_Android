package com.example.mqttclient;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;

class Machine implements Serializable {
    public int num;
    public boolean state;

    public Machine(int num,boolean state) {
        this.num = num;
        this.state = state;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "num=" + num +
                ", state='" + state + '\'' +
                '}';
    }
}



public class StateViewModel extends ViewModel {
    private MyServiceConnection serviceConnection;
    private MQTTService mqttService;
    // TODO: Implement the ViewModel
    public final MutableLiveData<Machine> mMachineLiveData = new MutableLiveData<>();
    //模拟 进行一些数据骚操作
    public void doSomething() {
        Machine machine = mMachineLiveData.getValue();
        if (machine != null) {
            machine.num = 15;
            machine.state = true;
            mMachineLiveData.setValue(machine);
        }
    }



}
