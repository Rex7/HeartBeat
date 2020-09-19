package com.heartbeat.suggestion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.duckdns.berdosi.heartbeat.R;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.Viewholders> {
  private  Context ctx;
   private  ArrayList<ChildModel>childModels;
    public ChildAdapter(ArrayList<ChildModel> childModels,Context ctx){
        this.ctx=ctx;
        this.childModels=childModels;
    }


    @NonNull
    @Override
    public Viewholders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_childrow,viewGroup,false);

        return new Viewholders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholders viewholders, int i) {
        viewholders.gameName.setText(childModels.get(i).getName());
        Glide.with(ctx).load(childModels.get(i).getIcon()).into(viewholders.imageView);


    }

    @Override
    public int getItemCount() {
        return childModels.size();
    }

    class Viewholders extends RecyclerView.ViewHolder{
        TextView gameName;
        ImageView imageView;

        public Viewholders(@NonNull View itemView) {
            super(itemView);
            gameName=itemView.findViewById(R.id.gameName);
            imageView=itemView.findViewById(R.id.ImageViewVertical);
        }
    }

}
