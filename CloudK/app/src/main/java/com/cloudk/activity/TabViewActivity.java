package com.cloudk.activity;

/**
 * creat by 世东 on 2018/2/5
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cloudk.R;
import com.cloudk.bean.EquiListBean;
import com.cloudk.bean.ExitBean;
import com.cloudk.request.RequestServe;
import com.cloudk.tabview.BaseFragment;
import com.cloudk.tabview.HistoryFragment;
import com.cloudk.tabview.LocationFragment;
import com.cloudk.tabview.RealtimeFragment;
import com.cloudk.tabview.TabItem;
import com.cloudk.tabview.TabLayout;
import com.cloudk.tabview.TrackFragment;
import com.cloudk.tooks.MyLog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TabViewActivity extends AppCompatActivity implements TabLayout.OnTabClickListener, View.OnClickListener {
    private static final String TAG = "YunKong";
    private static final String URL = "http://www.thxyno1.xin:8899/";
    private static final int GET_EQUI_SUCCEED = 0;
    private static final int GET_EQUI_FAILURE = 1;
    private static final int EXIT_SUCCEED = 2;
    private static final int EXIT_FAILURE = 3;
    private static final boolean isOpen = true;             //是否打印日志
    private boolean isSendExit = false;
    private String token = null;
    private EquiListBean equiList;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<TabItem> tabs;
    private BaseFragment fragment;
    private LinearLayout equiTitle;
    private TextView equiText;
    private ImageButton equiDown;
    private Button menu;


    private PopupWindow mPopupWindow;
    private PopupWindow mLastPopupWindow;
    private ListView mTypeLv;
    private ArrayAdapter<String> DataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tebview);

        getToken();
        initView();
        listener();
        initData();
        sendGetEquiRequest();
    }



    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        equiTitle = (LinearLayout) findViewById(R.id.equi_info_title);
        equiText = (TextView) findViewById(R.id.equi_info_text);
        equiDown = (ImageButton) findViewById(R.id.equi_info_down);
        menu = (Button) findViewById(R.id.menu);
    }
    private void listener()
    {
        equiTitle.setOnClickListener(this);
        equiText.setOnClickListener(this);
        equiDown.setOnClickListener(this);
        menu.setOnClickListener(this);
    }


    private void initData() {

        tabs = new ArrayList<>();
        tabs.add(new TabItem(R.drawable.selector_tab_realtime, R.string.realtime, RealtimeFragment.class));
        tabs.add(new TabItem(R.drawable.selector_tab_track, R.string.track, TrackFragment.class));
        tabs.add(new TabItem(R.drawable.selector_tab_history, R.string.history, HistoryFragment.class));
        tabs.add(new TabItem(R.drawable.selector_tab_location, R.string.user, LocationFragment.class));
        mTabLayout.initData(tabs, this);
        mTabLayout.setCurrentTab(0);

        FragAdapter adapter = new FragAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //监听页面是否滑动
                //MyLog.d(TAG, "onPageScrolled: "+position+" "+ positionOffset+" " +positionOffsetPixels, isOpen);
            }

            @Override
            public void onPageSelected(int position) {
                //监听是否跳转
                MyLog.d(TAG, "onPageSelected: ", isOpen);
                //设置上标
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //监听页面是否有滑动变化
                MyLog.d(TAG, "onPageScrollStateChanged: ", isOpen);
            }
        });
    }



    private Handler handler = new Handler(){
        public void handleMessage(Message message)
        {
            switch(message.what) {
                case GET_EQUI_SUCCEED:
//                    for(int i = 0; i<equiList.getResult().size();i++)
//                    {
//                        MyLog.d(TAG, "设备Id: "+equiList.getResult().get(i).getId(), isOpen);
//                    }

                    break;
                case GET_EQUI_FAILURE:
                    sendGetEquiRequest();
                    break;
                case EXIT_FAILURE:
                    MyLog.d(TAG, "截取到退出失败信息", isOpen);
                    isSendExit = false;
                    exit();
                    break;
                case EXIT_SUCCEED:
                    MyLog.d(TAG, "截取到退出成功信息", isOpen);
                    SharedPreferences.Editor editor = getSharedPreferences("YunKong",MODE_PRIVATE).edit();
                    editor.remove("token");
                    editor.apply();
                    Intent toLogin = new Intent(TabViewActivity.this,LoginActivity.class);
                    startActivity(toLogin);
                    finish();
                    break;
            }
        }

    };

    @Override
    public boolean onCreateOptionsMenuI(Menu menu) {
        return false;
    }

    @Override
    public void onTabClick(TabItem tabItem) {
        MyLog.d("YunKong", "onTabClick: "+tabItem, isOpen);
        mViewPager.setCurrentItem(tabs.indexOf(tabItem),false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.equi_info_text:
            case R.id.equi_info_down:
                MyLog.d(TAG, "点击设备信息", isOpen);
                equi_popup(equiList);

                if(!mPopupWindow.isShowing())
                {
                    int xOffset = equiTitle.getWidth()/6;
                    mPopupWindow.showAsDropDown(equiTitle, xOffset, 0);
                }else {
                    mPopupWindow.dismiss();
                }
                break;
            case R.id.menu:
                MyLog.d(TAG, "点击菜单",isOpen);
                //exit();
                menu_popup();
                if(!mPopupWindow.isShowing())
                {
                    mPopupWindow.showAsDropDown(menu, 0, 0);
                }
                break;
        }
    }


    public class FragAdapter extends FragmentPagerAdapter {

        public FragAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            try {
                return tabs.get(arg0).tagFragmentClz.newInstance();

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return tabs.size();
        }

    }
//    private void initRecyclerView()
//    {
//        MyLog.d(TAG, "initRecyclerView", isOpen);
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.attr_recycler);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);
//        RecyclerAdapter adapter = new RecyclerAdapter(attributesList);
//        recyclerView.setAdapter(adapter);
//    }
//    private void initAttributesList()
//    {
//        for(int i = 0; i<equiData.getResult().get(0).getAttributes().size(); i++)
//        {
//            Attributes attributes = new Attributes();
//            attributes.setId(equiData.getResult().get(0).getAttributes().get(i).getId());
//            attributes.setAgreementId(equiData.getResult().get(0).getAttributes().get(i).getAgreementId());
//            attributes.setAttributeInterval(equiData.getResult().get(0).getAttributes().get(i).getAttributeInterval());
//            attributes.setAttributeKey(equiData.getResult().get(0).getAttributes().get(i).getAttributeKey());
//            attributes.setAttributeName(equiData.getResult().get(0).getAttributes().get(i).getAttributeName());
//            attributes.setCreateDate(equiData.getResult().get(0).getAttributes().get(i).getCreateDate());
//            attributes.setUpdateDate(equiData.getResult().get(0).getAttributes().get(i).getUpdateDate());
//            attributesList.add(attributes);
//        }
//    }
    private void exit()
    {
        sendExitRequest();
    }

    private void sendExitRequest()
    {
        if(!isSendExit)
        {
            isSendExit = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Message message = new Message();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    RequestServe getDataServes = retrofit.create(RequestServe.class);
                    Call<ExitBean> call = getDataServes.exit(token);
                    call.enqueue(new Callback<ExitBean>() {
                        @Override
                        public void onResponse(Call<ExitBean> call, Response<ExitBean> response) {
                            MyLog.d(TAG, "退出登录请求数据返回成功", isOpen);
                            message.what = EXIT_SUCCEED;
                            handler.sendMessage(message);
                        }
                        @Override
                        public void onFailure(Call<ExitBean> call, Throwable t) {
                            MyLog.d(TAG, "退出登录请求数据返回成功:" + t.toString(), isOpen);
                            message.what = EXIT_FAILURE;
                            handler.sendMessage(message);
                        }
                    });
                }
            }).start();
        }
    }

    private void sendGetEquiRequest()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestServe getDataServes = retrofit.create(RequestServe.class);
        Call<EquiListBean> call = getDataServes.getEqui(token);
        call.enqueue(new Callback<EquiListBean>() {
            @Override
            public void onResponse(Call<EquiListBean> call, Response<EquiListBean> response) {
                equiList = new EquiListBean();
                //equiList.setCode(response.body().getCode());
                equiList.setResult(response.body().getResult());

                MyLog.d(TAG, "获取设备信息成功" + response.body().getCode(), isOpen);
                Message message = new Message();
                message.what = GET_EQUI_SUCCEED;
                handler.sendMessage(message);
            }
            @Override
            public void onFailure(Call<EquiListBean> call, Throwable t) {
                MyLog.d(TAG, "获取设备信息失败:" + t.toString(), isOpen);
                Message message = new Message();
                message.what = GET_EQUI_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    public void equi_popup(EquiListBean data) {
        mTypeLv = new ListView(this);
        // 设置适配器
        DataAdapter = new ArrayAdapter<String>(this, R.layout.equi_popup, DataConvert(data));
        mTypeLv.setAdapter(DataAdapter);

        // 设置ListView点击事件监听
        mTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 在这里获取item数据
                 MyLog.d(TAG, "点击列表" + equiList.getResult().get(position).getId() , isOpen);

                // 选择完后关闭popup窗口
                mPopupWindow.dismiss();

            }
        });
        WindowManager wm = this.getWindowManager();
        mPopupWindow = new PopupWindow(mTypeLv, wm.getDefaultDisplay().getWidth()*2/3,
                wm.getDefaultDisplay().getHeight()/3, true);
        // 取得popup窗口的背景图片
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.bg_equi);
        mPopupWindow.setBackgroundDrawable(drawable);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setOutsideTouchable(true);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }
    //把getData的转换成String
    public static List<String> DataConvert(EquiListBean data)
    {
        List<String> str = new ArrayList<>();
        for(int i = 0; i < data.getResult().size();i++)
        {
            str.add(data.getResult().get(i).getId());
        }

        return str;
    }
    private void getToken()
    {
        SharedPreferences pref = getSharedPreferences("YunKong", Context.MODE_PRIVATE);
        token = pref.getString("token","");
    }

    public void menu_popup() {
        mTypeLv = new ListView(this);
        // 设置适配器
        DataAdapter = new ArrayAdapter<String>(this, R.layout.equi_popup,menu_list());
        mTypeLv.setAdapter(DataAdapter);

        // 设置ListView点击事件监听
        mTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 在这里获取item数据
                MyLog.d(TAG, "点击列表:"+mTypeLv.getAdapter().getItem(position), isOpen);
                switch (position){
                    case 0:
                        exitDialog();
                        break;
                }
                // 选择完后关闭popup窗口
                mPopupWindow.dismiss();
            }
        });
        WindowManager wm = this.getWindowManager();
        mPopupWindow = new PopupWindow(mTypeLv, wm.getDefaultDisplay().getWidth()/3,
                wm.getDefaultDisplay().getHeight()/3, true);
        // 取得popup窗口的背景图片
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.bg_equi);
        mPopupWindow.setBackgroundDrawable(drawable);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setOutsideTouchable(true);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }
    private List<String> menu_list()
    {
        List<String> strL = new ArrayList<>();
        strL.add("退出");
        return strL;
    }

    private void exitDialog()
    {
        final AlertDialog.Builder exit = new AlertDialog.Builder(TabViewActivity.this);
        exit.setMessage("你是否要退出登录?");
        exit.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyLog.d(TAG, "确定退出", isOpen);
                exit();
            }
        });
        exit.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyLog.d(TAG, "取消退出", isOpen);
            }
        });
        exit.show();
    }

}
