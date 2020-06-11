package com.yosua.wfhremainder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Data> data;
    MainAdapter mainAdapter;
    Database database;
    ImageButton add;
    Remainder remainder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = new Database();
        remainder = new Remainder();


        //assign variable
        recyclerView = (RecyclerView) findViewById (R.id.reclyerView);
        add = findViewById(R.id.add);
        data = new ArrayList<Data>();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(
                MainActivity.this, LinearLayoutManager.VERTICAL, false
        );
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mainAdapter = new MainAdapter(MainActivity.this, data);
        recyclerView.setAdapter(mainAdapter);
        database.database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    Data data1 = keyNode.getValue(Data.class);
                    if(data1.isStatus()) {
                        if(!remainder.checkIfAlarmIsRegistred(MainActivity.this, data1.getId())){
                            remainder.SetAlarm(MainActivity.this, data1.getTime(), data1.getActivity(), data1.getId());
                        }
                    }
                    else{
                        if(remainder.checkIfAlarmIsRegistred(MainActivity.this, data1.getId())){
                            remainder.cancelAlarm(MainActivity.this, data1.getId());
                        }

                    }
                    data.add(data1);
                    mainAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail = new Intent(MainActivity.this, Detail.class);
                detail.putExtra("title", "Create New Remainder");
                MainActivity.this.startActivity(detail);
            }
        });


    }

    public void account(View view) {
        startActivity(new Intent(this, Account.class));
    }
}
