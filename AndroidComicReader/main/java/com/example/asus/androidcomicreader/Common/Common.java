package com.example.asus.androidcomicreader.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.asus.androidcomicreader.Model.Category;
import com.example.asus.androidcomicreader.Model.Chapter;
import com.example.asus.androidcomicreader.Model.Comic;
import com.example.asus.androidcomicreader.Remote.IComicAPI;
import com.example.asus.androidcomicreader.Remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

public class Common {

    public static Comic selected_comic = null;
    public static Chapter selected_chapter = null;
    public static int chapter_index = -1;
    public static List<Chapter> chapterList = new ArrayList<>();

    public static int adapterPositionOption;
    public static List<Category> categories = new ArrayList<>();

    public static IComicAPI getAPI(){

        return RetrofitClient.getInstance().create(IComicAPI.class);
    }

    public static boolean isConnectionToInternet(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null){

            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null){

                for (int i = 0; i < info.length; i++) {

                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    public static String formatString(String name) {

        //If chapteris too long , just SubString
        StringBuilder finalResult = new StringBuilder(name.length() > 15 ? name.substring(0,15) + "..." : name);
        return finalResult.toString();
    }
}
