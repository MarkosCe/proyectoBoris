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

    //private String codigo = "";
    //private String id = "";
    private GroupProvider groupProvider;
    private ArrayList<String> array;

    public GroupViewAdapter(FirebaseRecyclerOptions<Group> options){
        super(options);

        groupProvider = new GroupProvider();
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Group model) {

        holder.textViewNameGroup.setText(model.getName());
        String codigo = model.getCode();
        String id = model.getId();
        holder.btnViewCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToViewCode(v.getContext(), codigo);
            }
        });
        holder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMap(v.getContext(), id);
            }
        });
    }

    private void goToViewCode(Context context, String code){
        Intent intent = new Intent(context,CodeActivity.class);
        intent.putExtra("codigo", code);
        context.startActivity(intent);
    }

    private void goToMap(Context context, String id){
        groupProvider.getGroupId(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Group group = snapshot.getValue(Group.class);
                    assert group != null;
                    array = new ArrayList<>(group.getMembers().keySet());
                    Log.i("array",array.get(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Intent intent = new Intent(context,MapUserActivity.class);
        intent.putStringArrayListExtra("members", array);
        context.startActivity(intent);

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

        //private String codigo = "";

        View myView;

        public ViewHolder(View view){
            super(view);
            myView = view;
            textViewNameGroup = view.findViewById(R.id.textViewNameGroup);
            btnViewCode = view.findViewById(R.id.buttonViewCode);
            /*btnViewCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context,CodeActivity.class);
                    intent.putExtra("codigo", codigo);
                    context.startActivity(intent);
                }
            });*/
        }
    }
}
