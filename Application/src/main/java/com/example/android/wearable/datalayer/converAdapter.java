package com.example.android.wearable.datalayer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class converAdapter extends RecyclerView.Adapter<converAdapter.Viewholder3>{
    private static final String TAG = "converAdapter";

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> msgs = new ArrayList<>();
    private Context mContext;

    public converAdapter(ArrayList<String> names, ArrayList<String> msgs, Context mContext) {
        this.names = names;
        this.msgs = msgs;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Viewholder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listofconver, parent, false);
        Viewholder3 holder = new Viewholder3(view);
        return holder;     }

    @Override
    public void onBindViewHolder(@NonNull Viewholder3 holder, final int position) {
        Log.d(TAG, "onBindViewHolder:called");

        holder.t1.setText(names.get(position));
        holder.t2.setText(msgs.get(position));
        holder.chatters.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick:clicked on:" + names.get(position));
                Toast.makeText(mContext, names.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class Viewholder3 extends RecyclerView.ViewHolder{

        TextView t1, t2;
        RelativeLayout chatters;

        public Viewholder3(@NonNull View itemView) {
            super(itemView);

            t1 = itemView.findViewById(R.id.caregiver_name);
          //  t2 = itemView.findViewById(R.id.msg);
            chatters = itemView.findViewById(R.id.chatters);

        }
    }
}
