package com.example.asus.androidcomicreader.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.androidcomicreader.Interface.IRecyclerOnClick;
import com.example.asus.androidcomicreader.R;

public class ComicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView img_comic;
    public TextView txt_comic;

    private IRecyclerOnClick iRecyclerOnClick;

    public ComicViewHolder(View itemView) {
        super(itemView);

        img_comic = (ImageView)itemView.findViewById(R.id.img_comic);
        txt_comic = (TextView) itemView.findViewById(R.id.txt_comic);
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
