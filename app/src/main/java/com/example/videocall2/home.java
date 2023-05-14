package com.example.videocall2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.videocall2.databinding.ActivityHomeBinding;
import com.example.videocall2.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class home extends AppCompatActivity{

    ActivityHomeBinding binding;
    String nameU, phnoU, passU, key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        replaceFragment(new profile());

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences pref2 = getSharedPreferences("userse", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref2.edit();
        String loginemail = pref2.getString("email", null);

        binding.bottomNavigation2.setOnItemSelectedListener(item -> {

            switch(item.getItemId()){
                case R.id.profile:
                    replaceFragment(new profile());
                    break;
                case R.id.call:
                    replaceFragment(new call());
                    break;
                case R.id.logout:
                    sendToken("", loginemail);
                    editor.clear();
                    editor.commit();
                    Intent intent = new Intent(home.this, MainActivity.class);
                    startActivity(intent);
                    break;


            }

            return true;
        });




        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(task.isSuccessful() && task.getResult() != null){
                    Toast.makeText(home.this, ""+task.getResult().getToken(), Toast.LENGTH_SHORT).show();
                    sendToken(task.getResult().getToken(), loginemail);
                }
            }
        });



    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.commit();
    }

    public void sendToken(String token, String loginemail){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://video-call-828d6-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference("User_details");
        Query query = databaseReference.orderByChild("user_email").equalTo(loginemail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        User_details details = user.getValue(User_details.class);

                        Map<String, Object> map = new HashMap<>();
                        map.put("fcm_token", token );
                        map.put("key", details.key);
                        map.put("user_email", details.user_email);
                        map.put("user_name",  details.user_name);
                        map.put("user_pwd", details.user_pwd);
                        map.put("user_pno", details.user_pno);


                        databaseReference
                                .child(details.key)
                                .updateChildren(map);
                    }
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}