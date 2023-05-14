package com.example.videocall2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapterDetails extends RecyclerView.Adapter<RecyclerAdapterDetails.ViewHolder>{

    private UserListener userListener;

    Context context;
    ArrayList<User_details> arrDetails;
    RecyclerAdapterDetails(Context context, ArrayList<User_details> arrDetails, UserListener userListener){

        this.context=context;
        this.arrDetails = arrDetails;
        this.userListener = userListener;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User_details model = (User_details) arrDetails.get(position);

        holder.recycler_name.setText(arrDetails.get(position).user_name);

        holder.video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userListener.initiateVideoMeeting(arrDetails.get(position));
            }
        });

        holder.audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userListener.initiateAudioMeeting(arrDetails.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView recycler_name;
        LinearLayout AppRow;
        Button video,audio;

        public ViewHolder(View itemView){
            super(itemView);

            recycler_name = itemView.findViewById(R.id.recycler_name);

            AppRow = itemView.findViewById(R.id.AppRow);

            video = (Button) itemView.findViewById(R.id.video);
            audio = (Button) itemView.findViewById(R.id.audio);

        }
    }
}
