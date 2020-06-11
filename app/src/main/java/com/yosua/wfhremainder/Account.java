package com.yosua.wfhremainder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Account extends AppCompatActivity {
    EditText newpass1, newpass;
    Button acc, logout;
    FirebaseAuth firebaseAuth;
    Remainder remainder;
    Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        //assign variable
        newpass1 = findViewById(R.id.newpass1);
        newpass = findViewById(R.id.newpass);
        acc = findViewById(R.id.change);
        logout = findViewById(R.id.logout);
        firebaseAuth = FirebaseAuth.getInstance();
        database = new Database();
        remainder = new Remainder();

        //if button change password pressed
        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newpass.getText().toString() == newpass1.getText().toString()){
                    //change the user password
                    firebaseAuth.getCurrentUser().updatePassword(newpass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isComplete()){
                                Toast.makeText(Account.this, "Password has change", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Account.this, MainActivity.class));
                            }
                            else{
                                Toast.makeText(Account.this, "Error "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else {
                    newpass1.setError("Password don't match");
                    newpass1.requestFocus();
                }
            }
        });
        // logout button pressed
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getCurrentUser();
                firebaseAuth.signOut();
                database.database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                            Data data1 = keyNode.getValue(Data.class);
                            if(data1.isStatus()) {
                                if(!remainder.checkIfAlarmIsRegistred(Account.this, data1.getId())){
                                    remainder.cancelAlarm(Account.this, data1.getId());
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Intent intent = new Intent(Account.this, Started.class);
                startActivity(intent);
            }
        });
    }

}
