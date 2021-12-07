package com.proyect.proyectoboris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EnteredCodeActivity extends AppCompatActivity {

    Button mUnirmeButton;
    private PinView mPinView;

    private AuthProvider mAuthProvider;

    private GroupProvider groupProvider;
    private Group group;

    private Boolean exist = false;
    private String codigo = "";
    private String codigoEncontrado = "";

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
        codigo = mPinView.getText().toString();
        if(!codigo.isEmpty()) {
            groupProvider.getGroup().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Group groupS = dataSnapshot.getValue(Group.class);
                            if (codigo.equals(groupS.code)) {
                                codigo = groupS.code;
                                exist = true;
                                group = groupS;
                                groupProvider.updateMembers(group, mAuthProvider.getId());
                                break;
                            }
                        }
                        if (!exist) {
                            Toast.makeText(EnteredCodeActivity.this, "No existe el grupo", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent = new Intent(EnteredCodeActivity.this, GroupViewActivity.class);
                            intent.putExtra("codigo", codigo);
                            intent.putExtra("flag", 1);
                            startActivity(intent);
                        }
                    }else{
                        Toast.makeText(EnteredCodeActivity.this, "No hay grupos", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            Toast.makeText(this, "Ingrese un codigo", Toast.LENGTH_SHORT).show();
        }
    }
}