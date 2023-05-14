package com.example.videocall2;

public class User_details {
    public String user_pno, user_pwd, user_name, user_email, fcm_token, key;

    public User_details(){}

    public User_details(String user_name, String user_pwd, String user_pno, String user_email, String fcm_token, String key) {
        this.user_pno = user_pno;
        this.user_pwd = user_pwd;
        this.user_name = user_name;
        this.user_email = user_email;
        this.fcm_token = fcm_token;
        this.key =key;
    }
}
