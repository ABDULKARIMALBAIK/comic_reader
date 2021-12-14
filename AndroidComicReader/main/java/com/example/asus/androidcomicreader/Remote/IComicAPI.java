package com.example.asus.androidcomicreader.Remote;

import com.example.asus.androidcomicreader.Model.Banner;
import com.example.asus.androidcomicreader.Model.Category;
import com.example.asus.androidcomicreader.Model.Chapter;
import com.example.asus.androidcomicreader.Model.Comic;
import com.example.asus.androidcomicreader.Model.Link;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IComicAPI {

    @GET("banner")
    Observable<List<Banner>> getBannerList();

    @GET("comic")
    Observable<List<Comic>> getComicList();

    @GET("chapter/{comicid}")
    Observable<List<Chapter>> getChapterList(@Path("comicid")int comicid);

    @GET("links/{chapterid}")
    Observable<List<Link>> getImagesList(@Path("chapterid")int chapterid);

    @GET("categories")
    Observable<List<Category>> getCategoryList();

    @FormUrlEncoded
    @POST("filter")
    Observable<List<Comic>> getFilteredComic(@Field("data") String data);

    @FormUrlEncoded
    @POST("search")
    Observable<List<Comic>> getSearchedComic(@Field("search") String search);
}
