package com.proyect.proyectoboris.retrofit;

import com.proyect.proyectoboris.models.FCMBody;
import com.proyect.proyectoboris.models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAaEnac1g:APA91bF-yxW416bpcdBk5dRgKcIkG-Hg6WllO11IGmEe26ZAD6r3mKuKfM01tOqEpQjprlijRCd2wYC8TWpBZ8TggO7SJOSiUzJOlQ34aONTvO9L3cV_TgKm2YOOhXvTftHX_r2tEmdh"
    })
    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);

}
