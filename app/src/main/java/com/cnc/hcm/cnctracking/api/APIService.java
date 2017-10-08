package com.cnc.hcm.cnctracking.api;


import com.cnc.hcm.cnctracking.model.GetUserProfileResponseStatus;
import com.cnc.hcm.cnctracking.model.ItemTrackLocation;
import com.cnc.hcm.cnctracking.model.LoginResponseStatus;
import com.cnc.hcm.cnctracking.model.UpdateLocationResponseStatus;
import com.cnc.hcm.cnctracking.util.Conts;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;

public interface APIService {

    @PATCH(Conts.PATH_TRACKING)
//    Call<UpdateLocationResponseStatus> updateLocation(/*@Header(Constants.KEY_CONTENT_TYPE) String contentType, @Header(Constants.KEY_ACCESS_TOKEN) String accessToken, */@Body ItemTrackLocation body);
    Call<UpdateLocationResponseStatus> updateLocation(@Body ItemTrackLocation body);

    @GET(Conts.PATH_USER_LOGIN)
//    Call<LoginResponseStatus> login(@Header(Constants.KEY_AUTHORIZATION) String valueAuthorization);
    Call<LoginResponseStatus> login();

    @GET(Conts.PATH_USER_PROFILE)
    Call<GetUserProfileResponseStatus> getUserProfile();
}