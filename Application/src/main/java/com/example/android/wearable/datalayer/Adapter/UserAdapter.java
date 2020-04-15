package com.example.android.wearable.datalayer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.wearable.datalayer.MessageActivity;
import com.example.android.wearable.datalayer.Model.User;
import com.example.android.wearable.datalayer.R;
import com.example.android.wearable.datalayer.R;

import java.util.List;

public class UserAdapter extends  RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context mContext;
    private List<User> mUsers;
    private boolean ischat;


    public UserAdapter(Context mContext, List<User> mUsers, boolean ischat){
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.listofconv_caregiver, parent, false);
        return new UserAdapter.ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = mUsers.get(position);


        holder.username1.setText(user.getfirstName());
        holder.major.setText(user.getRole());
        //holder.profile_image.setImageResource(R.drawable.user);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getuid());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username1,major;
        public ImageView profile_image;

        private ImageView img_on;
        //private ImageView img_off;
       // private TextView last_msg;



        public ViewHolder(View itemView) {
            super(itemView);

            username1 = itemView.findViewById(R.id.username_advisor);
            profile_image = itemView.findViewById(R.id.profile_image2);
            major=itemView.findViewById(R.id.major);

           // img_on = itemView.findViewById(R.id.img_on);
           // img_off = itemView.findViewById(R.id.img_off);
            //last_msg = itemView.findViewById(R.id.last_msg);


        }
    }

}
