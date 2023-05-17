package com.example.appreadingmangav2.Apdater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appreadingmangav2.Model.Manga;
import com.example.appreadingmangav2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MangaAdapter extends ArrayAdapter<Manga> {
    private Context ct;
    private ArrayList<Manga> arrayList;
    public MangaAdapter(Context context, int resource, List<Manga> objects){
        super(context, resource, objects);
        this.ct = context;
        this.arrayList = new ArrayList<>(objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_manga, null);
        }
        if (arrayList.size() >0){
            Manga manga= this.arrayList.get(position);
            TextView nameManga = convertView.findViewById(R.id.txtname_item_manga);
            ImageView imgImageManga = convertView.findViewById(R.id.img_item_manga);
            nameManga.setText(manga.getNameManga());
            Picasso.get().load(manga.getLinkImage()).into(imgImageManga);
        }
        return convertView;
    }

}