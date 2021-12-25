package com.proyect.proyectoboris;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * esta clase es para recibir los mensajes de firebase y mostrarlos como notificaciones
 * en el dispositivo
 */
public class FCM extends FirebaseMessagingService {

    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;
    private AuthProvider ap;
    private String id;

    /**
     * el token se registrará cada que se instale la aplicación en el dispositovi
     * @param s es el token
     */
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        ap = new AuthProvider();
        id = ap.getId();
        guardarToken(s);
    }

    /**
     * guardamos el token en la base de datos de firebase
     * @param s
     */
    private void guardarToken(String s) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("token");
        ref.child(id).setValue(s);
    }

    /**
     * este método se ejecutará cada que se reciba un mensaje de los servidores de firebase
     * @param remoteMessage
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            String titulo = remoteMessage.getData().get("titulo");
            String detalle = remoteMessage.getData().get("detalle");
            //verificamos la versión del android de nuestro proyecto
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                createNotificationChannel(titulo, detalle);
            else
                createNotification(titulo, detalle);
        }
    }

    /**
     * registramos el canal de notificaciones, esto es solo para las verisones superiores o iguales a Oreo
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String t, String d) {
        CharSequence name = "Notificacion";
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        //creamos la notificación
        createNotification(t,d);
    }

    /**
     * método para crear la notificación, esto es para cualquier versión de android
     */
    private void createNotification(String t, String d) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alerta)
                .setContentTitle(t)
                .setContentText(d)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ERROR);

        //esto es para que se muestre la notificación
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }
}
