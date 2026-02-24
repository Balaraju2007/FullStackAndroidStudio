package com.example.android;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/register")
    Call<ResponseBody> registerUser(@Body RegisterRequest request);

    @POST("api/login")
    Call<ResponseBody> loginUser(@Body LoginRequest request);
}