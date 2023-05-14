package com.example.videocall2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText reg_name, reg_pwd, reg_pno, reg_email;
    Button register_user,loginf;
    BackendUser bu;
    ImageView profilepic;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    Integer isImageUploaded=0;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth mAuth;
    CountryCodePicker ccp;
    private DatabaseReference databaseReferencew;

    public static String nameS, phnoS, pwdS, emailS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        FirebaseApp.initializeApp(Context);

        mAuth = FirebaseAuth.getInstance();

        reg_name = (EditText) findViewById(R.id.reg_name);
        reg_pwd = (EditText) findViewById(R.id.reg_pwd);
        reg_pno = (EditText) findViewById(R.id.reg_pno);
        reg_email = (EditText) findViewById(R.id.reg_email);
        ccp=(CountryCodePicker)findViewById(R.id.ccp) ;
        ccp.registerCarrierNumberEditText(reg_pno);
        profilepic = (ImageView) findViewById(R.id.profilepic);

        register_user = (Button) findViewById(R.id.user_reg) ;
        loginf = (Button) findViewById(R.id.loginf) ;
        bu = new BackendUser();

        //image upload firebase storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference() ;


        //auto login
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
//        SharedPreferences.Editor editor = pref.edit();


        //String loginemail = pref.getString("Pnol", null);
        //String loginpass = pref.getString("Pwdl", null);

//        editor.clear();
//        editor.commit();
//
//
//        Toast.makeText(user_registeration.this, loginpass+" "+loginpno, Toast.LENGTH_SHORT).show();


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://video-call-828d6-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference("User_details");

        //Query query = databaseReference.orderByChild("user_pno").equalTo(loginemail);



        SharedPreferences pref2 = getSharedPreferences("userse", 0);
        String loginemail = pref2.getString("email", null);

        if(loginemail!=null){
            Intent intent = new Intent(MainActivity.this, home.class);
            startActivity(intent);
        }

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePic();
            }
        });

        loginf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,login.class);
                startActivity(intent);
            }
        });

        register_user.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(reg_name.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"enter name",Toast.LENGTH_LONG).show();
                }
                if(reg_email.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"enter email",Toast.LENGTH_LONG).show();
                }
                else if(reg_pno.getText().toString().equals("") || ccp.getFullNumberWithPlus().substring(3).length()!=10){
                    Toast.makeText(MainActivity.this,"enter valid phone",Toast.LENGTH_LONG).show();
                }
                else if(reg_pwd.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"set password",Toast.LENGTH_LONG).show();
                }
                else if(isImageUploaded == 0){
                    Toast.makeText(MainActivity.this,"select a profile picture",Toast.LENGTH_LONG).show();
                }

                else{
                    nameS = reg_name.getText().toString();
                    pwdS = reg_pwd.getText().toString();
                    phnoS = ccp.getFullNumberWithPlus().substring(3).toString().trim();
                    emailS = reg_email.getText().toString();
                    uploadPicture(reg_pno.getText().toString().trim());
                    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if(task.isSuccessful() && task.getResult() != null){

                                FirebaseDatabase dbu = FirebaseDatabase.getInstance("https://video-call-828d6-default-rtdb.asia-southeast1.firebasedatabase.app/");
                                databaseReferencew = dbu.getReference(User_details.class.getSimpleName());
                                String key= databaseReferencew.push().getKey();

                                Map<String, Object> map = new HashMap<>();
                                map.put("fcm_token", task.getResult().getToken());
                                map.put("key", key);
                                map.put("user_email", nameS);
                                map.put("user_name",  emailS);
                                map.put("user_pwd", pwdS);
                                map.put("user_pno", phnoS);


                                databaseReference
                                        .child(key)
                                        .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Intent intent = new Intent(MainActivity.this,login.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });
    }

    private void choosePic(){
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(user_registeration.this,"requestCode:" + requestCode + "")
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            profilepic.setImageURI(imageUri);
            isImageUploaded = 1;
        }
    }

    private void uploadPicture(String pno){

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading details....");
        pd.show();

        StorageReference stf = storageReference.child("images/" +emailS );

        stf.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Snackbar.make(findViewById(android.R.id.content),"image uploaded ",Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Failed to upload",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
                        pd.setMessage("Progress: " + (int) progressPercent + "%");
                    }
                });
    }

}