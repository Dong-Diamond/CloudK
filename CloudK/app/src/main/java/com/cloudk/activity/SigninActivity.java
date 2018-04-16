package com.cloudk.activity;
/**
 * creat by 世东 on 2018/1/28
 */

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudk.R;
import com.cloudk.request.Data;
import com.cloudk.request.RequestServe;
import com.cloudk.tooks.JudgeFormat;
import com.cloudk.tooks.MyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "YunKong";
    private static final String URL = "http://www.thxyno1.xin:8899/";
    private static final int SIGNIN_SUCCEED = 1;
    private static final int SIGNIN_FAILURE = 0;
    private static final int CLICK_USERNAME_SUCCEED = 2;
    private boolean isOpen = true;

    private boolean isClickCanSend = true;
    private Call<ResponseBody> call;
    private Data data;
    private EditText userName;
    private EditText password;
    private Button signin;
    private Button cancel;
    private TextView error_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        setContentView(R.layout.activity_signin);

        init();
        addListener();
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message message)
        {
            switch(message.what) {
                //判断用户名是否可用
                case CLICK_USERNAME_SUCCEED:
                        isClickCanSend = true;
                        switch (data.getCode()) {
                            case 0:
                                MyLog.d(TAG, "该用户可以注册",isOpen);
                                break;
                            case 2006:
                                MyLog.d(TAG, data.getMsg(), isOpen);
                                break;
                            default:
                                MyLog.d(TAG, "default " + data.getMsg(), isOpen);
                                break;
                        }
                    break;
                case SIGNIN_FAILURE:
                    Toast.makeText(SigninActivity.this,"注册失败，检测网络后重新登录",Toast.LENGTH_LONG).show();
                    break;
                case SIGNIN_SUCCEED:
                    isClickCanSend = true;
                    switch (data.getCode()) {
                        //注册成功
                        case 0:
                            MyLog.d(TAG, "注册成功", isOpen);
                            break;
                        //该用户已注册
                        case 2006:
                            MyLog.d(TAG,data.getMsg(),isOpen);
                            break;
                        default:
                            MyLog.d(TAG, "Code default",isOpen);
                            break;
                    }
                    break;
            }
        }

    };

    private void init() {
        userName = (EditText) findViewById(R.id.signin_user_name_edit);
        password = (EditText) findViewById(R.id.signin_user_pass_edit);
        signin = (Button) findViewById(R.id.btn_signin);
        cancel = (Button) findViewById(R.id.btn_cancel);
        error_info = (TextView) findViewById(R.id.error_info_text);
    }

    private void addListener() {
        password.setOnClickListener(SigninActivity.this);
        signin.setOnClickListener(SigninActivity.this);
        cancel.setOnClickListener(SigninActivity.this);
        userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    // 获得焦点
                    MyLog.d(TAG, "光标进入用户名输入文本", isOpen);
                } else {
                    // 失去焦点
                    MyLog.d(TAG, "光标离开用户名输入文本", isOpen);
                    sendClickUserName();
                }
            }


        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_signin:
                MyLog.d(TAG, "点击注册",isOpen);
                if(isClickCanSend) {
                    isClickCanSend = false;
                    sendSigninRequest();
                }
        }
    }

    public void sendSigninRequest() {
        if (data==null  && new JudgeFormat().isUserandPassRight(userName,password)) {
            MyLog.d(TAG, "发送注册请求",isOpen);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RequestServe requestServe = retrofit.create(RequestServe.class);
            call = requestServe.postSignin(userName.getText().toString(), password.getText().toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Gson gson = new Gson();
                        data = gson.fromJson(response.body().string(), new TypeToken<Data>() {
                        }.getType());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = SIGNIN_SUCCEED;
                    handler.sendMessage(message);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    MyLog.d(TAG, "请求数据失败: " + t.toString(),isOpen);
                    Message message = new Message();
                    message.what = SIGNIN_FAILURE;
                    handler.sendMessage(message);
                    isClickCanSend = true;
                }
            });
        }else if(!new JudgeFormat().isUserandPassRight(userName,password)) {
            MyLog.d(TAG, "用户名或密码输入格式有误",isOpen);
        }

    }

    public void sendClickUserName() {
        if (new JudgeFormat().isUserRight(userName)) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RequestServe requestServe = retrofit.create(RequestServe.class);
            call = requestServe.isUsable(userName.getText().toString(),"1");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Gson gson = new Gson();
                        data = gson.fromJson(response.body().string(), new TypeToken<Data>() {
                        }.getType());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Message message = new Message();
                    message.what = CLICK_USERNAME_SUCCEED;
                    handler.sendMessage(message);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }else if(!new JudgeFormat().isUserRight(userName)) {
            MyLog.d(TAG, "用户名输入格式有误", isOpen);
        }
    }

}
