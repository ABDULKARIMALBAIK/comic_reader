package com.example.asus.androidcomicreader.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.androidcomicreader.Common.Common;
import com.example.asus.androidcomicreader.Interface.IRecyclerOnClick;
import com.example.asus.androidcomicreader.Model.Chapter;
import com.example.asus.androidcomicreader.R;
import com.example.asus.androidcomicreader.ViewDetail;
import com.example.asus.androidcomicreader.ViewHolder.ChapterViewHolder;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterViewHolder> {

    Context context;
    List<Chapter> chapterList;

    public ChapterAdapter(Context context, List<Chapter> chapterList) {
        this.context = context;
        this.chapterList = chapterList;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.chapter_item , parent , false);

        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {

        holder.txt_chapter_number.setText(new StringBuilder(chapterList.get(position).getName()));

        Common.selected_chapter = chapterList.get(position);
        Common.chapter_index = position;

        holder.setiRecyclerOnClick(new IRecyclerOnClick() {
            @Override
            public void onClick(View view, int position) {

                Common.selected_chapter = chapterList.get(position);
                Common.chapter_index = position;

                context.startActivity(new Intent(context , ViewDetail.class));
            }
        });


    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }
}
