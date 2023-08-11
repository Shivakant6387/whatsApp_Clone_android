package com.example.whatsappclone;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Get a reference to the ActionBar
        ActionBar actionBar = getSupportActionBar();

        // Check if the ActionBar is not null before using it
        if (actionBar != null) {
            // Now you can call methods on the ActionBar
            actionBar.hide();
        }
    }
}