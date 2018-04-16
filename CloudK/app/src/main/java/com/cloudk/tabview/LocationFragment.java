package com.cloudk.tabview;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.cloudk.R;
import com.cloudk.algo.Search;
import com.cloudk.algo.Str;
import com.cloudk.request.MapData;
import com.cloudk.request.RequestServe;
import com.cloudk.tooks.MyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 世东 on 18/2/3.
 */
public class LocationFragment extends BaseFragment implements View.OnClickListener,AMap.OnInfoWindowClickListener {
    private static final String TAG = "YunKong";
    private static final String URL = "http://119.29.144.44:8888/";
    private boolean isOpen = true;
    private boolean isCanSend = true;
    private boolean isCanEnd = false;
    private MapView mMapView = null;
    private AMap aMap = null;
    private Marker seekOutMarker = null;
    private Marker clickMarker = null;
    private boolean isFisrtAssign = true;
    private Call<ResponseBody> call;
    private List<MapData> getData = null;
    private EditText map_edit;
    private Button btn_fab;
    private PopupWindow mPopupWindow;
    private ListView mTypeLv;
    private ArrayAdapter<String> DataAdapter;
    private EditText search_edit;

    @Override
    public void fetchData() {
        //点开页面执行
        sendRequestandGetData();
        Listener();
    }



    private void initView(View view)
    {
        map_edit = (EditText) view.findViewById(R.id.map_edit);
        btn_fab = (Button) view.findViewById(R.id.btn_list);
        search_edit = view.findViewById(R.id.map_edit);
    }

