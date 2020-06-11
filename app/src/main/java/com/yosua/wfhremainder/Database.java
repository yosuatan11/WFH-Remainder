package com.yosua.wfhremainder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {
    DatabaseReference database;
    FirebaseAuth firebaseAuth;
    public Database() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.getCurrentUser();
        this.database = FirebaseDatabase.getInstance().getReference(firebaseAuth.getUid().toString());
    }

    public boolean add(Data data){
        return database.child(data.getId().toString())
                .setValue(data)
                .isSuccessful();
    }

    public boolean delete(String key){
        return database.child(key)
                .removeValue()
                .isSuccessful();
    }
}
