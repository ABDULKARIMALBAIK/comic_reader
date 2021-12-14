package com.example.asus.androidcomicreader.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.asus.androidcomicreader.Interface.IRecyclerOnClick;
import com.example.asus.androidcomicreader.R;

public class ChapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txt_chapter_number;

    private IRecyclerOnClick iRecyclerOnClick;

    public ChapterViewHolder(View itemView) {
        super(itemView);

        txt_chapter_number = (TextView)itemView.findViewById(R.id.txt_chapter_number);
        itemView.setOnClickListener(this);
    }

    public void setiRecyclerOnClick(IRecyclerOnClick iRecyclerOnClick) {
        this.iRecyclerOnClick = iRecyclerOnClick;
    }

    @Override
    public void onClick(View v) {

        iRecyclerOnClick.onClick(v , getAdapterPosition());
    }
}
