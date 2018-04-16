package com.cloudk.activity;
/**
 * creat by 世东 on 2018/1/28
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cloudk.R;
import com.cloudk.bean.CheckTokenBean;
import com.cloudk.request.Data;
import com.cloudk.request.RequestServe;
import com.cloudk.tooks.MyLog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StartActivity extends AppCompatActivity {
    private static final String TAG = "YunKong";
    private static final String URL = "http://www.thxyno1.xin:8899/";
    private static final int SUCCEED = 1;
    private static final int FAILURE = 0;
    private String token = "";
    private Data data;
    private boolean istoOtherActivity = false;
    private boolean isOpen = true;
    private boolean isCanSend = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if(isHadTokey()) {
            if(data==null) {
                MyLog.d(TAG, "token存在" + token, isOpen);
                MyLog.d(TAG, "检验token是否过期", isOpen);
                //发送请求
                sendRequest();
            }
        }
        else{
            MyLog.d(TAG, "token不存在",isOpen);
            Intent toLogin = new Intent(StartActivity.this,LoginActivity.class);
            startActivity(toLogin);
            StartActivity.this.finish();
        }
    }
    private Handler handler = new Handler(){
        public void handleMessage(Message message)
        {
            switch(message.what)
            {
                case FAILURE:
                    if(!istoOtherActivity) {
                        isCanSend = true;
                        sendRequest();
                    }
                    break;

                case SUCCEED:
                    switch (data.getCode()){
                        case 0:
                            if(!istoOtherActivity) {
                                istoOtherActivity = true;
                                MyLog.d(TAG, "检验token后登录成功", isOpen);
                                Intent toInfo = new Intent(StartActivity.this,TabViewActivity.class);
                                startActivity(toInfo);
                                StartActivity.this.finish();
                            }
                            break;
                        case 2001:
                            if(!istoOtherActivity) {
                                MyLog.d(TAG, data.getMsg(), isOpen);
                                Intent toLogin = new Intent(StartActivity.this,LoginActivity.class);
                                startActivity(toLogin);
                                StartActivity.this.finish();
                            }
                            break;
                    }
                    break;
            }
        }

    };

    //判断是否有tokey值
    private boolean isHadTokey() {
        SharedPreferences pref = getSharedPreferences("YunKong",MODE_PRIVATE);
        token = pref.getString("token","");
        if(pref.getString("token",null)==null)
        {
            return false;
        }
        return true;
    }



    private void sendRequest() {
        if(isCanSend && data==null) {
            isCanSend = false;
            MyLog.d(TAG, "发送检验token请求", isOpen);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RequestServe requestServe = retrofit.create(RequestServe.class);
            Call<CheckTokenBean> call = requestServe.checkInfo(token);
            call.enqueue(new Callback<CheckTokenBean>() {
                @Override
                public void onResponse(Call<CheckTokenBean> call, Response<CheckTokenBean> response) {
                    int code = response.body().getCode();
                    data = new Data();
                    data.setCode(code);
                    data.setMsg(response.body().getMsg());

                    Message message = new Message();
                    message.what = SUCCEED;
                    handler.sendMessage(message);
                }

                @Override
                public void onFailure(Call<CheckTokenBean> call, Throwable t) {
                    Log.d(TAG, "onClick: 4" + t.toString());
                    Message message = new Message();
                    message.what = FAILURE;
                    handler.sendMessage(message);
                }
            });
        }
    }
}
