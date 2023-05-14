package com.example.videocall2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class call extends Fragment implements UserListener  {

    //User_details details;

    ArrayList<User_details> arrUsers = new ArrayList<>();
    RecyclerAdapterDetails adapter_app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view2 = inflater.inflate(R.layout.fragment_call, container, false);

        getUser(view2);
        return view2;
    }


    public void getUser(View view){

        RecyclerView recyclerView_app = (RecyclerView) view.findViewById(R.id.recycler_app);
        recyclerView_app.setLayoutManager(new LinearLayoutManager(getActivity()));


        SharedPreferences pref2 = getContext().getSharedPreferences("userse", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref2.edit();
        String loginemail = pref2.getString("email", null);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://video-call-828d6-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference("User_details");
        Query query = databaseReference.orderByChild("user_email").equalTo(loginemail);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot apt_details : dataSnapshot.getChildren()) {

                        User_details usersBean = apt_details.getValue(User_details.class);

                        arrUsers.add(new User_details(usersBean.user_pno,usersBean.user_pwd,usersBean.user_name, usersBean.user_email, usersBean.fcm_token, usersBean.key));
                        adapter_app = new RecyclerAdapterDetails(view.getContext(),arrUsers,call.this);
                        recyclerView_app.setAdapter(adapter_app);
                    }

                } else {
                    Toast.makeText(getContext(), "not found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void initiateVideoMeeting(User_details user) {
        if(user.fcm_token == null && user.fcm_token.trim().isEmpty()){
            Toast.makeText(getContext(), user.user_name+" "+" not available", Toast.LENGTH_LONG).show();
        }
        else{
            Intent intent = new Intent(getContext(), OutgoingInvitation.class);
            intent.putExtra("user_name",user.user_name);
            intent.putExtra("user_email",user.user_name);
            intent.putExtra("fcm_token",user.fcm_token);
            intent.putExtra("type","video");
            startActivity(intent);
        }
    }

    @Override
    public void initiateAudioMeeting(User_details user) {

    }
}