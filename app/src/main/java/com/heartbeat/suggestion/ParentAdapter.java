package com.heartbeat.suggestion;
import android.content.Context;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.duckdns.berdosi.heartbeat.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ViewHolders> {
    Context ctx;
    RecyclerView.RecycledViewPool recycledViewPool;
    ArrayList<ParentModel> parentModel;
public ParentAdapter(Context ctx,ArrayList<ParentModel> parentModel){

    this.ctx=ctx;
    this.parentModel=parentModel;
    recycledViewPool=new RecyclerView.RecycledViewPool();
}
    @NonNull
    @Override
    public ViewHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.parent_row,viewGroup,false);
        return new ViewHolders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolders viewHolders, int i) {
    viewHolders.title.setText(parentModel.get(i).getTitle());
    ChildAdapter childAdapter=new ChildAdapter(parentModel.get(i).getChilds(),ctx.getApplicationContext());
    viewHolders.childRecycler.setRecycledViewPool(recycledViewPool);
    viewHolders.childRecycler.setLayoutManager(new LinearLayoutManager(ctx.getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
    viewHolders.childRecycler.setItemAnimator(new DefaultItemAnimator());
    viewHolders.childRecycler.setAdapter(childAdapter);

    }

    @Override
    public int getItemCount() {
        return parentModel.size();
    }

    class  ViewHolders extends RecyclerView.ViewHolder{
        RecyclerView childRecycler;
        TextView title;

      public ViewHolders(@NonNull View itemView) {
          super(itemView);
          childRecycler=itemView.findViewById(R.id.childRecycler);
          title=itemView.findViewById(R.id.title_parent);
      }

  }

}
