package com.example.mqttclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class InforFragment extends Fragment {

    private InforViewModel mViewModel;
    private Button button_reset;

    public static InforFragment newInstance() {
        return new InforFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.infor_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(InforViewModel.class);
        button_reset = (Button) getView().findViewById(R.id.reset);             //连接保存按钮

        // TODO: Use the ViewModel

        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBOpenHelper dbOpenHelper = new DBOpenHelper(getActivity(), "hospital.db", null, 1);//新建数据库实例
                mViewModel.resetDta(dbOpenHelper.getWritableDatabase());

            }
        });
    }

}
