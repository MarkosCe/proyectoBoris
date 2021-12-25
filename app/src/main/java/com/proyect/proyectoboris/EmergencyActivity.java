package com.proyect.proyectoboris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EmergencyActivity extends AppCompatActivity {

    private Button mButtonEmergency;
    private PendingIntent pendingIntent;
    //esto es para versiones android superiores a O (Oreo)
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        MyToolbar.show(this, "Botón de Pánico", true);

        mButtonEmergency = findViewById(R.id.emergency_button);
        /**
        FirebaseMessaging.getInstance().subscribeToTopic("enviaratodos").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EmergencyActivity.this, "suscrito al topico", Toast.LENGTH_SHORT).show();
            }
        });
         */

        Bundle extras = this.getIntent().getExtras();
        String idgrupo = extras.getString("idGrupo");

        mButtonEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificarTodos();
                Toast.makeText(EmergencyActivity.this, "Alerta enviada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void notificarTodos() {
        RequestQueue myRequest = Volley.newRequestQueue(getApplicationContext());
        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/enviaratodos");
            JSONObject notificacion = new JSONObject();
            notificacion.put("titulo", "Alerta Boris");
            notificacion.put("detalle", "Fulano necesita ayuda");
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