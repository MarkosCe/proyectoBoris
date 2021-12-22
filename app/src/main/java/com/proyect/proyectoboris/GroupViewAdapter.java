package com.proyect.proyectoboris;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupViewAdapter extends FirebaseRecyclerAdapter<Group, GroupViewAdapter.ViewHolder> {

    private GroupProvider groupProvider;
    //private ArrayList<String> membrs;
    private Context mcontext;

    public GroupViewAdapter(FirebaseRecyclerOptions<Group> options, Context context){
        super(options);

        groupProvider = new GroupProvider();
        mcontext = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Group model) {

        holder.textViewNameGroup.setText(model.getName());
        String codi = model.getCode();
        String id = model.getId();
        holder.btnViewCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToViewCode(codi);
            }
        });

        holder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMap(id);
            }
        });
    }

    private void goToViewCode(String code){
        Intent intent = new Intent(mcontext,CodeActivity.class);
        intent.putExtra("codigo", code);
        mcontext.startActivity(intent);
    }

    private void goToMap(String id){
        Intent intent = new Intent(mcontext,MapUserActivity.class);
        intent.putExtra("idgrupo", id);
        mcontext.startActivity(intent);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_group, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewNameGroup;
        private ImageButton btnViewCode;

        private View myView;

        public ViewHolder(View view){
            super(view);
            myView = view;
            textViewNameGroup = view.findViewById(R.id.textViewNameGroup);
            btnViewCode = view.findViewById(R.id.buttonViewCode);
        }
    }
}
