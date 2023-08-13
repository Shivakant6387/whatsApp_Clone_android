package com.example.whatsappclone;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatsappclone.databinding.ActivityChatDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth  auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       // getSupportActionBar().hide();
        // Get a reference to the ActionBar
        ActionBar actionBar = getSupportActionBar();

        // Check if the ActionBar is not null before using it
        if (actionBar != null) {
            // Now you can call methods on the ActionBar
            actionBar.hide();
        }
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
    }
}