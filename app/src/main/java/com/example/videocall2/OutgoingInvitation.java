package com.example.videocall2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

public class OutgoingInvitation extends AppCompatActivity {

    private String invitationToken = null;

    public static HashMap<String, String> getRemoteMessageHeaders(){
        HashMap<String, String> headers = new HashMap<>();
        headers.put(
                "Authorization",
                "key=BDeqa1dsIvrBqi9WZesNcCpl0z4etm-O7IlhrJ40tc-ZecZHX6FZNyPqg0TPdtS7XUxsMNMKDeD45wLva0dtB8c"
        );
        headers.put("Content-type","application/json");
        return headers;
    }

    public static final String REMOTE_MSG_TYPE = "type";
    public static final String REMOTE_MSG_INVITATION = "invitation";
    public static final String REMOTE_MSG_MEETING_TYPE = "meetingType";
    public static final String REMOTE_MSG_INVITER_ICON = "inviterToken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_invitation);

        TextView textUserName = findViewById(R.id.textEmail);
        TextView textEmail = findViewById(R.id.textEmail);
        TextView textFirstName = findViewById(R.id.textFirstName);

        textUserName.setText(getIntent().getStringExtra("user_name"));
        textEmail.setText(getIntent().getStringExtra("user_email"));
        textFirstName.setText(getIntent().getStringExtra("user_name").substring(0,1));

        ImageView reject = (ImageView) findViewById(R.id.ImageStopInvitation);
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(task.isSuccessful() && task.getResult() != null){
                    invitationToken = task.getResult().getToken();
                }
            }
        });
    }
}