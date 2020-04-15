package com.example.android.wearable.datalayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {


    private List<VideoAdapter2> mUploads;

    private Context mContext;
    private onItemClickListener mListener;


    public VideoAdapter(Context context, List<VideoAdapter2> uploads) {

        mUploads = uploads;
        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        VideoAdapter2 uploadCurrent = mUploads.get(position);
        holder.VideoName.setText(uploadCurrent.getName());


    }

    public int getItemCount() {
        return mUploads.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
            , View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView VideoName;
        public VideoView videoView;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            VideoName = itemView.findViewById(R.id.video_name);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);


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


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("هل تريد حذف الفديو ؟");
            MenuItem delete = menu.add(Menu.NONE, 1, 1, "حذف");

            delete.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {

                        case 1:
                            mListener.onDeleteClick(position);
                            return true;

                    }
                }
            }
            return false;
        }
    }

    public interface onItemClickListener {
        void onItemClick(int position);



        void onDeleteClick(int position);

    }



    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;

    }
}