package com.maimemo.android.memonote.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public  class ListConverter {
    @TypeConverter
    public static List<String> revertList( String tag ) {
        Gson gson = new Gson();
        List<String> tagList = gson.fromJson(tag, new TypeToken<List<String>>() {}.getType());
        return tagList;
    }

    @TypeConverter
    public static String converterList(List<String> tag) {
        Gson gson = new Gson();
        return gson.toJson(tag);

    }
}