package com.example.asus.androidcomicreader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.androidcomicreader.Adapter.ComicAdapter;
import com.example.asus.androidcomicreader.Adapter.OptionsAdapter;
import com.example.asus.androidcomicreader.Common.Common;
import com.example.asus.androidcomicreader.Model.Category;
import com.example.asus.androidcomicreader.Model.Comic;
import com.example.asus.androidcomicreader.Remote.IComicAPI;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CategoryFilter extends AppCompatActivity {

    Button btn_filter , btn_search;
    IComicAPI comicAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView recycler_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_filter);

        comicAPI = Common.getAPI();


        if (Common.isConnectionToInternet(this)){

            new Handler().post(new Runnable() {
                @Override
                public void run() {

                    if (Common.isConnectionToInternet(CategoryFilter.this))
                        fetchCategory();  //Loaf all categories from server
                }
            });
        }
        else
            Toast.makeText(this, "Please check your connection !", Toast.LENGTH_SHORT).show();


        btn_filter = (Button)findViewById(R.id.btn_filter);
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectionToInternet(CategoryFilter.this))
                    showOptionDialog();
                else
                    Toast.makeText(CategoryFilter.this, "Please check your connection !", Toast.LENGTH_SHORT).show();
            }
        });

        btn_search = (Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSearchDialog();
            }
        });

        recycler_filter = (RecyclerView)findViewById(R.id.recycler_filter);
        recycler_filter.setHasFixedSize(true);
        recycler_filter.setLayoutManager(new GridLayoutManager(this , 2));

    }

    private void showSearchDialog() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setTitle("Search Comic");

        View search_layout = getLayoutInflater().inflate(R.layout.dialog_search , null);
        final EditText edt_search = (EditText)search_layout.findViewById(R.id.edt_search);

        alertDialog.setView(search_layout);
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                fetchSearchComic(edt_search.getText().toString());
            }
        });
        alertDialog.show();

    }

    private void fetchSearchComic(String search) {

        compositeDisposable.add(comicAPI.getSearchedComic(search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Comic>>() {
                    @Override
                    public void accept(List<Comic> comics) throws Exception {

                        recycler_filter.setVisibility(View.VISIBLE);
                        recycler_filter.setAdapter(new ComicAdapter(CategoryFilter.this , comics));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        recycler_filter.setVisibility(View.INVISIBLE);
                        Toast.makeText(CategoryFilter.this, "No Comic found", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void showOptionDialog() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setTitle("Select Category");

        View filter_layout = getLayoutInflater().inflate(R.layout.dialog_options , null);
        RecyclerView recycler_options = (RecyclerView)filter_layout.findViewById(R.id.recycler_options);
        recycler_options.setHasFixedSize(true);
        recycler_options.setLayoutManager(new LinearLayoutManager(this));
        final OptionsAdapter adapter = new OptionsAdapter(this , Common.categories);
        recycler_options.setAdapter(adapter);

        alertDialog.setView(filter_layout);
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("FILTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                fetchFilterCategory(adapter.getFilterArray());
            }
        });

        alertDialog.show();
    }

    private void fetchFilterCategory(String data) {

        compositeDisposable.add(comicAPI.getFilteredComic(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Comic>>() {
                    @Override
                    public void accept(List<Comic> comics) throws Exception {

                        recycler_filter.setVisibility(View.VISIBLE);
                        recycler_filter.setAdapter(new ComicAdapter(CategoryFilter.this , comics));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        recycler_filter.setVisibility(View.INVISIBLE);
                        Toast.makeText(CategoryFilter.this, "No Comic found", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void fetchCategory() {

        compositeDisposable.add(comicAPI.getCategoryList()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Category>>() {
            @Override
            public void accept(List<Category> categories) throws Exception {

                Common.categories = categories;
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(CategoryFilter.this, "Server is cloes !", Toast.LENGTH_SHORT).show();
            }
        }));
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
