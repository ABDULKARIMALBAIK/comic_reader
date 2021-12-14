package com.example.asus.androidcomicreader.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.androidcomicreader.ChapterActivity;
import com.example.asus.androidcomicreader.Common.Common;
import com.example.asus.androidcomicreader.Interface.IRecyclerOnClick;
import com.example.asus.androidcomicreader.Model.Comic;
import com.example.asus.androidcomicreader.R;
import com.example.asus.androidcomicreader.ViewHolder.ComicViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicViewHolder> {

    Context context;
    List<Comic> comicList;

    public ComicAdapter(Context context, List<Comic> comicList) {
        this.context = context;
        this.comicList = comicList;
    }

    @NonNull
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.comic_item , parent , false);

        return new ComicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {

        holder.txt_comic.setText(comicList.get(position).getName());
        Picasso.with(context)
                .load(comicList.get(position).getImage())
                .placeholder(android.R.color.holo_orange_light)
                .into(holder.img_comic);

        holder.setiRecyclerOnClick(new IRecyclerOnClick() {
            @Override
            public void onClick(View view, int position) {

                Common.selected_comic = comicList.get(position);
                context.startActivity(new Intent(context , ChapterActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }
}
