package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatsappclone.adapter.ChatAdapter;
import com.example.whatsappclone.databinding.ActivityChatDetailBinding;
import com.example.whatsappclone.model.MessageModels;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth  auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        final String senderId=auth.getUid();
        String recieveId=getIntent().getStringExtra("userId");
        String userName=getIntent().getStringExtra("userName");
        String profile=getIntent().getStringExtra("profile");
        binding.userName.setText(userName);
        Picasso.get().load(profile).placeholder(R.drawable.avatar).into(binding.profileImage);
    binding.backArrow.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(ChatDetailActivity.this, MainActivity.class);
            startActivity(intent);
        }
    });

    final ArrayList<MessageModels> messageModels=new ArrayList<>();
    final ChatAdapter chatAdapter=new ChatAdapter(messageModels,this,recieveId);
    binding.chatsRecycleView.setAdapter(chatAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        binding.chatsRecycleView.setLayoutManager(linearLayoutManager);
        final String senderRoom=senderId+recieveId;
        final String receiveRoom=recieveId+senderId;

        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                 MessageModels model=snapshot1.getValue(MessageModels.class);
                 model.setMessageId(snapshot1.getKey());
                 messageModels.add(model);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=binding.enterMessage.getText().toString();
                final MessageModels models=new MessageModels(senderId,message);
                models.setTimeStamp(new Date().getTime());
                binding.enterMessage.setText("");
                database.getReference().child("chats")
                        .child(senderRoom)
                        .push().
                        setValue(models)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                         database.getReference()
                                 .child("chats")
                                 .child(receiveRoom)
                                 .push().setValue(models)
                                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void unused) {

                             }
                         });
                    }
                });
            }
        });
    }
}