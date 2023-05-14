package com.example.videocall2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class profile extends Fragment {

    TextView r_phone, r_email, r_name;
    StorageReference storageReference;
    Bitmap bitmap;
    ImageView profile_r_img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences pref = getContext().getSharedPreferences("usersp", 0); // 0 - for private mode
        String loginpno = pref.getString("Phno", null);

        SharedPreferences pref2 = getContext().getSharedPreferences("userse", 0); // 0 - for private mode
        String loginemail = pref2.getString("email", null);

        r_phone = (TextView) view.findViewById(R.id.r_phone);
        r_phone.setText(loginpno.substring(3,13));


        r_email = (TextView) view.findViewById(R.id.r_email);
        r_name = (TextView) view.findViewById(R.id.r_name);
        profile_r_img = (ImageView) view.findViewById(R.id.profile_r_img);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://video-call-828d6-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference("User_details");
        Query query = databaseReference.orderByChild("user_email").equalTo(loginemail);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        User_details details = user.getValue(User_details.class);
                        r_email.setText(details.user_email);
                        r_name.setText(details.user_name);

                    }
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        storageReference = FirebaseStorage.getInstance().getReference("images/"+loginemail);

        try {
            File localfile = File.createTempFile("tempFile",".jpg");
            storageReference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            profile_r_img.setImageBitmap(bitmap);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getContext(), "failed to retreive", Toast.LENGTH_SHORT).show();


                }
            });
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        return view;
    }
}