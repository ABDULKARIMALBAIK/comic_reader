package com.example.asus.androidcomicreader.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.asus.androidcomicreader.Common.Common;
import com.example.asus.androidcomicreader.Model.Category;
import com.example.asus.androidcomicreader.R;
import com.example.asus.androidcomicreader.ViewHolder.OptionsViewHolder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsViewHolder> {

    Context context;
    List<Category> categoryList;
    SparseBooleanArray itemStateArray = new SparseBooleanArray();
    List<Category> selected_category = new ArrayList<>();

    public OptionsAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public OptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View  view = LayoutInflater.from(context).inflate(R.layout.check_item , parent , false);
        return new OptionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionsViewHolder holder, final int position) {

        holder.ckb_options.setText(categoryList.get(position).getName());
        holder.ckb_options.setChecked(itemStateArray.get(position));

        holder.ckb_options.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //int adapterPosition = position;
                itemStateArray.put(position , isChecked);
                if (isChecked)
                    selected_category.add(categoryList.get(position));
                else
                    selected_category.remove(categoryList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    public String getFilterArray() {

        List<Integer> id_selected = new ArrayList<>();
        for (Category category : selected_category) {
            id_selected.add(category.getID());

        }

        return new Gson().toJson(id_selected);
    }


}
