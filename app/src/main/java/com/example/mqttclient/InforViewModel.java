package com.example.mqttclient;

import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.ViewModel;

public class InforViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    boolean resetDta(SQLiteDatabase db){
        db.execSQL("drop table tb_patient");
        return true;
    }

}
