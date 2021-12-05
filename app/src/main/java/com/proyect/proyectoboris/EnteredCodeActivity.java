package com.proyect.proyectoboris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class EnteredCodeActivity extends AppCompatActivity {

    Button mUnirmeButton;
    private PinView mPinView;

    private AuthProvider mAuthProvider;

    private GroupProvider groupProvider;
    private Group group;

    private Boolean exist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entered_code);

        MyToolbar.show(this, "Unirse", true);

        //groupProvider = new GroupProvider();
        groupProvider = new GroupProvider();
        mAuthProvider = new AuthProvider();

        mUnirmeButton = findViewById(R.id.unirmeButton);

        mPinView = findViewById(R.id.pinView);

        mUnirmeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unirme();
            }
        });

    }

    public void unirme() {
        String codigo = mPinView.getText().toString();
        if(!codigo.isEmpty()) {
            groupProvider.getGroup().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Group groupS = dataSnapshot.getValue(Group.class);
                            if (codigo.equals(group.getCode())) {
                                exist = true;
                                group = groupS;
                                groupProvider.updateMembers(group, mAuthProvider.getId());
                                break;
                            }
                        }
                        if (!exist) {
                            Toast.makeText(EnteredCodeActivity.this, "No existe el grupo", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(EnteredCodeActivity.this, "No hay grupos", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}