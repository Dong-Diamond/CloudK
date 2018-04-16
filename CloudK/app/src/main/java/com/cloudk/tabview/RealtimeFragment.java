package com.cloudk.tabview;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.cloudk.R;
import com.cloudk.bean.EquiListBean;
import com.cloudk.bean.RealTimeBean;
import com.cloudk.request.RequestServe;
import com.cloudk.tooks.MyLog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by 世东 on 18/2/3.
 */
public class RealtimeFragment extends BaseFragment implements ITabClickListener,OnChartGestureListener {

    private static final String TAG = "YunKong";
    private static final String URL = "http://www.thxyno1.xin:8899/";
    private boolean isOpen = true;
    private boolean isCanSend = true;
    private boolean isCanEnd = false;
    private boolean isFisrt = true;
    private boolean isFisrtGetRealtimeData = true;
    private Call<EquiListBean> equiListCall;
    private EquiListBean equiData;
    private Call<RealTimeBean> realTimeCall;
    private String token = "";
    private RealTimeBean realData;
    private int stopMillis = 2000;                  //多少毫秒发送一次请求
    private Thread send;


    // TODO: 折线
    private LineDataSet dataSet;
    private LineData mChartData;
    private XAxis xAxis;
    private static final int maxX = 20;
    //所有线的List
    private ArrayList<LineDataSet> allLinesList = new ArrayList<LineDataSet>();
    private LineChart mChart;

