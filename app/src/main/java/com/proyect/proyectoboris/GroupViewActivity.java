package com.proyect.proyectoboris;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class GroupViewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private GroupViewAdapter mAdapter;
    private AuthProvider mAuthProvider;

    private int flag = 0;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);

        MyToolbar.show(this, "Grupos", true);
        mRecyclerView = findViewById(R.id.recyclerViewGroups);

        Bundle extras = this.getIntent().getExtras();
        if(extras != null){
            flag = extras.getInt("flag");
            code = extras.getString("codigo");
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuthProvider = new AuthProvider();

        Query query = null;

        if(flag == 0){
            query = FirebaseDatabase.getInstance().getReference()
                    .child("Group")
                    .orderByChild("idUser")
                    .equalTo(mAuthProvider.getId());
        }else if(flag == 1){
            query = FirebaseDatabase.getInstance().getReference()
                    .child("Group")
                    .orderByChild("code")
                    .equalTo(code);
        }

        //Query query1 = FirebaseDatabase.getInstance().getReference()

        /*Query query = FirebaseDatabase.getInstance().getReference()
                                        .child("Group")
                                        .orderByChild("members")
                                        .equalTo("true", mAuthProvider.getId());*/

        FirebaseRecyclerOptions<Group> options = new FirebaseRecyclerOptions.Builder<Group>()
                .setQuery(query, Group.class)
                .build();

        /*FirebaseRecyclerOptions<Group> options1 = new FirebaseRecyclerOptions.Builder<Group>()
                .setQuery()*/

        mAdapter = new GroupViewAdapter(options);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}