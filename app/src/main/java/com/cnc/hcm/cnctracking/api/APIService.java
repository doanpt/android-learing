package com.cnc.hcm.cnctracking.api;


import com.cnc.hcm.cnctracking.model.GetTaskDetailResult;
import com.cnc.hcm.cnctracking.model.GetTaskListResult;
import com.cnc.hcm.cnctracking.model.GetUserProfileResponseStatus;
import com.cnc.hcm.cnctracking.model.ItemTrackLocation;
import com.cnc.hcm.cnctracking.model.LoginResponseStatus;
import com.cnc.hcm.cnctracking.model.UpdateLocationResponseStatus;
import com.cnc.hcm.cnctracking.util.Conts;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface APIService {

    @PATCH(Conts.PATH_TRACKING)
    Call<UpdateLocationResponseStatus> updateLocation(@Body ItemTrackLocation body);

    @GET(Conts.PATH_USER_LOGIN)
    Call<LoginResponseStatus> login();

    @GET(Conts.PATH_USER_PROFILE)
    Call<GetUserProfileResponseStatus> getUserProfile();

    @GET(Conts.PATH_TASK_LIST)
    Call<GetTaskListResult> getTaskList();

    @GET(Conts.PATH_TASK_DETAIL)
    Call<GetTaskDetailResult> getTaskDetails(@Path("id") String taskId);

}