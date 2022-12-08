package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder> {


    private UsersFragment usersFragment;
    private Context context;
    private Boolean isChat;

    FirebaseAuth firebaseAuth;
    String uid;

    public AdapterUsers(Context context, List<ModelUsers> list) {
        this.context = context;
        this.isChat = isChat;
        this.list = list;
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();

    }

    List<ModelUsers> list;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {

        ModelUsers modelUsers = list.get(position);

//        final String hisuid = list.get(position).getUid();
        final String hisuid = list.get(position).getUid();
        String userImage = list.get(position).getImage();
        String username = list.get(position).getName();
        String usermail = list.get(position).getEmail();



//                if (modelUsers.onlineStatus.contains("online")) {
//
//                } else {
//
//                }



        holder.name.setText(username);
        holder.email.setText(usermail);
        try {
            Glide.with(context).load(userImage).into(holder.profiletv);
        } catch (Exception e) {
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("uiD", modelUsers.getUid());
//                intent.putExtra("name", modelUsers.getName());
//                intent.putExtra("image", modelUsers.getImage());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        CircleImageView profiletv;
        TextView name, email;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profiletv = itemView.findViewById(R.id.imagep);
            name = itemView.findViewById(R.id.namep);
            email = itemView.findViewById(R.id.emailp);
        }

    }
}
