package com.yosua.wfhremainder;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    ArrayList<Data> data;
    Context context;
    Remainder remainder;

    public MainAdapter(Context context, ArrayList<Data> data) {
        this.data = data;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_item, parent, false);
        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.activity.setText(data.get(position).getActivity());
        holder.time.setText(data.get(position).getTime());
        holder.status.setChecked(data.get(position).isStatus());
    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        private TextView activity;
        private TextView time;
        private SwitchCompat status;
        RelativeLayout rl1;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            activity = itemView.findViewById(R.id.activity);
            time = itemView.findViewById(R.id.time);
            status = itemView.findViewById(R.id.status);
            rl1 = (RelativeLayout) itemView.findViewById(R.id.rl1);
            rl1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context = v.getContext();
                    Data currentdata = data.get(getAdapterPosition());
                    Intent detail = new Intent(context, Detail.class);
                    detail.putExtra("activity", currentdata.getActivity());
                    detail.putExtra("time", currentdata.getTime());
                    detail.putExtra("status", currentdata.isStatus());
                    detail.putExtra("title", "Change clock");
                    detail.putExtra("id", currentdata.getId());
                    context.startActivity(detail);
                }
            });
            status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    context = itemView.getContext();
                    Data currentdata = data.get(getAdapterPosition());
                    currentdata.setStatus(isChecked);
                    Database database = new Database();
                    database.add(currentdata);
                    System.out.println("here");
                }
            });
        }


    }





}
