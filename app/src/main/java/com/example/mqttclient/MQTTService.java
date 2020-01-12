package com.example.mqttclient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
//可以创建一个服务来接收MQTT的消息
public class MQTTService extends Service {
    public MQTTService() {
    }

    private String host = "tcp://49.234.235.161:1883";
    private String userName = "yang";
    private String passWord = "yang";
    private static String myTopic = "ForTest";      //要订阅的主题
    private String clientId = "androidId";//客户端标识

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }
}