    @Override
    public void fetchData() {
        //首次点开页面执行
        getToken();
        sendRequestandGetEqui();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_realtime, container, false);
        mChart= (LineChart) view.findViewById(R.id.realtime_line_chart1);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MyLog.d(TAG, "onActivityCreated: ", isOpen);
    }

    @Override
    public void onStart() {
        super.onStart();
        MyLog.d(TAG, "onStart: ", isOpen);
    }

    @Override
    public void onResume() {
        super.onResume();
        MyLog.d(TAG, "onResume: ", isOpen);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.d(TAG, "onDestroy: ", isOpen);
    }

    @Override
    public void onPause() {
        super.onPause();
        MyLog.d(TAG, "onPause: ", isOpen);
    }

    @Override
    public void onStop() {
        super.onStop();
        MyLog.d(TAG, "onStop: ", isOpen);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MyLog.d(TAG, "onDestroyView: ", isOpen);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message message)
        {
            switch(message.what)
            {
                case 0:
                    if(equiData!=null && isFisrt)
                    {
                        isFisrt = false;
//                        isCanSend = true;
                        isCanEnd = true;
                        switch(equiData.getCode())
                        {
                            case 0:
                                MyLog.d(TAG, "得到Code:"+equiData.getCode(), isOpen);
                                //sendRequestandgetRealTime();
                                break;
                            case 2001:
                                MyLog.d(TAG, "未登录或登录已过期", isOpen);
                                break;
                        }
                    }
                    break;
                case 1:
                    if(realData != null)
                    {
                        if (isFisrtGetRealtimeData)
                        {
                            isFisrtGetRealtimeData = false;
                            loadLineChartData(mChart);
                            setLineChart(mChart);
                            byValuetoLineChart();
                        }else {

                            byValuetoLineChart();
                        }


                        MyLog.d(TAG, "得到实时数据", isOpen);
                    }
                    break;
            }
        }

    };



    private void getToken()
    {
        SharedPreferences pref = getContext().getSharedPreferences("YunKong",Context.MODE_PRIVATE);
        token = pref.getString("token","");
    }

    @Override
    public void onMenuItemClick() {
        Log.d(TAG, "onMenuItemClick: ");
    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }

    private void loadLineChartData(final LineChart chart) {
        dataSet = new LineDataSet(null, "dataLine");
        dataSet.setDrawCircleHole(true);
        dataSet.setDrawFilled(true);
        dataSet.setLineWidth(1.0f);
        dataSet.setCircleSize(1.0f);
        dataSet.setHighLightColor(Color.RED); // 设置点击某个点时，横竖两条线的颜色
        dataSet.setDrawValues(true); // 是否在点上绘制Value
        dataSet.setValueTextColor(Color.GREEN);
        dataSet.setValueTextSize(5f);
        allLinesList.add(dataSet);
        mChartData = new LineData(getXAxisShowLable(), allLinesList);
        mChart.setData((LineData) mChartData);
    }
    private void setLineChart(LineChart chart) {
        chart.setOnChartGestureListener(this);
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(true);
        chart.setDoubleTapToZoomEnabled(true);
        chart.setScaleEnabled(false); // 设置缩放
        chart.setDoubleTapToZoomEnabled(false); // 设置双击不进行缩放
        chart.setAutoScaleMinMaxEnabled(true);
        chart.setLogEnabled(false);
        chart.setDragEnabled(false);
        chart.setNoDataText("");
        chart.setVisibleXRangeMaximum(maxX);
        /**
         * 设置X轴
         */
        xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的位置
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(1);
        /**
         * 获得左侧侧坐标轴
         */
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextSize(10f);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //设置右侧坐标轴
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawAxisLine(false); // 右侧坐标轴线
        rightAxis.setDrawLabels(false);
    }
    private ArrayList<String> getXAxisShowLable() {
        ArrayList<String> m = new ArrayList<String>();
        SimpleDateFormat format =  new SimpleDateFormat( "HH:mm:ss" );
//        long time = Calendar.getInstance().getTimeInMillis();
        long time = realData.getResult().get(0).getTimestamp();
        for(long i = time; i<time+86400*stopMillis; i+=stopMillis)
        {
            m.add(format.format(i));
        }
        return m;
    }

    private void sendRequestandGetEqui()
    {
        send = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    if (isCanEnd)
                    {
                        isCanEnd = false;
                        break;
                    }
                    if(isCanSend){
                        isCanSend = false;
                        MyLog.d(TAG, "发送请求", isOpen);
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        RequestServe getDataServes = retrofit.create(RequestServe.class);
                        equiListCall = getDataServes.getEqui(token);
                        equiListCall.enqueue(new Callback<EquiListBean>() {
                            @Override
                            public void onResponse(Call<EquiListBean> call, Response<EquiListBean> response) {
                                try {
                                    equiData = new EquiListBean();
                                    equiData.setCode(response.body().getCode());
                                    equiData.setResult(response.body().getResult());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onFailure(Call<EquiListBean> call, Throwable t) {
                                MyLog.d(TAG, "onFailure: " + t.toString(), isOpen);
                                isCanSend = true;
                            }
                        });
                    }
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(stopMillis);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        send.start();
    }

    private void sendRequestandgetRealTime()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    MyLog.d(TAG, "发送实时数据请求", isOpen);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    RequestServe getDataServes = retrofit.create(RequestServe.class);
                    realTimeCall = getDataServes.getRealTime(token,equiData.getResult().get(0).getId());
                    realTimeCall.enqueue(new Callback<RealTimeBean>() {
                        @Override
                        public void onResponse(Call<RealTimeBean> call, Response<RealTimeBean> response) {
                            try {
                                realData = new RealTimeBean();
                                realData.setCode(response.body().getCode());
                                realData.setResult(response.body().getResult());

                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<RealTimeBean> call, Throwable t) {
                            MyLog.d(TAG, "onFailure: " + t.toString(), isOpen);
                        }
                    });

                    try {
                        Thread.sleep(stopMillis);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private void byValuetoLineChart()
    {
        dataSet.addEntry(new Entry(Float.parseFloat(realData.getResult().get(0).getAttributeValue()),
                dataSet.getEntryCount()));
        //移到某个位置
        mChart.moveViewToX(dataSet.getEntryCount() - maxX -1);
        mChart.notifyDataSetChanged();
        mChart.invalidate();
        Log.d("Main", "run: "+dataSet.getEntryCount());
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }
}