    public void Listener()
    {
        btn_fab.setOnClickListener(this);
        // 定义 Marker 点击事件监听
        AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
            // marker 对象被点击时回调的接口
            // 返回 true 则表示接口已响应事件，否则返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                MyLog.d(TAG, "点击Marker", isOpen);
                clickMarker = marker;
                return false;
            }
        };
        //InfoWindow监听
        AMap.OnInfoWindowClickListener infoWindowClickListener = new AMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                MyLog.d(TAG, "onInfoWindowClick: " + arg0.getTitle(),isOpen);

            }
        };
        AMap.OnMapTouchListener mapTouchListener = new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                MyLog.d(TAG, "点击地图", isOpen);
                if(seekOutMarker!=null && seekOutMarker.isInfoWindowShown())
                {
                    seekOutMarker.hideInfoWindow();
                }else if(clickMarker!=null && clickMarker.isInfoWindowShown())
                {
                    clickMarker.hideInfoWindow();
                }
            }
        };
        //绑定Map被点击事件
        aMap.setOnMapTouchListener(mapTouchListener);
        //绑定Marker被点击事件
        aMap.setOnMarkerClickListener(markerClickListener);
        //绑定InfoWindow点击事件
        aMap.setOnInfoWindowClickListener(infoWindowClickListener);
        map_edit.addTextChangedListener(textWatcher);
    }
    //初始化地图
    private void initMap(View view,Bundle savedInstanceState)
    {
        //定义了一个地图view
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
//      初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
    }

    private void sendRequestandGetData()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                    if(isCanEnd) {
                        isCanEnd = false;
                        break;
                    }
                    if(getData == null && isCanSend){
                        isCanSend = false;
                        MyLog.d(TAG, "发送获取点经纬度坐标请求", isOpen);
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        RequestServe getDataServes = retrofit.create(RequestServe.class);
                        call = getDataServes.getMachineInfo("5");
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    String s = response.body().string();
                                    Gson gson = new Gson();
                                    getData = gson.fromJson(s, new TypeToken<List<MapData>>() {}.getType());
                                    MyLog.d(TAG, "onResponse: ", isOpen);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
                    //判断是否请求到数据 && 是否是第一次赋值
                    if(getData != null && isFisrtAssign)
                    {
                        MyLog.d(TAG, "handleMessage: ", isOpen);
                        isFisrtAssign = false;
                        isCanEnd = true;
                        //myLocation();
                        markPoint();
                    }else{
                        isCanSend = true;
                    }
                    break;
            }
        }

    };
    //绘制点标记
    public void markPoint()
    {
        ArrayList<MarkerOptions> markerList = new ArrayList<>();
        for(MapData data:getData) {
            //绘制的经纬度
            LatLng latLng = new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude()));
            MarkerOptions markerOptions = new MarkerOptions();
            //Marker信息
            markerOptions.position(latLng).title(data.getId())
                    .snippet("机名:" + data.getProto() + "    "
                            + "类型:" + data.getType() + "\n"
                            + "版本:" + data.getVersion() + "    "
                            + "企业号:" + data.getOrg());
            markerList.add(markerOptions);
        }
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f));
        aMap.addMarkers(markerList,true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //创建页面视图
        View view = inflater.inflate(R.layout.layout_loaction, container, false);
        initMap(view,savedInstanceState);
        initView(view);
        return view;
    }


    @Override
    public BaseFragment getFragment() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_list:
                MyLog.d(TAG,"点击小按钮",isOpen);
                //点击控件后显示popup窗口
                WindowManager wm = getActivity().getWindowManager();
                initPopup(getData,wm.getDefaultDisplay().getWidth()*2/3, wm.getDefaultDisplay().getHeight()/3);
                // 使用isShowing()检查popup窗口是否在显示状态
                if (mPopupWindow != null && !mPopupWindow.isShowing()) {
                    int xOffset = - mPopupWindow.getWidth();
                    int yOffset = mPopupWindow.getHeight() + btn_fab.getHeight();
                    mPopupWindow.showAsDropDown(btn_fab, xOffset, -yOffset);
                }
                break;
        }
    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            disposeSearch();
        }
    };

    //处理搜索
    public void disposeSearch()
    {
        if("".equals(map_edit.getText().toString()) && map_edit.getText().toString()!=null){
            if(mPopupWindow != null && mPopupWindow.isShowing())
            {
                mPopupWindow.dismiss();
            }
        }
        if(!"".equals(map_edit.getText().toString())
                && map_edit.getText().toString()!=null
                && !Str.isEmpty(map_edit.getText().toString()))
        {
            List<MapData> searchResult = new ArrayList<>();
            if(getData != null){
                String textStr = Str.delBeginSpace(map_edit.getText().toString());
                for(int j = 0; j < getData.size(); j++)
                {
                    //判断输入的字符串是否与得到的数据有相应的结果
                    if(Search.isEquality(textStr,getData.get(j).getId())
                            || Search.isEquality(textStr,getData.get(j).getProto())
                            || Search.isEquality(textStr,getData.get(j).getType())
                            || Search.isEquality(textStr,getData.get(j).getVersion())
                            || Search.isEquality(textStr,getData.get(j).getOrg())
                            ){
                        //如果有，就加入搜索结果列表
                        searchResult.add(getData.get(j));
                    }
                }
            }
            if(searchResult.size()==0)
            {
                MapData nothings = new MapData("没有找到");
                searchResult.add(nothings);
            }
            if(mPopupWindow != null && mPopupWindow.isShowing())
            {
                mPopupWindow.dismiss();
            }
            WindowManager wm = getActivity().getWindowManager();
            initPopup(searchResult,wm.getDefaultDisplay().getWidth()*2/3, wm.getDefaultDisplay().getHeight()/3);
            if (mPopupWindow != null && !mPopupWindow.isShowing()) {
                int xOffset = wm.getDefaultDisplay().getWidth()/3 - mPopupWindow.getWidth()/4;
                mPopupWindow.showAsDropDown(search_edit, xOffset, 5);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MyLog.d(TAG, "onSaveInstanceState: ", isOpen);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }



    //地图中的InfoWindow监听
    @Override
    public void onInfoWindowClick(Marker marker) {

    }


    public void toMarker(List<MapData> dataList,int i)
    {
        aMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(Double.parseDouble(dataList.get(i).getLatitude()),
                        Double.parseDouble(dataList.get(i).getLongitude())),17));
        //停一下，不然有时中心没有跳到marker上就执行下一句了
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Marker> markerList = aMap.getMapScreenMarkers();
        for(Marker marker:markerList)
        {
            if(marker.getTitle().equals(dataList.get(i).getId()))
            {
                MyLog.d(TAG, "有Marker:" + dataList.get(i).getId(), isOpen);
                seekOutMarker = marker;
                seekOutMarker.setInfoWindowEnable(true);
                seekOutMarker.showInfoWindow();
                seekOutMarker.setToTop();
            }
        }
    }

    private void initPopup(final List<MapData> dataList, int x, int y) {
        mTypeLv = new ListView(getActivity());
        // 设置适配器
        DataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.popup_text_item, DataConvert(dataList));
        mTypeLv.setAdapter(DataAdapter);

        // 设置ListView点击事件监听
        mTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(dataList.get(position).getNothings() == null)
                {
                    // 在这里获取item数据
                    MyLog.d(TAG, "点击列表得到的ID:" + dataList.get(position).getId(), isOpen);
                    //跳到指定点
                    toMarker(dataList,position);
                    // 选择完后关闭popup窗口
                    mPopupWindow.dismiss();
                }
            }
        });

        mPopupWindow = new PopupWindow(mTypeLv, x, y, true);
        // 取得popup窗口的背景图片
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.bg_filter_down);
        mPopupWindow.setBackgroundDrawable(drawable);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 关闭popup窗口
                mPopupWindow.dismiss();
            }
        });
    }

    //把getData的转换成String
    public List<String> DataConvert(List<MapData> dataList)
    {
        List<String> str = new ArrayList<>();

        for(int i = 0; i < dataList.size(); i++)
        {
            if(dataList.get(i).getNothings()!=null)
            {
                str.add("\n\n"+dataList.get(i).getNothings()+"\n\n");
            }else {
                str.add("ID:" + dataList.get(i).getId() + "\n"
                        + "机名:" + dataList.get(i).getProto() + "  " + "类型:" + dataList.get(i).getType() + "\n"
                        + "版本:" + dataList.get(i).getVersion() + "  " + "企业号:" + dataList.get(i).getOrg());
            }

        }
        return str;
    }

}
