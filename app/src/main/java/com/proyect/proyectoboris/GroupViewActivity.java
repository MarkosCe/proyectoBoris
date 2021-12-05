package com.proyect.proyectoboris;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class GroupViewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private GroupViewAdapter mAdapter;
    private AuthProvider mAuthProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);

        MyToolbar.show(this, "Grupos", true);
        mRecyclerView = findViewById(R.id.recyclerViewGroups);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuthProvider = new AuthProvider();

        Query query = FirebaseDatabase.getInstance().getReference()
                                        .child("Group")
                                        .orderByChild("idUser")
                                        .equalTo(mAuthProvider.getId());

        FirebaseRecyclerOptions<Group> options = new FirebaseRecyclerOptions.Builder<Group>()
                .setQuery(query, Group.class)
                .build();

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