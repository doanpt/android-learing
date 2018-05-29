package com.cnc.hcm.cnctrack.api;


import com.cnc.hcm.cnctrack.util.Conts;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofitLogin = null;

    public static Retrofit getClientToken(final List<MHead> arrHeads) {
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                for (int i = 0; i < arrHeads.size(); i++) {
                    builder.addHeader(arrHeads.get(i).getKey(), arrHeads.get(i).getValue());
                }
                Request request = builder.build();
                return chain.proceed(request);
            }
        }).build();

        Retrofit retrofitToken = new Retrofit.Builder()
                .baseUrl(Conts.BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofitToken;
    }

}