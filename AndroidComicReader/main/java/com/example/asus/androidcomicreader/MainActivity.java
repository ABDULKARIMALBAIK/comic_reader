package com.example.asus.androidcomicreader;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.androidcomicreader.Adapter.ComicAdapter;
import com.example.asus.androidcomicreader.Adapter.ComicSliderAdapter;
import com.example.asus.androidcomicreader.Common.Common;
import com.example.asus.androidcomicreader.Model.Banner;
import com.example.asus.androidcomicreader.Model.Comic;
import com.example.asus.androidcomicreader.Remote.IComicAPI;
import com.example.asus.androidcomicreader.Service.PicassoImageLoadingService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ss.com.bannerslider.Slider;

public class MainActivity extends AppCompatActivity {

    Slider slider;
    RecyclerView recycler_comic;
    TextView txt_comic;
    ImageView btn_search;

    SwipeRefreshLayout swipeRefreshLayout;
    IComicAPI comicAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        comicAPI = Common.getAPI();

        //Views
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_blue_dark,
                R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Common.isConnectionToInternet(MainActivity.this)){

                    fetchBanner();
                    fetchComic();
                }
                else
                    Toast.makeText(MainActivity.this, "Please check your connection internet !", Toast.LENGTH_SHORT).show();
            }
        });

        btn_search = (ImageView)findViewById(R.id.btn_filter);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this , CategoryFilter.class));
            }
        });

        slider = (Slider)findViewById(R.id.banner_slider);
        Slider.init(new PicassoImageLoadingService(this));

        recycler_comic = (RecyclerView)findViewById(R.id.recycler_comic);
        recycler_comic.setHasFixedSize(true);
        recycler_comic.setLayoutManager(new GridLayoutManager(this , 2));

        txt_comic = (TextView)findViewById(R.id.txt_comic);

        //Default , load first time
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                if (Common.isConnectionToInternet(MainActivity.this)){

                    fetchBanner();
                    fetchComic();
                }
                else
                    Toast.makeText(MainActivity.this, "Please check your connection internet !", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void fetchComic() {

        compositeDisposable.add(comicAPI.getComicList()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<List<Comic>>() {
                                    @Override
                                    public void accept(List<Comic> comics) throws Exception {

                                        txt_comic.setText(new StringBuilder("NEW COMIC (").append(comics.size()).append(")"));
                                        displayComic(comics);
                                        swipeRefreshLayout.setRefreshing(false);

                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {

                                        swipeRefreshLayout.setRefreshing(false);
                                        Toast.makeText(MainActivity.this, "Server is close !", Toast.LENGTH_SHORT).show();
                                    }
                                }));
    }

    private void displayComic(List<Comic> comics) {

        ComicAdapter adapter = new ComicAdapter(this , comics);
        recycler_comic.setAdapter(adapter);
    }

    private void fetchBanner() {
        
        compositeDisposable.add(comicAPI.getBannerList()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<List<Banner>>() {
                                    @Override
                                    public void accept(List<Banner> banners) throws Exception {

                                        displayBanner(banners);

                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Toast.makeText(MainActivity.this, "Server is close !\n" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }));
    }

    private void displayBanner(List<Banner> banners) {

        slider.setAdapter(new ComicSliderAdapter(banners));

    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
