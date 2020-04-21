package com.example.mqttclient;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class NewCaseFragment extends Fragment {

    private NewCaseViewModel mViewModel;
    private Button button_save;
    private TextView text_num;
    private TextView text_nam;
    private String string_sex;
    private TextView text_age;
    private TextView text_illness;
    DBOpenHelper dbOpenHelper;//声明DBOpenHelper对象
    private SQLiteDatabase db;

    // TODO: Use the ViewModel


    public static NewCaseFragment newInstance() {
        return new NewCaseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_case_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NewCaseViewModel.class);
        text_num = (EditText) getView().findViewById(R.id.num);  //连接住院号
        text_nam = (EditText) getView().findViewById(R.id.nam);  //连接姓名
        RadioGroup radioSex = (RadioGroup)getView().findViewById(R.id.sex);
        text_age = (EditText) getView().findViewById(R.id.age);  //连接姓名
        text_illness = (EditText) getView().findViewById(R.id.illness);  //

        text_illness = (EditText) getView().findViewById(R.id.illness);  //连接疾病
        button_save = (Button) getView().findViewById(R.id.btn_save);             //连接保存按钮
        dbOpenHelper = new DBOpenHelper(getActivity(),"hospital.db",null,1);//新建数据库实例


        radioSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                string_sex = checkedId == R.id.male ? "男" : "女";
            }
        });




        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = text_num.getText().toString();//获取填写号码
                String name = text_nam.getText().toString(); //获取填写名称
                String age = text_age.getText().toString(); //获取填写疾病

                String illness = text_illness.getText().toString(); //获取填写疾病
                String sex = string_sex;
                String temp1 = null,
                        temp2=null;

                Log.d("yang ","数据" +num+name+string_sex);
                if(false)
                {
                    Toast.makeText(getActivity(), "失败", Toast.LENGTH_SHORT).show() ;
                }
                else {
                    writeData(dbOpenHelper.getWritableDatabase(), num, name,sex,age,illness);   //调用存储方法
                }



            }
        });

    }

    //存储数据
    private void writeData(SQLiteDatabase sqLiteDatabase, String num, String name,String sex,String age,String ill){
        ContentValues values = new ContentValues();
        values.put("number",num);
        values.put("name",name);
        values.put("sex",sex);
        values.put("age",age);
        values.put("illness",ill);

        sqLiteDatabase.insert("tb_patient",null,values);//保存功能
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(dbOpenHelper != null){
            dbOpenHelper.close();//关闭数据库连接
        }
    }

    private void display(){

    }



//    private void changeToAnotherFragment(){
//        //如果是用的v4的包，则用getActivity().getSuppoutFragmentManager();
//        FragmentManager fm = getActivity().getFragmentManager();
//        //注意v4包的配套使用
//        Fragment fragment = new StateFragment();
//        fm.beginTransaction().replace(R.id.item_detail_container,fragment).commit();
//    }

}


