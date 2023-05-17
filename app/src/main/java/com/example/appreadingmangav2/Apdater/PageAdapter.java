package com.example.appreadingmangav2.Apdater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appreadingmangav2.Model.Page;
import com.example.appreadingmangav2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PageAdapter extends RecyclerView.Adapter<PageAdapter.PageHolder> {
    private Context context;
    private ArrayList<Page> arrayList;
    public PageAdapter(Context context, ArrayList<Page> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public PageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page,parent,false);
        return new PageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageHolder holder, int position) {
        Page page = arrayList.get(position);
        holder.txtPageNum.setText(page.getNumberPage());
        Picasso.get().load(page.getLinkImagePage()).into(holder.imgPage);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class PageHolder extends RecyclerView.ViewHolder {
        private TextView txtPageNum;
        private ImageView imgPage;
        public PageHolder(@NonNull View itemView) {
            super(itemView);
            txtPageNum = itemView.findViewById(R.id.numberpage);
            imgPage = itemView.findViewById(R.id.img_item_page);
        }
    }

}
