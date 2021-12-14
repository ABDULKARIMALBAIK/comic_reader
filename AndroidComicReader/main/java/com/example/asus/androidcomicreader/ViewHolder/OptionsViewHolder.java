package com.example.asus.androidcomicreader.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.example.asus.androidcomicreader.Common.Common;
import com.example.asus.androidcomicreader.R;

public class OptionsViewHolder extends RecyclerView.ViewHolder {

    public CheckBox ckb_options;

    public OptionsViewHolder(View itemView) {
        super(itemView);

        ckb_options = (CheckBox)itemView.findViewById(R.id.check_options);
        //Common.adapterPositionOption = getAdapterPosition();
    }
}
