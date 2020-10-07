package com.heartbeat.history;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heartbeat.HeartBeat;

import org.duckdns.berdosi.heartbeat.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter {
    private ArrayList<HeartBeat> heartBeatArrayList;
    private Context ctx;
    private int dateChange = 100;
    private String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};


    HistoryAdapter(Context ctx, ArrayList<HeartBeat> laundryHistories) {
        this.ctx = ctx;
        this.heartBeatArrayList = laundryHistories;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == dateChange) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month_change, parent, false);
            return new DateViewholder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
            return new ViewHolders(v, ctx);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int month = heartBeatArrayList.get(position).getMonth() + 1;
        if (holder.getItemViewType() == dateChange) {
            ((DateViewholder) holder).date.setText("" + months[month - 1]);
            ((DateViewholder) holder).heartrate.setText("" + heartBeatArrayList.get(position).getHeartbeat() + " " + "HP");
            ((DateViewholder) holder).month.setText(" " + month);


        } else {
            ((ViewHolders) holder).heartbeat.setText(heartBeatArrayList.get(position).getHeartbeat() + " " + "HP");
            //month starts from 0 to 11
            ((ViewHolders) holder).month.setText("" + month);
        }
    }


    @Override
    public int getItemViewType(int position) {

        int currentMonth = heartBeatArrayList.get(position).getMonth() + 1;
        if (position != 0) {
            Log.v("HistoryAdapter", "Month" + (heartBeatArrayList.get(position - 1).getMonth() + 1));
        }
        Log.v("HistoryAdapter", "CurrentMonth" + currentMonth);
        if (position == 0) {
            return dateChange;
        } else if ((heartBeatArrayList.get(position - 1).getMonth() + 1) != currentMonth) {
            return dateChange;
        } else {
            return position;
        }
    }


    @Override
    public int getItemCount() {
        return heartBeatArrayList.size();
    }

    static class ViewHolders extends RecyclerView.ViewHolder {
        Context ctx;
        TextView heartbeat, month;

        ViewHolders(@NonNull View itemView, Context ctx) {
            super(itemView);
            this.ctx = ctx;
            heartbeat = itemView.findViewById(R.id.heartrate_history);
            month = itemView.findViewById(R.id.month);
        }
    }

    public static class DateViewholder extends RecyclerView.ViewHolder {

        TextView date, month, heartrate;

        DateViewholder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.month_show_history);
            month = itemView.findViewById(R.id.month_item_month);
            heartrate = itemView.findViewById(R.id.heartrate_history_item);
        }
    }

}
