package com.example.appreadingmangav2.Apdater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appreadingmangav2.Model.Category;
import com.example.appreadingmangav2.Model.Chap;
import com.example.appreadingmangav2.R;

import java.util.ArrayList;

public class ChapAdapter extends RecyclerView.Adapter<ChapAdapter.ChapHolder> {
    private Context context;
    private ArrayList<Chap> arrayList;
    private OnChapListner onChapListner;
    public ChapAdapter(Context context, ArrayList<Chap> arrayList,OnChapListner onChapListner) {
        this.context = context;
        this.arrayList = arrayList;
        this.onChapListner = onChapListner;
    }

    @NonNull
    @Override
    public ChapHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chap,parent,false);
        return new ChapHolder(view, onChapListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapHolder holder, int position) {
        Chap chap = arrayList.get(position);
        holder.txtNameChap.setText(chap.getNameChap());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ChapHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtNameChap;
        OnChapListner onChapListner;
        public ChapHolder(@NonNull View itemView, OnChapListner onChapListner) {
            super(itemView);
            txtNameChap = itemView.findViewById(R.id.txtitem_chap);
            this.onChapListner = onChapListner;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            onChapListner.onChapClick(getAdapterPosition());
        }
    }
    public interface OnChapListner {
        void onChapClick(int position);
    }
}
