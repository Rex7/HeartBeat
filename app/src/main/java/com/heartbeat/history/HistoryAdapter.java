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
            ((DateViewholder) holder).date.setText(months[month - 1]);
            ((DateViewholder) holder).heartrate.setText(ctx.getResources().getString(R.string.heart_rate_with_hp,heartBeatArrayList.get(position).getHeartbeat()));
            ((DateViewholder) holder).month.setText(ctx.getResources().getString(R.string.day,heartBeatArrayList.get(position).getDay_of_month()));


        } else {
            ((ViewHolders) holder).heartbeat.setText(ctx.getResources().getString(R.string.heart_rate_with_hp,heartBeatArrayList.get(position).getHeartbeat()));
            //month starts from 0 to 11
            ((ViewHolders) holder).day.setText(ctx.getResources().getString(R.string.day,heartBeatArrayList.get(position).getDay_of_month()));
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
        TextView heartbeat, day;

        ViewHolders(@NonNull View itemView, Context ctx) {
            super(itemView);
            this.ctx = ctx;
            heartbeat = itemView.findViewById(R.id.heartrate_history);
            day = itemView.findViewById(R.id.dayofmonth);
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
