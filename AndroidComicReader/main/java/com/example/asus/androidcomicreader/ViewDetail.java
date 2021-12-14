package com.example.asus.androidcomicreader;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.androidcomicreader.Adapter.ViewPagerAdapter;
import com.example.asus.androidcomicreader.Common.Common;
import com.example.asus.androidcomicreader.Model.Link;
import com.example.asus.androidcomicreader.Remote.IComicAPI;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ViewDetail extends AppCompatActivity {

    IComicAPI iComicAPI;
    ViewPager viewPager;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    TextView txt_chapter_name;
    View back , next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail);

        //Init views
        iComicAPI = Common.getAPI();
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        txt_chapter_name = (TextView)findViewById(R.id.txt_chapter_name);
        back = (View)findViewById(R.id.chapter_back);
        next = (View)findViewById(R.id.chapter_next);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.chapter_index == 0){  //If user in first chapter but press back
                    Toast.makeText(ViewDetail.this, "You are reading first chapter", Toast.LENGTH_SHORT).show();
                }
                else {

                    Common.chapter_index--;
                    if (Common.isConnectionToInternet(ViewDetail.this))
                        fetchLinks(Common.chapterList.get(Common.chapter_index).getID());
                    else
                        Toast.makeText(ViewDetail.this, "Please check your connection internet !", Toast.LENGTH_SHORT).show();
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.chapter_index == Common.chapterList.size() - 1){  //If user in last chapter but press next
                    Toast.makeText(ViewDetail.this, "You are reading last chapter", Toast.LENGTH_SHORT).show();
                }
                else {

                    Common.chapter_index++;
                    if (Common.isConnectionToInternet(ViewDetail.this))
                        fetchLinks(Common.chapterList.get(Common.chapter_index).getID());
                    else
                        Toast.makeText(ViewDetail.this, "Please check your connection internet !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fetchLinks(Common.selected_chapter.getID());

    }

    private void fetchLinks(int id) {

        compositeDisposable.add(iComicAPI.getImagesList(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Link>>() {
            @Override
            public void accept(List<Link> links) throws Exception {

                displayImage(links);

                txt_chapter_name.setText(Common.formatString(Common.selected_chapter.getName()));

                setFlipPageTransformer();

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(ViewDetail.this, "There is not images exists !", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void setFlipPageTransformer() {

        //Create Book Flip Page
        BookFlipPageTransformer bookFlipPageTransformer = new BookFlipPageTransformer();
        bookFlipPageTransformer.setScaleAmountPercent(10f);
        viewPager.setPageTransformer(true , bookFlipPageTransformer);
    }

    private void displayImage(List<Link> links) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(ViewDetail.this , links);
        viewPager.setAdapter(adapter);
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
