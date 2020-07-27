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

import java.util.ArrayList;

public class NewCaseFragment extends Fragment {

    private NewCaseViewModel mViewModel;
    private Button button_save;
    private TextView text_num;
    private TextView text_nam;
    private String string_sex;
    private TextView text_age;
    private TextView text_illness;
    private TextView num1,num2,num3,num4,num5,num6,num7,num8;
    private TextView nam1,nam2,nam3,nam4,nam5,nam6,nam7,nam8;
    private TextView sex1,sex2,sex3,sex4,sex5,sex6,sex7,sex8;
    private TextView age1,age2,age3,age4,age5,age6,age7,age8;
    private TextView ill1,ill2,ill3,ill4,ill5,ill6,ill7,ill8;

    private int pageNum;
    DBOpenHelper dataPatient;

    private Button refresh;


    DBOpenHelper dbOpenHelper;//声明DBOpenHelper对象
//    private SQLiteDatabase db;如果不行该这里

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
        num1=(TextView) getView().findViewById(R.id.num1);
        num2=(TextView) getView().findViewById(R.id.num2);
        num3=(TextView) getView().findViewById(R.id.num3);
        num4=(TextView) getView().findViewById(R.id.num4);
        num5=(TextView) getView().findViewById(R.id.num5);
        num6=(TextView) getView().findViewById(R.id.num6);
        num7=(TextView) getView().findViewById(R.id.num7);
        num8=(TextView) getView().findViewById(R.id.num8);

        nam1=(TextView) getView().findViewById(R.id.nam1);
        nam2=(TextView) getView().findViewById(R.id.nam2);
        nam3=(TextView) getView().findViewById(R.id.nam3);
        nam4=(TextView) getView().findViewById(R.id.nam4);
        nam5=(TextView) getView().findViewById(R.id.nam5);
        nam6=(TextView) getView().findViewById(R.id.nam6);
        nam7=(TextView) getView().findViewById(R.id.nam7);
        nam8=(TextView) getView().findViewById(R.id.nam8);

        sex1=(TextView) getView().findViewById(R.id.sex1);
        sex2=(TextView) getView().findViewById(R.id.sex2);
        sex3=(TextView) getView().findViewById(R.id.sex3);
        sex4=(TextView) getView().findViewById(R.id.sex4);
        sex5=(TextView) getView().findViewById(R.id.sex5);
        sex6=(TextView) getView().findViewById(R.id.sex6);
        sex7=(TextView) getView().findViewById(R.id.sex7);
        sex8=(TextView) getView().findViewById(R.id.sex8);

        age1=(TextView) getView().findViewById(R.id.age1);
        age2=(TextView) getView().findViewById(R.id.age2);
        age3=(TextView) getView().findViewById(R.id.age3);
        age4=(TextView) getView().findViewById(R.id.age4);
        age5=(TextView) getView().findViewById(R.id.age5);
        age6=(TextView) getView().findViewById(R.id.age6);
        age7=(TextView) getView().findViewById(R.id.age7);
        age8=(TextView) getView().findViewById(R.id.age8);

        ill1=(TextView) getView().findViewById(R.id.ill1);
        ill2=(TextView) getView().findViewById(R.id.ill2);
        ill3=(TextView) getView().findViewById(R.id.ill3);
        ill4=(TextView) getView().findViewById(R.id.ill4);
        ill5=(TextView) getView().findViewById(R.id.ill5);
        ill6=(TextView) getView().findViewById(R.id.ill6);
        ill7=(TextView) getView().findViewById(R.id.ill7);
        ill8=(TextView) getView().findViewById(R.id.ill8);

        pageNum = 1;

        refresh = (Button) getView().findViewById(R.id.refresh);

        text_illness = (EditText) getView().findViewById(R.id.illness);  //连接疾病
        button_save = (Button) getView().findViewById(R.id.btn_save);             //连接保存按钮
        dbOpenHelper = new DBOpenHelper(getActivity(),"hospital.db",null,1);//新建数据库实例
        display();

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


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("temp", "onClick: ");
                display();



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
        final ArrayList<String>[] dataList = new ArrayList[]{new ArrayList<String>()};
        dataPatient = new DBOpenHelper(getActivity(),"hospital.db",null,1);//新建数据库实例

        dataList[0] = (ArrayList<String>) mViewModel.numPatient(dataPatient);

        Log.d("data", dataList[0].toString());



        num1.setText("test");
        nam1.setText("test");
        sex1.setText("sex");
        age1.setText("age");
        ill1.setText("ill");

        num2.setText("test");
        nam2.setText("test");
        sex2.setText("sex");
        age2.setText("age");
        ill2.setText("ill");

        num3.setText("test");
        nam3.setText("test");
        sex3.setText("sex");
        age3.setText("age");
        ill3.setText("ill");

        num4.setText("test");
        nam4.setText("test");
        sex4.setText("sex");
        age4.setText("age");
        ill4.setText("ill");

        num5.setText("test");
        nam5.setText("test");
        sex5.setText("sex");
        age5.setText("age");
        ill5.setText("ill");

        num6.setText("test");
        nam6.setText("test");
        sex6.setText("sex");
        age6.setText("age");
        ill6.setText("ill");

        num7.setText("test");
        nam7.setText("test");
        sex7.setText("sex");
        age7.setText("age");
        ill7.setText("ill");

        num8.setText("test");
        nam8.setText("test");
        sex8.setText("sex");
        age8.setText("age");
        ill8.setText("ill");


    }



//    private void changeToAnotherFragment(){
//        //如果是用的v4的包，则用getActivity().getSuppoutFragmentManager();
//        FragmentManager fm = getActivity().getFragmentManager();
//        //注意v4包的配套使用
//        Fragment fragment = new StateFragment();
//        fm.beginTransaction().replace(R.id.item_detail_container,fragment).commit();
//    }

}


