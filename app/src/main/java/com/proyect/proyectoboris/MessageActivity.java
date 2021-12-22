package com.proyect.proyectoboris;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MessageActivity extends AppCompatActivity {

    Button mButtonDefaultMessage;
    //TextView mTextViewMessage;

    //EditText mgetmessage;
    Button mgetmessage;
    ImageButton msendmessagebutton;

    //CardView msendmessagecardview;
    //androidx.appcompat.widget.Toolbar mtoolbarofspecificchat;
    //ImageView mimageviewofspecificuser;
    //TextView mnameofspecificuser;

    private String enteredmessage;
    //Intent intent;
    String mrecievername,sendername,mrecieveruid,msenderuid;
    //private FirebaseAuth firebaseAuth;
    private AuthProvider authProvider;
    //FirebaseDatabase firebaseDatabase;
    String senderroom,recieverroom;

    //ImageButton mbackbuttonofspecificchat;

    RecyclerView mmessagerecyclerview;

    String currenttime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    MessagesAdapter messagesAdapter;
    ArrayList<Messages> messagesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //mButtonDefaultMessage = findViewById(R.id.getmessage);
        msendmessagebutton = findViewById(R.id.sendMessageButton);
        mgetmessage = findViewById(R.id.getmessage);
        //mTextViewMessage = findViewById(R.id.textViewMessage);
        //MyToolbar.show(this, "Chat", false);
        //firebaseAuth=FirebaseAuth.getInstance();
        authProvider = new AuthProvider();
        msenderuid = authProvider.getId();
        calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("hh:mm a");

        messagesArrayList=new ArrayList<>();
        mmessagerecyclerview=findViewById(R.id.recyclerviewofspecific);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmessagerecyclerview.setLayoutManager(linearLayoutManager);
        messagesAdapter=new MessagesAdapter(MessageActivity.this,messagesArrayList);
        mmessagerecyclerview.setAdapter(messagesAdapter);


        mgetmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inicializar el PopupMenu
                //Toast.makeText(MessageActivity.this, "siiiii", Toast.LENGTH_SHORT).show();
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                //inflate el popupMenu
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        mButtonDefaultMessage.setText(item.getTitle().toString());
                        //Toast.makeText(MessageActivity.this, "Mensaje: "+item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                popupMenu.show();

                /*LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.popup_window,null);

                final PopupWindow popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                popupWindow.showAtLocation();*/

            }
        });

        msendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredmessage=mgetmessage.getText().toString();
                if(enteredmessage.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Selecciona un mensaje",Toast.LENGTH_SHORT).show();
                }
                else {
                    Date date = new Date();
                    currenttime = simpleDateFormat.format(calendar.getTime());
                    Messages messages = new Messages(enteredmessage, msenderuid, date.getTime(), currenttime);

                    mgetmessage.setText(null);
                }
            }
        });

    }
}