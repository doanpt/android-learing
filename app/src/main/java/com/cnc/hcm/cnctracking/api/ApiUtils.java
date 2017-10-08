package com.cnc.hcm.cnctracking.api;

public class ApiUtils {

    private ApiUtils() {}

    public static APIService getAPIService(String accessToken) {
        return RetrofitClient.getClientToken(accessToken).create(APIService.class);
    }

    public static APIService getAPIService(String username, String password) {
        return RetrofitClient.getClientLogin(username, password).create(APIService.class);
    }

}