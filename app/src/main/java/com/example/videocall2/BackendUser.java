package com.example.videocall2;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BackendUser {
    private DatabaseReference databaseReferencew;

    public BackendUser(){
        FirebaseDatabase dbu = FirebaseDatabase.getInstance("https://video-call-828d6-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReferencew = dbu.getReference(User_details.class.getSimpleName());

    }
    public Task<Void> add(User_details details){
        return databaseReferencew.push().setValue(details);
    }
}
