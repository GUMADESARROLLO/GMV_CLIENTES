package com.tiendaumk.utils;

import android.content.Context;
import android.content.res.TypedArray;

import com.tiendaumk.model.News;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.tiendaumk.R;
public class DataGenerator {
    private static Random r = new Random();
    public static int randInt(int max) {
        int min = 0;
        return r.nextInt((max - min) + 1) + min;
    }
    /**
     * Generate dummy data News
     *
     * @param ctx   android context
     * @param count total result data
     * @return list of object
     */

    public static List<News> getNewsData(Context ctx, int count) {

        List<News> items = new ArrayList<>();

        List<Integer> images = getAllImages(ctx);
        List<String> titles = getStringsMedium(ctx);
        List<String> full_date = getFullDate(ctx);
        String cat[] = ctx.getResources().getStringArray(R.array.news_category);

        for (int i = 0; i < count; i++) {
            News obj = new News();
            obj.image = images.get(getRandomIndex(images.size()));
            obj.title = titles.get(getRandomIndex(titles.size()));
            obj.subtitle = cat[getRandomIndex(cat.length)];
            obj.date = full_date.get(getRandomIndex(full_date.size()));
            items.add(obj);
        }
        return items;
    }
    public static List<Integer> getAllImages(Context ctx) {
        List<Integer> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.all_images);
        for (int i = 0; i < drw_arr.length(); i++) {
            items.add(drw_arr.getResourceId(i, -1));
        }
        Collections.shuffle(items);
        return items;
    }
    public static List<String> getStringsMedium(Context ctx) {
        List<String> items = new ArrayList<>();
        String name_arr[] = ctx.getResources().getStringArray(R.array.strings_medium);
        for (String s : name_arr) items.add(s);
        Collections.shuffle(items);
        return items;
    }
    public static List<String> getFullDate(Context ctx) {
        List<String> items = new ArrayList<>();
        String name_arr[] = ctx.getResources().getStringArray(R.array.full_date);
        for (String s : name_arr) items.add(s);
        Collections.shuffle(items);
        return items;
    }
    private static int getRandomIndex(int max) {
        return r.nextInt(max - 1);
    }
}
