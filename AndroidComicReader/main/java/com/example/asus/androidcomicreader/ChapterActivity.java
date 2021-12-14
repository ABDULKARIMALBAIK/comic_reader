package com.example.asus.androidcomicreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.androidcomicreader.Adapter.ChapterAdapter;
import com.example.asus.androidcomicreader.Common.Common;
import com.example.asus.androidcomicreader.Model.Chapter;
import com.example.asus.androidcomicreader.Remote.IComicAPI;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChapterActivity extends AppCompatActivity {

    IComicAPI comicAPI;
    RecyclerView recycler_chapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    TextView txt_chapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        comicAPI = Common.getAPI();

        //Setup ToolBar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(Common.selected_comic.getName());
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //Go back
            }
        });
        setSupportActionBar(toolbar);

        //Setup Recycler_Chapter
        recycler_chapter = (RecyclerView)findViewById(R.id.recycler_chapter);
        recycler_chapter.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_chapter.setLayoutManager(layoutManager);
        recycler_chapter.addItemDecoration(new DividerItemDecoration(this , layoutManager.getOrientation()));

        //views
        txt_chapter = (TextView)findViewById(R.id.txt_chapter);

        if (Common.isConnectionToInternet(this)){

            fetchChapter();
        }
        else
            Toast.makeText(ChapterActivity.this, "Please check your connection internet !", Toast.LENGTH_SHORT).show();

    }

    private void fetchChapter() {

        compositeDisposable.add(comicAPI.getChapterList(Common.selected_comic.getID())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Chapter>>() {
            @Override
            public void accept(List<Chapter> chapters) throws Exception {

                Common.chapterList = chapters;  //Save Chapter to  back,next
                displayChpater(chapters);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(ChapterActivity.this, "There is not Chapters exists !", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void displayChpater(List<Chapter> chapters) {

        ChapterAdapter adapter = new ChapterAdapter(this , chapters);
        recycler_chapter.setAdapter(adapter);
        txt_chapter.setText(new StringBuilder("CHAPTER (").append(chapters.size()).append(")"));
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
