package com.cloudk.activity;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.cloudk.LineChartView;
import com.cloudk.R;
import com.cloudk.bean.EquiListBean;
import com.cloudk.bean.RealTimeBean;
import com.cloudk.request.RequestServe;
import com.cloudk.tooks.MyLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RealTimeActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "TabViewActivity";
    private static final String URL = "http://www.thxyno1.xin:8899/";
    private boolean isOpen = true;
    private boolean isCanSend = true;
    private boolean isCanEnd = false;
    private String token = "";

    private EquiListBean equiData;
    private RealTimeBean realData;
    private PopupWindow mPopupWindow;
    private PopupWindow mLastPopupWindow;
    private ListView mTypeLv;
    private ArrayAdapter<String> DataAdapter;

    private Call<EquiListBean> equiListCall;
    private Call<RealTimeBean> realTimeCall;

    private LinearLayout atTheMoment;
    private LinearLayout track;
    private LinearLayout history;
    private LinearLayout userInfo;
    private LinearLayout equiList;


    private int i;
    private boolean isFisrtInitLine = true;
    private LineChartView chartView;

    private List<Integer> xList = new ArrayList<>();
    private List<Integer> yList = new ArrayList<>();
    private List<String> dateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime);
        getToken();
        init();
        Listener();
    }

    private void getToken()
    {
        SharedPreferences pref = getSharedPreferences("YunKong",MODE_PRIVATE);
        token = pref.getString("token","");
    }

    private void sendRequestandGetEqui()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
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
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(isCanEnd)
                    {
                        isCanEnd = false;
                        break;
                    }
                }
            }
        }).start();
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
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onFailure(Call<RealTimeBean> call, Throwable t) {
                                MyLog.d(TAG, "onFailure: " + t.toString(), isOpen);
                                isCanSend = true;
                            }
                        });
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(isCanEnd)
                    {
                        isCanEnd = false;
                        break;
                    }
                }
            }
        }).start();
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message message)
        {

            switch(message.what)
            {
                case 0:

                    if(equiData!=null)
                    {
                        isCanSend = true;
                        isCanEnd = true;

                        MyLog.d(TAG, equiData.getCode()+" "+ token,isOpen);
                        switch(equiData.getCode())
                        {

                            case 0:
                                MyLog.d(TAG, "得到Code:"+equiData.getCode(), isOpen);
                                //initAttributesList();
                                //initRecyclerView();
                                xList = new ArrayList<>();
                                yList = new ArrayList<>();
                                sendRequestandgetRealTime();

                                break;
                            case 2001:
                                MyLog.d(TAG, "未登录或登录已过期", isOpen);
//                                Intent toLogin = new Intent(RealTimeActivity.this,LoginActivity.class);
//                                startActivity(toLogin);
                                break;
                        }
                    }
                    break;
                case 1:
                    String date = toDate(realData.getResult().get(0).getTimestamp());
                    xList.add(i);
                    int y = (int) (Math.random()*70+1);
                    yList.add(y);
                    i++;
                    dateList.add(date);
                    if(xList.size()>30)
                    {
                        xList = xList.subList(xList.size()-30,xList.size());
                        yList = yList.subList(yList.size()-30,yList.size());
                        dateList = dateList.subList(dateList.size()-30,dateList.size());
                    }
                    chartView.setDataList(xList,yList,dateList);
                    break;
                case 2:
                    if(realData != null)
                    {
                        switch(equiData.getCode())
                        {
                            case 0:

                                for(int i = 0; i < realData.getResult().size(); i++)
                                {
                                    MyLog.d(TAG, "handleMessage: "+toDate(realData.getResult().get(i).getTimestamp()),isOpen);
                                }

                                if(isFisrtInitLine)
                                {
                                    isFisrtInitLine = false;
                                    for (int i = 0; i < 30; i++) {
                                        xList.add(i);
                                        int yy = (int)(Math.random()*70+1);
                                        yList.add(yy);
                                        String date1 = toDate(realData.getResult().get(0).getTimestamp());
                                        dateList.add(date1);
                                    }
                                    chartView.setDataList(xList,yList,dateList);
                                    startInfo();
                                }

                                break;
                            case 2001:
                                MyLog.d(TAG, "未登录或登录已过期", isOpen);
                                break;
                        }
                    }
                    break;
            }
        }

    };


    private void startInfo()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    private void init()
    {
        atTheMoment = findViewById(R.id.at_the_moment);
        track = findViewById(R.id.track);
        history = findViewById(R.id.history);
        userInfo = findViewById(R.id.user_info);
        equiList = findViewById(R.id.iot_info_list);
        chartView = (LineChartView) findViewById(R.id.chartView);
    }

    private void Listener()
    {
        atTheMoment.setOnClickListener(this);
        track.setOnClickListener(this);
        history.setOnClickListener(this);
        userInfo.setOnClickListener(this);
        equiList.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
                MyLog.d(TAG, "设备列表" , isOpen);
                initPopup(equiData);
                if(mPopupWindow != null && mLastPopupWindow == null)
                {
                    int xOffset = equiList.getWidth()/6;
                    mPopupWindow.showAsDropDown(equiList, xOffset, 0);
                    mLastPopupWindow = mPopupWindow;
                }else if (mPopupWindow != null && !mLastPopupWindow.isShowing()) {
                    int xOffset = equiList.getWidth()/6;
                    mPopupWindow.showAsDropDown(equiList, xOffset, 0);
                    mLastPopupWindow = mPopupWindow;
                }else if(mLastPopupWindow.isShowing())
                {
                    mLastPopupWindow.dismiss();
                }
    }




    private void initPopup(EquiListBean data) {
        mTypeLv = new ListView(this);
        // 设置适配器
        DataAdapter = new ArrayAdapter<String>(this, R.layout.equi_popup, DataConvert(data));
        mTypeLv.setAdapter(DataAdapter);

        // 设置ListView点击事件监听
        mTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // 在这里获取item数据
                    MyLog.d(TAG, "点击列表" , isOpen);

                    // 选择完后关闭popup窗口
                    mPopupWindow.dismiss();

            }
        });
        WindowManager wm = this.getWindowManager();
        mPopupWindow = new PopupWindow(mTypeLv, wm.getDefaultDisplay().getWidth()*2/3,
                wm.getDefaultDisplay().getHeight()/3, true);
        // 取得popup窗口的背景图片
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.bg_filter_down);
        mPopupWindow.setBackgroundDrawable(drawable);
        mPopupWindow.setFocusable(false);
        //mPopupWindow.setOutsideTouchable(true);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }
    //把getData的转换成String
    public List<String> DataConvert(EquiListBean data)
    {
        List<String> str = new ArrayList<>();
//        for(int i = 0; i < data.getResult().get(0).getId().length();i++)
//        {}
            str.add(data.getResult().get(0).getId());


        return str;
    }

    private String toDate(long timestamp)
    {
        SimpleDateFormat format =  new SimpleDateFormat( "HH:mm:ss" );
        String d = format.format(timestamp);
        return d;
    }
}
