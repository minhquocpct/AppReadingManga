package com.example.appreadingmangav2.Apdater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appreadingmangav2.Model.Comment;
import com.example.appreadingmangav2.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private Context context;
    private ArrayList<Comment> arrayList;
    public CommentAdapter(Context context, ArrayList<Comment> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        Comment comment = arrayList.get(position);
        holder.txtNameUser.setText(comment.getNameUser());
        holder.txtComment.setText(comment.getComment());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        private TextView txtNameUser,txtComment;
        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            txtNameUser = itemView.findViewById(R.id.txtnameusercmt);
            txtComment = itemView.findViewById(R.id.txtcmt);
        }

    }

}
