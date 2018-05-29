package com.cnc.hcm.cnctrack.api;

import java.util.List;

public class ApiUtils {

    private ApiUtils() {
    }

    public static APIService getAPIService(final List<MHead> arrHead) {
        return RetrofitClient.getClientToken(arrHead).create(APIService.class);
    }

}