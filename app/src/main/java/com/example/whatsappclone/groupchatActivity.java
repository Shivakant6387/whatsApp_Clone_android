package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.adapter.ChatAdapter;
import com.example.whatsappclone.databinding.ActivityGroupchatBinding;
import com.example.whatsappclone.model.MessageModels;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class groupchatActivity extends AppCompatActivity {


    ActivityGroupchatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGroupchatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(groupchatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final ArrayList<MessageModels>messageModels=new ArrayList<>();
        final String senderId= FirebaseAuth.getInstance().getUid();
        binding.userName.setText("Group Chat");
        final ChatAdapter adapter=new ChatAdapter(messageModels,this);
        binding.chatsRecycleView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        binding.chatsRecycleView.setLayoutManager(linearLayoutManager);
        database.getReference().child("Group Chat")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                messageModels.clear();
                                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                    MessageModels model=dataSnapshot.getValue(MessageModels.class);
                                    messageModels.add(model);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String message=binding.enterMessage.getText().toString();
                final MessageModels model=new MessageModels(senderId,message);
                model.setTimeStamp(new Date().getTime());
                binding.enterMessage.setText("");
                database.getReference().child("Group Chat")
                        .push()
                        .setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(groupchatActivity.this,"Message send.",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}