package com.proyect.proyectoboris.providers;

import com.proyect.proyectoboris.models.FCMBody;
import com.proyect.proyectoboris.models.FCMResponse;
import com.proyect.proyectoboris.retrofit.IFCMApi;
import com.proyect.proyectoboris.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {

    private String url = "https://fcm.googleapis.com";

    public NotificationProvider(){

    }

    public Call<FCMResponse> sendNotification(FCMBody body){
        return RetrofitClient.getClient(url).create(IFCMApi.class).send(body);
    }

}
