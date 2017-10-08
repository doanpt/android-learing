package com.cnc.hcm.cnctracking.api;


import com.cnc.hcm.cnctracking.util.Conts;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofitToken = null;

    private static Retrofit retrofitLogin = null;

    public static Retrofit getClientToken(final String accessToken) {
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader(Conts.KEY_CONTENT_TYPE, Conts.VALUE_CONTENT_TYPE)
                        .addHeader(Conts.KEY_ACCESS_TOKEN, accessToken).build();
                return chain.proceed(request);
            }
        }).build();

        retrofitToken = new Retrofit.Builder()
                .baseUrl(Conts.BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofitToken;
    }


    public static Retrofit getClientLogin(final String userName, final String password) {
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader(Conts.KEY_AUTHORIZATION, Credentials.basic(userName, password))
                        .build();
                return chain.proceed(request);
            }
        }).build();

        retrofitLogin = new Retrofit.Builder()
                .baseUrl(Conts.BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofitLogin;
    }

}