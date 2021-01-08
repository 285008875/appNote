package com.maimemo.android.memonote.network.notes;

import androidx.collection.ArrayMap;

import com.maimemo.android.memonote.application.MemoNotesApplication;
import com.maimemo.android.memonote.model.NotesModel;
import com.maimemo.android.memonote.network.RetrofitServiceManager;
import com.maimemo.android.memonote.network.base.NetworkError;

import java.util.Hashtable;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NotesHttpEngine {
    private static NotesService notesService = RetrofitServiceManager.getInstance().create(NotesService.class);

    //获取笔记
    public static void getNotes(int skip, int limit, String search, Observer observer) {

        Map<String, Object> map = new ArrayMap<>();
        map.put("skip", skip);
        map.put("limit", limit);
        map.put("search", search);
        setSubscribe(notesService.getNotes(map), observer);
    }

    //获取笔记 重载
    public static void getNotes(String search, Observer observer) {
        Map<String, Object> map = new ArrayMap<>();
        map.put("search", search);
        setSubscribe(notesService.getNotes(map), observer);
    }

    //获取笔记 重载
    public static void getNotes(int skip, int limit, Observer observer) {
        Map<String, Object> map = new ArrayMap<>();
        map.put("skip", skip);
        map.put("limit", limit);
        setSubscribe(notesService.getNotes(map), observer);
    }

    //获取笔记 重载
    public static void getNotes(Observer observer) {
        Map<String, Object> map = new ArrayMap<>();
        setSubscribe(notesService.getNotes(map), observer);
    }

    //差量获取笔记
    public static void uploadLocalNotes(Map<String, Object> map, Observer observer) {
        setSubscribe(notesService.uploadLocalNotes(map), observer);
    }

    public static void createNote(NotesModel note, Observer observer) {
        setSubscribe(notesService.createNote(note), observer);
    }

    public static void getNote(String id, Observer observer) {
        setSubscribe(notesService.getNote(id), observer);
    }

    public static void searchNotes( Integer skip,Integer limit,String search, Observer observer) {

        setSubscribe(notesService.searchNotes(skip,limit,search), observer);
    }

    public static void delNote(String id, Observer observer) {
        setSubscribe(notesService.delNote(id), observer);
    }

    public static void updateNote(String id, Hashtable note, Observer observer) {
        setSubscribe(notesService.updateNote(id, note), observer);
    }

//    public static void subscribeNote(String id,Observer observer){
//        setSubscribe(notesService.subscribeNote(id),observer);
//    }

    //    public static void cancelNote(String id,Observer observer){
//        setSubscribe(notesService.subscribeNote(id),observer);
//    }
    private static <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(observer);
    }

}
