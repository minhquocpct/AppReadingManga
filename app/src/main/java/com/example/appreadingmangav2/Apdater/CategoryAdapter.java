package com.example.appreadingmangav2.Apdater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appreadingmangav2.Model.Category;
import com.example.appreadingmangav2.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private Context context;
    private ArrayList<Category> arrayList;
    private OnCategoryListner onCategoryListner;
    public CategoryAdapter(Context context, ArrayList<Category> arrayList,OnCategoryListner onCategoryListner) {
        this.context = context;
        this.arrayList = arrayList;
        this.onCategoryListner = onCategoryListner;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        return new CategoryHolder(view, onCategoryListner);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Category category = arrayList.get(position);
        holder.txtNameCategory.setText(category.getNameCategory());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtNameCategory;
        OnCategoryListner onCategoryListner;
        public CategoryHolder(@NonNull View itemView, OnCategoryListner onCategoryListner) {
            super(itemView);
            txtNameCategory = itemView.findViewById(R.id.txtitem_category);
            this.onCategoryListner = onCategoryListner;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            onCategoryListner.onCategoryClick(getAdapterPosition());
        }
    }
    public interface OnCategoryListner {
        void onCategoryClick(int position);
    }
}
