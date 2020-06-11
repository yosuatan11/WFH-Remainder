package com.yosua.wfhremainder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Started extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_started);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            Intent Main = new Intent(Started.this, MainActivity.class);
            startActivity(Main);
            finish();
        }
    }

    public void Login(View view) {
        startActivity(new Intent(this, Login.class));
        finish();
    }

}
