package com.arcsoft.arcfacedemo.service;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RequestService {
    String URL = "https://synclassapi.scxcj.com.cn/syn/face/";

    @GET("register")
    Observable<JsonObject> getUserInfo(@Query("face_id") String face_id,
                                       @Query("user_name") String user_name,
                                       @Query("gender") String gender,
                                       @Query("organization") String organization,
                                       @Query("face_imgs") String face_imgs,
                                       @Query("face_aspect") String face_aspect);

    @POST("file/upload")
    Observable<ResponseBody> multiUpload(@Body MultipartBody multipartBody);

    @Multipart
    @POST("file/upload")
    Observable<String> singleUpload(@Part MultipartBody.Part part);
}
