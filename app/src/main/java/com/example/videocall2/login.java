package com.example.videocall2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class login extends AppCompatActivity {

    EditText user_email , user_pwd;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        user_email = (EditText) findViewById(R.id.user_email);
        user_pwd = (EditText) findViewById(R.id.user_pwd);

        String phone = user_email.getText().toString();

        login = (Button) findViewById(R.id.login);

        //
        //SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        //SharedPreferences.Editor editor = pref.edit();

//        String loginpno = pref.getString("Pnol", null);
//        String loginpass = pref.getString("Pwdl", null);



        //

        SharedPreferences pref = getApplicationContext().getSharedPreferences("userse", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://video-call-828d6-default-rtdb.asia-southeast1.firebasedatabase.app/");
                DatabaseReference databaseReference = firebaseDatabase.getReference("User_details");
                Query query = databaseReference.orderByChild("user_email").equalTo(user_email.getText().toString());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            for (DataSnapshot user : dataSnapshot.getChildren()) {
                                User_details details = user.getValue(User_details.class);
                                if(details.user_pwd.equals(user_pwd.getText().toString())){
                                    //Toast.makeText(login.this, "login successfully", Toast.LENGTH_LONG).show();
                                    editor.putString("email", user_email.getText().toString());
                                    editor.commit();
                                    Intent intent = new Intent(login.this,verifyNumber.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(login.this, "password didn't match", Toast.LENGTH_LONG).show();
                                }

                            }
                        } else {
                            Toast.makeText(login.this, "not found", Toast.LENGTH_LONG).show();
                            Toast.makeText(login.this, " "+user_email.getText().toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}