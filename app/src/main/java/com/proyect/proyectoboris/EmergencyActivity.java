package com.proyect.proyectoboris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.proyect.proyectoboris.models.FCMBody;
import com.proyect.proyectoboris.models.FCMResponse;
import com.proyect.proyectoboris.providers.NotificationProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmergencyActivity extends AppCompatActivity {

    private NotificationProvider mNotificationProvider;
    private GroupProvider groupPro;
    private UserProvider userProvider;
    private ArrayList<String> membersIds;
    ArrayList<String> mReceiversIds = new ArrayList<>();
    ArrayList<User> mReceivers = new ArrayList<>();

    private Button mButtonEmergency;
    private PendingIntent pendingIntent;
    //esto es para versiones android superiores a O (Oreo)
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;
    private String idgrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        MyToolbar.show(this, "Botón de emergencia", true);

        mNotificationProvider = new NotificationProvider();
        groupPro = new GroupProvider();
        userProvider = new UserProvider();

        mButtonEmergency = findViewById(R.id.emergency_button);

        idgrupo = getIntent().getStringExtra("idGrupo");
        getReceiversId();
        getReceiversInfo();

        /**
        FirebaseMessaging.getInstance().subscribeToTopic("enviaratodos").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EmergencyActivity.this, "suscrito al topico", Toast.LENGTH_SHORT).show();
            }
        });


        Bundle extras = this.getIntent().getExtras();
        idgrupo = extras.getString("idGrupo");
         */

        mButtonEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendNotification("Fulanito ha enviado una notificacion");
                //notificarTodos();
                //Toast.makeText(EmergencyActivity.this, "Alerta enviada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getReceiversId() {
        groupPro.getGroupId(idgrupo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Group group = snapshot.getValue(Group.class);
                    membersIds = new ArrayList<>(group.getMembers().keySet());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getReceiversInfo() {

        for (String id: membersIds){
            userProvider.getUser(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        mReceivers.add(user);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void sendNotification(String message) {

        Map<String,String> data = new HashMap<>();
        data.put("title","NUEVA NOTIFICACION");
        data.put("body", message);

        List<String> tokens = new ArrayList<>();
        for (User u:mReceivers){
            tokens.add(u.getToken());
        }

        FCMBody body = new FCMBody(tokens,"high", "4500s",data);
        mNotificationProvider.sendNotification(body).enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                
                if (response.body() != null) {
                    if (response.body().getSuccess() == 1) {
                        Toast.makeText(EmergencyActivity.this, "Se ha enviado una notificacipon a todos los integrantes del grupo", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(EmergencyActivity.this, "Falló el envío de la notificación", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(EmergencyActivity.this, "No hubo respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FCMResponse> call, Throwable t) {
                Toast.makeText(EmergencyActivity.this, "Falló la petición con retrofit:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void notificarTodos() {
        RequestQueue myRequest = Volley.newRequestQueue(getApplicationContext());
        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/"+idgrupo);
            JSONObject notificacion = new JSONObject();
            notificacion.put("titulo", "Alerta Boris");
            notificacion.put("detalle", "Alguien necesita de tu ayuda");
            json.put("data", notificacion);
            String URL = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, json, null, null) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    //aqui ecesitamos el key de la configuraciones de firebase de Cloud Messaging
                    header.put("authorization", "key=AAAAaEnac1g:APA91bF-yxW416bpcdBk5dRgKcIkG-Hg6WllO11IGmEe26ZAD6r3mKuKfM01tOqEpQjprlijRCd2wYC8TWpBZ8TggO7SJOSiUzJOlQ34aONTvO9L3cV_TgKm2YOOhXvTftHX_r2tEmdh");
                    return header;
                }
            };
            myRequest.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}