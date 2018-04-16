package com.cloudk.activity;
/**
 * creat by 世东 on 2018/1/28
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String URL = "http://www.thxyno1.xin:8899/";
    private static final String TAG = "YunKong";
    private static final int LOGIN_SUCCEED = 1;
    private static final int LOGIN_FAILURE = 0;
    private boolean isOpen = true;                          //是否打开日志
    private boolean isClickCanSend = true;                  //用于判断点击登录按钮后是否能再次点击发送

    private Call<ResponseBody> call = null;
    private Data data = null;
    private Button login;
    private Button signin;
    private EditText userName;
    private EditText password;
    private Button passeye;
    private TextView error_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        setContentView(R.layout.activity_login);

        initViewId();
        btnListener();
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message message)
        {
            switch(message.what) {
                case LOGIN_FAILURE:
                    Toast.makeText(LoginActivity.this,"登录失败，检测网络后重新登录",Toast.LENGTH_LONG).show();
                    break;
                case LOGIN_SUCCEED:
                        MyLog.d(TAG, "得到Code: " + data.getCode(), isOpen);
                        isClickCanSend = true;
                        switch (data.getCode()) {
                            //登录成功
                            case 0:
                                //把token保存
                                SharedPreferences.Editor editor = getSharedPreferences("YunKong",MODE_PRIVATE).edit();
                                editor.putString("token",data.getResult());
                                editor.apply();
                                MyLog.d(TAG, "登录成功",isOpen);
                                Intent toTab = new Intent(LoginActivity.this, TabViewActivity.class);
                                startActivity(toTab);
                                finish();
                                break;
                            //无效参数
                            case 2000:
                                MyLog.d(TAG, data.getMsg(),isOpen);
                                error_info.setText("用户名或密码错误");
                                break;
                            case 2005:
                                MyLog.d(TAG, data.getMsg(),isOpen);
                                error_info.setText(data.getMsg());
                                break;
                            default:
                                MyLog.d(TAG, data.getMsg(),isOpen);
                                error_info.setText(data.getMsg());
                                break;
                        }
                    break;
            }
        }

    };

    private void initViewId()
    {
        login = (Button) findViewById(R.id.btn_login);
        signin = (Button) findViewById(R.id.btn_signin);
        userName = (EditText) findViewById(R.id.user_name_edit);
        password = (EditText) findViewById(R.id.user_pass_edit);
        passeye = (Button) findViewById(R.id.set_canornot_see);
        error_info = (TextView) findViewById(R.id.error_info_text);
    }
    private void btnListener()
    {
        login.setOnClickListener(LoginActivity.this);
        signin.setOnClickListener(LoginActivity.this);
        passeye.setOnClickListener(LoginActivity.this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                MyLog.d(TAG, "点击登录按钮", isOpen);
                if(isClickCanSend) {
                    isClickCanSend = false;
                    sendLoginRequset();
                }
                break;
            case R.id.btn_signin:
                MyLog.d(TAG, "点击注册按钮", isOpen);
                Intent intent = new Intent(LoginActivity.this,SigninActivity.class);
                startActivity(intent);
                break;
            case R.id.set_canornot_see:
                MyLog.d(TAG, "点击密码可见按钮", isOpen);
                if(password.getInputType() ==(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT)) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passeye.setBackground(getResources().getDrawable(R.drawable.can_see));
                    password.setSelection(password.length());
                }else {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    passeye.setBackground(getResources().getDrawable(R.drawable.not_see));
                    password.setSelection(password.length());
                }
                break;
        }
    }

    //发送登录请求方法
    private void sendLoginRequset() {
        //该方法用于判断用户名或密码输入是否正确
        if(new JudgeFormat(error_info).isUserandPassRight(userName,password)) {
            MyLog.d(TAG, "发送登录请求", isOpen);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RequestServe requestServe = retrofit.create(RequestServe.class);
            call = requestServe.postLogin(userName.getText().toString(), password.getText().toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        MyLog.d(TAG, response.body().string(), isOpen);
                        Gson gson = new Gson();
                        data = gson.fromJson(response.body().string(), new TypeToken<Data>() {
                        }.getType());

                        //TODO 截取登录成功消息
                        Message message = new Message();
                        message.what = LOGIN_SUCCEED;
                        handler.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    MyLog.d(TAG, "请求数据失败: " + t.toString(), isOpen);
                    //TODO 截取登录失败消息
                    Message message = new Message();
                    message.what = LOGIN_FAILURE;
                    handler.sendMessage(message);
                    isClickCanSend = true;
                }
            });
        }
    }
}
