package com.maimemo.android.memonote.network.notes;

import com.maimemo.android.memonote.model.NoteResModel;
import com.maimemo.android.memonote.model.NoteSearchResModel;
import com.maimemo.android.memonote.model.NotesModel;

import java.util.Hashtable;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NotesService {
    // 获取(全量)或搜素笔记
    @GET("notes")
    Observable<NoteResModel> getNotes(@Body Map body);

    // 获取(差量)笔记
    @POST("notes/sync")
    Observable<NoteResModel> uploadLocalNotes(@Body Map notes);

    // 创建笔记
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("note")
    Observable<NotesModel> createNote(@Body NotesModel note);

    // 获取笔记
    @GET("notes/{id}")
    Observable<NotesModel> getNote(@Path("id") String id);


    @GET("notes")
    Observable<NoteSearchResModel> searchNotes(@Query("skip") Integer skip,@Query("limit") Integer limit, @Query("search") String search);
    // 删除笔记
    @DELETE("notes/{id}")
    Observable<NoteResModel> delNote(@Path("id") String id);

    //更新笔记
    @PATCH("notes/{id}")
    Observable<NotesModel> updateNote(@Path("id") String id,@Body Hashtable note);


    // 收藏笔记
//    @POST("notes/{id}/favorite")
//    Observable<Object> subscribeNote(@Path("id") String id);
    //取消收藏
//    @DELETE("notes/{id}/favorite")
//    Observable<Object> cancelNote(@Path("id") String id);
}
