package com.example.mqttclient;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class StatisticFragment extends Fragment {

    private StatisticViewModel mViewModel;

    private ListView listview;


    private ArrayList<Integer> myTestSQLData;



    //private List<StepEntity> stepEntityList = new ArrayList<>();
    //private StepDataDao stepDataDao;
    private LineChartView chart;
    private LineChartData data;          // 折线图封装的数据类
    private  int numberOfLines = 1;
    private int maxNumberOfLines = 3;///
    private int numberOfPoints = 12;


    private boolean hasAxes = true;       //是否有轴，x和y轴
    private boolean hasAxesNames = true;   //是否有轴的名字
    private boolean hasLines = true;       //是否有线（点和点连接的线）
    private boolean hasPoints = true;       //是否有点（每个值的点）
    private ValueShape shape = ValueShape.CIRCLE;    //点显示的形式，圆形，正方向，菱形
    private boolean isFilled = false;                //是否是填充
    private boolean hasLabels = false;               //每个点是否有名字
    private boolean isCubic = false;                 //是否是立方的，线条是直线还是弧线
    private boolean hasLabelForSelected = false;       //每个点是否可以选择（点击效果）
    private boolean pointsHaveDifferentColor;           //线条的颜色变换
    private boolean hasGradientToTransparent = false;      //是否有梯度的透明

    private String numberTemp;
private String myTestXName;


    String[] date ;
    float [] score;
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    DBOpenHelper dataPatient;
    DBOpenHelper2 dataTrain;
    //首先还是先声明这个Spinner控件
    private Spinner spinner;
    private TextView namPatient;
    private TextView trainDate;

    //定义一个ArrayAdapter适配器作为spinner的数据适配器
    private ArrayAdapter<String> adapter;



    public static StatisticFragment newInstance() {
        return new StatisticFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.statistic_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StatisticViewModel.class);
        // TODO: Use the ViewModel


        RadioGroup radioShowChart = (RadioGroup)getView().findViewById(R.id.showchart);
        final RadioButton radioShowChart1 = (RadioButton)getView().findViewById(R.id.showchart1);
        final RadioButton radioShowChart2 = (RadioButton)getView().findViewById(R.id.showchart2);
        final RadioButton radioShowChart3 = (RadioButton)getView().findViewById(R.id.showchart3);
        spinner = (Spinner) getView().findViewById(R.id.statisticSpinner);
        namPatient=(TextView)getView().findViewById(R.id.train_name_patient);
        trainDate=(TextView)getView().findViewById(R.id.data);

        //为dataList赋值，将下面这些数据添加到数据源中
        dataPatient = new DBOpenHelper(getActivity(),"hospital.db",null,1);//新建数据库实例
        dataTrain = new DBOpenHelper2(getActivity(),"train.db",null,1);//新建数据库实例

        final ArrayList<String>[] dataList = new ArrayList[]{new ArrayList<String>()};
        dataList[0] = (ArrayList<String>) mViewModel.numPatient(dataPatient);




        //dataList[0] = (ArrayList<String>) mViewModel.numPatient(data);
        /*为spinner定义适配器，也就是将数据源存入adapter，这里需要三个参数
        1. 第一个是Context（当前上下文），这里就是this
        2. 第二个是spinner的布局样式，这里用android系统提供的一个样式
        3. 第三个就是spinner的数据源，这里就是dataList*/
        adapter = new ArrayAdapter<String>(this.getContext(),  android.R.layout.simple_gallery_item, dataList[0]);

        //为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //为spinner绑定我们定义好的数据适配器
        spinner.setAdapter(adapter);

        //为spinner绑定监听器，这里我们使用匿名内部类的方式实现监听器
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String dataTemp;
                numberTemp = adapter.getItem(position);
                dataTemp = mViewModel.Patient(dataPatient,numberTemp,"name");
                namPatient.setText(dataTemp);


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                textView.setText("请选择接受训练的病人");

            }
    });

        initView();
        radioShowChart.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.showchart1:
                        myTestSQLData=(ArrayList<Integer>) mViewModel.myTrainData(dataTrain,numberTemp,"strength");
                        myTestXName="强度";
                        initData();
                        break;
                    case R.id.showchart2:
                        myTestSQLData=(ArrayList<Integer>) mViewModel.myTrainData(dataTrain,numberTemp,"time");
                        myTestXName="时间";
                        initData();
                        break;
                    case R.id.showchart3:
                        myTestSQLData=(ArrayList<Integer>) mViewModel.myTrainData(dataTrain,numberTemp,"speed");
                        myTestXName="速率";
                        initData();
                        break;
                }
            }
        });
    }


    private void initView() {
        //实例化
        chart = (LineChartView) getView().findViewById(R.id.line_chart);
    }

    private void initData( ) {
        generateData();    //设置数据
        // Disable viewport recalculations, see toggleCubic() method for more info.
        chart.setViewportCalculationEnabled(false);

        //chart.setZoomType(ZoomType.HORIZONTAL);//设置线条可以水平方向收缩，默认是全方位缩放
        resetViewport();   //设置折线图的显示大小
    }

    private void initEvent() {
        //chart.setOnValueTouchListener(new ValueTouchListener());

    }

    /**
     * 图像显示大小
     */
    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = 50;
        v.left = 0;
        v.right = numberOfPoints - 1;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }



    /**
     * 配置数据
     */
    private void generateData( ) {

        Log.d("统计图相关", "最开始 ");
        //存放线条对象的集合
        List<Line> lines = new ArrayList<Line>();
        //把数据设置到线条上面去
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < myTestSQLData.size(); ++j) {
                Log.d("统计图相关", "开始 ");

                values.add(new PointValue(j, (float)myTestSQLData.get(0)));
                Log.d("统计图相关", "结束 ");


            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);//不知道怎么改
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
           // line.setHasGradientToTransparent(hasGradientToTransparent);
            if (pointsHaveDifferentColor) {
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setTextColor(Color.BLACK);//设置x轴字体的颜色
                axisY.setTextColor(Color.BLACK);//设置y轴字体的颜色
                axisX.setTextSize(24);
                axisY.setTextSize(24);
                axisX.setName("次 数");
                axisY.setName(myTestXName);
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

    }



}
