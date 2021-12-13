package com.proyect.proyectoboris;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class GroupViewAdapter extends FirebaseRecyclerAdapter<Group, GroupViewAdapter.ViewHolder> {

    public GroupViewAdapter(FirebaseRecyclerOptions<Group> options){
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Group model) {

        holder.textViewNameGroup.setText(model.getName());

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

        public ViewHolder(View view){
            super(view);
            textViewNameGroup = view.findViewById(R.id.textViewNameGroup);
            btnViewCode = view.findViewById(R.id.buttonViewCode);
            btnViewCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context,CodeActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
