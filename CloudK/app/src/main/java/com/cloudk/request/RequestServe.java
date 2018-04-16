package com.cloudk.request;


import com.cloudk.bean.CheckTokenBean;
import com.cloudk.bean.EquiListBean;
import com.cloudk.bean.ExitBean;
import com.cloudk.bean.RealTimeBean;
import com.cloudk.bean.SetNameBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by dong on 2018/1/28.
 */

public interface RequestServe {
    @GET("wlwSso/user/token/{token}")
    Call<CheckTokenBean> checkInfo(@Path("token") String token);

    @GET("wlwSso/user/logout/{token}")
    Call<ExitBean> exit(@Path("token") String token);

    @GET("wlwSso/user/check/{date}/{type}")
    Call<ResponseBody> isUsable(@Path("date") String Data,
                                @Path("type") String Type);
    @GET("WLW/WebServlet")
    Call<ResponseBody> getMachineInfo(@Query("type") String type);


    @GET("wlwBusiness/company/equipments")
    Call<EquiListBean> getEqui(@Header("USER_TOKEN") String token);

    @GET("wlwBusiness/company/equipment/{id}")
    Call<SetNameBean> setName(@Header("USER_TOKEN") String token,
                              @Path("id") String id,
                              @Query("name") String name);

//    @GET("wlwBusiness/company/equipment/{equipmentId}/attribute/{attributeId}/attributeValues")
//    Call<ResponseBody> getAttrs(@Header("USER_TOKEN") String token,
//                                @Path("equipmentId") String equipmentId,
//                                @Path("attributeId") String attributeId,
//                                @Query("start") String start,
//                                @Query("end") String end);
//
//    @GET("wlwBusiness/company/equipment/{equipmentId}/attribute/{attributeId}/attributeValues")
//    Call<ResponseBody> getAttrs(@Header("USER_TOKEN") String token,
//                                @Path("equipmentId") String equipmentId,
//                                @Path("attributeId") String attributeId);



    @GET("wlwBusiness/company/equipment/{equipmentId}/attributeValues/realTime")
    Call<RealTimeBean> getRealTime(@Header("USER_TOKEN") String token,
                                   @Path("equipmentId") String equipmenId);


    @POST("wlwSso/user/register")
    Call<ResponseBody> postSignin(@Query("userName") String userName,
                                  @Query("password") String password);

    @POST("wlwSso/user/login")
    Call<ResponseBody> postLogin(@Query("userName") String userName,
                                 @Query("password") String password);
}
