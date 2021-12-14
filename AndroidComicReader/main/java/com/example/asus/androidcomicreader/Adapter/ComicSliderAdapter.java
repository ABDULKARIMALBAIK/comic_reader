package com.example.asus.androidcomicreader.Adapter;

import com.example.asus.androidcomicreader.Model.Banner;

import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class ComicSliderAdapter extends SliderAdapter {

    private List<Banner> bannerList;

    public ComicSliderAdapter(List<Banner> bannerList) {
        this.bannerList = bannerList;
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {

        imageSlideViewHolder.bindImageSlide(bannerList.get(position).getLink());
    }
}
