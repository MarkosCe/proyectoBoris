package com.proyect.proyectoboris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class MessageActivity extends AppCompatActivity {

    String mExtraIdUser;
    String mExtraidChat;

    UserProvider mUsersProvider;
    AuthProvider mAuthProvider;
    GroupProvider mGroupProvider;
    //ChatProvider mChatProvider;
    MessageProvider mMessagesProvider;
    //FilesProvider mFilesProvider;
    //NotificationProvider mNotificationProvider;

    //ImageView mImageViewBack;
    //TextView mTextViewUsername;
    //TextView mTextViewOnline;
    //CircleImageView mCircleImageUser;
    //EditText mEditTextMessage;
    ImageButton mImageViewSend;
    //ImageView mImageViewSelectFile;

    //ImageView mImageViewSelectPictures;

    Button mGetMessage;
    //ImageButton msendmessagebutton;
    private String enteredmessage;

    MessageAdapter mAdapter;
    RecyclerView mRecyclerViewMessages;
    LinearLayoutManager mLinearLayoutManager;

    Timer mTimer;

    //ListenerRegistration mListenerChat;

    User mUserReceiver;
    User mMyUser;

    //BitmapFactory.Options mOptions;
    ArrayList<String> mReturnValues = new ArrayList<>();
    ArrayList<String> mReceiversId = new ArrayList<>();

    final int ACTION_FILE = 2;
    ArrayList<Uri> mFileList;

    //Chat mChat;
    Group mGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //mExtraIdUser = getIntent().getStringExtra("idUser");
        mExtraidChat = getIntent().getStringExtra("idChat");

        //Log.d("EXTRA", "ID CHAT: " + mExtraidChat);
        //Log.d("EXTRA", "ID USER: " + mExtraIdUser);

        mUsersProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
        mGroupProvider = new GroupProvider();
        //mChatProvider = new ChatProvider();
        mMessagesProvider = new MessageProvider();
        //mFilesProvider = new FilesProvider();
        //mNotificationProvider = new NotificationProvider();

        //mEditTextMessage = findViewById(R.id.editTextMessage);
        mImageViewSend = findViewById(R.id.sendMessageButton);
        mGetMessage = findViewById(R.id.getmessage);
        /*mImageViewSelectPictures = findViewById(R.id.imageViewSelectPictures);
        mImageViewSelectFile = findViewById(R.id.imageViewSelectFiles);*/
        mRecyclerViewMessages = findViewById(R.id.recyclerviewofspecific);

        mLinearLayoutManager = new LinearLayoutManager(MessageActivity.this);
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerViewMessages.setLayoutManager(mLinearLayoutManager);

        /*mOptions = Options.init()
                .setRequestCode(100)
                .setCount(5)
                .setFrontfacing(false)
                .setPreSelectedUrls(mReturnValues)
                .setExcludeVideos(true)
                .setVideoDurationLimitinSeconds(0)
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)
                .setPath("/pix/images");*/

        //showChatToolbar(R.layout.chat_toolbar);
        getUserReceiverInfo();
        getMyUserInfo();

        checkIfExistChat();
        //setWriting();

        mGetMessage.setOnClickListener(new View.OnClickListener() {
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
                        mGetMessage.setText(item.getTitle().toString());
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

        mImageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMessage();
            }
        });

        /*mImageViewSelectPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPix();
            }
        });

        mImageViewSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFiles();
            }
        });*/

    }

    private void selectFiles() {
        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent,"ChooseFile"), ACTION_FILE);
    }


    /*private void setWriting() {
        mEditTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            // ESTA ESCRIBIENDO
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mTimer != null) {
                    if (mExtraidChat != null) {
                        mChatsProvider.updateWriting(mExtraidChat, mAuthProvider.getId());
                        mTimer.cancel();
                    }
                }
            }

            // SI EL USUARIO DEJO DE ESCRIBIR
            @Override
            public void afterTextChanged(Editable editable) {
                mTimer = new Timer();
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (mExtraidChat != null) {
                            mChatsProvider.updateWriting(mExtraidChat, "");
                        }
                    }
                }, 2000);
            }
        });
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        //AppBackgroundHelper.online(ChatActivity.this, true);
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
        //AppBackgroundHelper.online(ChatActivity.this, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (mListenerChat != null) {
            mListenerChat.remove();
        }*/
    }

    /*private void startPix() {
        Pix.start(ChatActivity.this, mOptions);
    }*/



    private void createMessage() {

        String textMessage = mGetMessage.getText().toString();
        if (!textMessage.isEmpty()) {
            Message message = new Message();
            message.setId(mMessagesProvider.getIdMessage());
            message.setIdChat(mExtraidChat);
            message.setIdSender(mAuthProvider.getId());
            //message.setIdReceiver(mExtraIdUser);
            message.setMessage(textMessage);
            message.setStatus("ENVIADO");
            message.setType("texto");
            message.setTimestamp(new Date().getTime());

            mMessagesProvider.create(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mGetMessage.setText(null);
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    mGroupProvider.updateNumberMessages(mExtraidChat);
                    getLastMessages(message);
                    //Toast.makeText(ChatActivity.this, "El mensaje se creo correctamente", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this, "Ingresa el mensaje", Toast.LENGTH_SHORT).show();
        }

    }

    private void getLastMessages(final Message message) {
        /*mMessagesProvider.getLastMessagesByChatAndSender(mExtraidChat, mAuthProvider.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                if (querySnapshot != null) {
                    ArrayList<Message> messages = new ArrayList<>();

                    for(DocumentSnapshot document: querySnapshot.getDocuments()) {
                        Message m = document.toObject(Message.class);
                        messages.add(m);
                    }

                    if (messages.size() == 0) {
                        messages.add(message);
                    }
                    Collections.reverse(messages);
                    sendNotification(messages);
                }
            }
        });*/
    }

    private void sendNotification(ArrayList<Message> messages) {
        /*Map<String, String> data = new HashMap<>();
        data.put("title", "MENSAJE");
        data.put("body", "texto mensaje");
        data.put("idNotification", String.valueOf(mChat.getIdNotification()));
        data.put("usernameReceiver", "");
        data.put("usernameSender", mMyUser.getName());
        data.put("imageReceiver", "");
        data.put("imageSender", mMyUser.getImage());
        data.put("idChat", mExtraidChat);
        data.put("idSender", mAuthProvider.getId());
        data.put("idReceiver", "");
        data.put("tokenSender", mMyUser.getToken());
        data.put("tokenReceiver", "");

        Gson gson = new Gson();
        String messagesJSON = gson.toJson(messages);
        data.put("messagesJSON", messagesJSON);
        mNotificationProvider.send(ChatActivity.this, mUserReceiver.getToken(), data);*/
    }

    private void checkIfExistChat() {
        getMessagesByChat();
       /*mChatProvider.getChatByUser1AndUser2(mExtraIdUser, mAuthProvider.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    if (queryDocumentSnapshots.size() == 0) {
                        createChat();
                    }
                    else {
                        mExtraidChat = queryDocumentSnapshots.getDocuments().get(0).getId();
                        getMessagesByChat();
                        updateStatus();
                        //Toast.makeText(ChatActivity.this, "El chat entre dos usuarios ya existe", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });*/
    }

    private void updateStatus() {
        /*mMessagesProvider.getMessagesNotRead(mExtraidChat).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot document: queryDocumentSnapshots.getDocuments()) {
                    Message message = document.toObject(Message.class);
                    if (!message.getIdSender().equals(mAuthProvider.getId())) {
                        mMessagesProvider.updateStatus(message.getId(), "VISTO");
                    }
                }
            }
        });*/
    }

    private void getMessagesByChat() {
        Query query = mMessagesProvider.getMessagesByChat(mExtraidChat);

        FirebaseRecyclerOptions<Message> options = new FirebaseRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();

        mAdapter = new MessageAdapter(options, MessageActivity.this);
        mRecyclerViewMessages.setAdapter(mAdapter);
        mAdapter.startListening();

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                updateStatus();
                int numberMessage = mAdapter.getItemCount();
                int lastMessagePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastMessagePosition == -1 || (positionStart >= (numberMessage - 1) && lastMessagePosition == (positionStart -1))) {
                    mRecyclerViewMessages.scrollToPosition(positionStart);
                }
            }
        });
    }

    private void createChat() {
        /*Random random = new Random();
        int n = random.nextInt(100000);

        mChat = new Chat();
        mChat.setId(mAuthProvider.getId() + mExtraIdUser);
        mChat.setTimestamp(new Date().getTime());
        mChat.setNumberMessages(0);
        mChat.setWriting("");
        mChat.setIdNotification(n);

        ArrayList<String> ids = new ArrayList<>();
        ids.add(mAuthProvider.getId());
        ids.add(mExtraIdUser);

        mChat.setIds(ids);

        mExtraidChat = mChat.getId();

        mChatsProvider.create(mChat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                getMessagesByChat();
                //Toast.makeText(ChatActivity.this, "El chat se creo correctamente", Toast.LENGTH_SHORT).show();
            }
        });
         */
    }

    private void getMyUserInfo() {
        mUsersProvider.getUser(mAuthProvider.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    mMyUser = snapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserReceiverInfo() {

        /*mUsersProvider.getUserInfo(mExtraIdUser).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null) {
                    if (documentSnapshot.exists()) {
                        mUserReceiver = documentSnapshot.toObject(User.class);
                        mTextViewUsername.setText(mUserReceiver.getUsername());
                        if (mUserReceiver.getImage() != null) {
                            if (!mUserReceiver.getImage().equals("")) {
                                Picasso.with(ChatActivity.this).load(mUserReceiver.getImage()).into(mCircleImageUser);
                            }
                        }

                        if (mUserReceiver.isOnline()) {
                            mTextViewOnline.setText("En linea");
                        }
                        else {
                            String relativeTime = RelativeTime.getTimeAgo(mUserReceiver.getLastConnect(), ChatActivity.this);
                            mTextViewOnline.setText(relativeTime);
                        }
                    }
                }
            }
        });*/

    }

    private void showChatToolbar (int resource) {
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, null);
        actionBar.setCustomView(view);

        mImageViewBack = view.findViewById(R.id.imageViewBack);
        mTextViewUsername = view.findViewById(R.id.textViewUsername);
        mCircleImageUser = view.findViewById(R.id.circleImageUser);
        mTextViewOnline = view.findViewById(R.id.textViewOnline);

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            mReturnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Intent intent = new Intent(ChatActivity.this, ConfirmImageSendActivity.class);
            intent.putExtra("data", mReturnValues);
            intent.putExtra("idChat", mExtraidChat);
            intent.putExtra("idReceiver", mExtraIdUser);

            Gson gson = new Gson();
            String myUserJSON = gson.toJson(mMyUser);
            String receiverUserJSON = gson.toJson(mUserReceiver);

            intent.putExtra("myUser", myUserJSON);
            intent.putExtra("receiverUser", receiverUserJSON);
            intent.putExtra("idNotification", String.valueOf(mChat.getIdNotification()));
            startActivity(intent);
        }

        if (requestCode == ACTION_FILE && resultCode == RESULT_OK) {
            mFileList = new ArrayList<>();

            ClipData clipData = data.getClipData();

            // SELECCIONO UN SOLO ARCHIVO
            if (clipData == null) {
                Uri uri = data.getData();
                mFileList.add(uri);
            }
            // SELECCIONO VARIOS ARCHIVOS
            else {
                int count = clipData.getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri uri = clipData.getItemAt(i).getUri();
                    mFileList.add(uri);
                }
            }

            mFilesProvider.saveFiles(ChatActivity.this, mFileList, mExtraidChat, mExtraIdUser);

            final Message message = new Message();
            message.setIdChat(mExtraidChat);
            message.setIdSender(mAuthProvider.getId());
            message.setIdReceiver(mExtraIdUser);
            message.setMessage("\uD83D\uDCC4 Documento");
            message.setStatus("ENVIADO");
            message.setType("texto");
            message.setTimestamp(new Date().getTime());
            ArrayList<Message> messages = new ArrayList<>();
            messages.add(message);
            sendNotification(messages);
        }*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        /*switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(ChatActivity.this, mOptions);
                } else {
                    Toast.makeText(ChatActivity.this, "Por favor concede los permisos para acceder a la camara", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }*/
    }

}