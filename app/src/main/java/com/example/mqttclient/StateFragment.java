package com.example.mqttclient;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class StateFragment extends Fragment {

    private StateViewModel mViewModel;
    private TextView mContentTv;


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



        mViewModel.mMachineLiveData.observe(this, new Observer<Machine>() {
            @Override
            public void onChanged(Machine machine) {
                mContentTv.setText(machine.toString());

            }
        });
//        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //点击按钮  更新User数据  观察TextView变化
//                mViewModel.doSomething();
//            }
//        });

    };

}
