package com.example.android.wearable.datalayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoAdapter3 extends RecyclerView.Adapter<VideoAdapter3.ViewHolder3> {

    private List<VideoAdapter2> mUploads ;

    private Context mContext;
    private onItemClickListener mListener;

    public VideoAdapter3(Context context,List<VideoAdapter2> uploads) {
        mUploads = uploads;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_item_caregiver, parent, false);
        return new VideoAdapter3.ViewHolder3(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder3 holder, int position) {
        VideoAdapter2 uploadCurrent=mUploads.get(position);
        holder.VideoName.setText(uploadCurrent.getName());
        holder.advisorName.setText(uploadCurrent.getAdvisorName());
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ViewHolder3 extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView VideoName;
        public  TextView advisorName;

        public ViewHolder3(@NonNull View itemView) {
            super(itemView);
            VideoName=itemView.findViewById(R.id.video_name);
            advisorName=itemView.findViewById(R.id.advisor_name);


            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }



    }
    public interface onItemClickListener {
        void onItemClick(int position);

    }

    public void setOnItemClickListener( onItemClickListener listener) {
        mListener = listener;

    }
}