package com.maimemo.android.memonote.application;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.maimemo.android.memonote.db.AppDatabase;
import com.maimemo.android.memonote.model.UsersModel;

import java.util.ArrayList;

import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

/**
 * @author zga28
 * @version v1.0
 * @update
 */
public class MemoNotesApplication extends Application {

    private static MemoNotesApplication mContext;
    private static AppDatabase mAppDatabase;
    private static String userId;
    private  static String userToken =null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();//这里处理所有的Rxjava异常
            }
        });
    }
    public static Context getContextInstance(){

        return  mContext;
    }

    public static AppDatabase getAppDatabaseInstance() {
        if (mAppDatabase==null){
            mAppDatabase = Room.databaseBuilder(mContext, AppDatabase.class, "android_room_dev.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return mAppDatabase;
    }
    public static String getUserTokenInstanse(){
        if (userToken ==null||userId==null){
             ArrayList<UsersModel>  userList= (ArrayList<UsersModel>) getAppDatabaseInstance().userDao().getAllUsers();
             if (userList.size()<=0){
                 userToken = null;
             }else {
                 userToken = userList.get(0).getJwt();
                 userId = userList.get(0).getId();
             }
        }
        return userToken;
    }
    public static String getUserId(){
        if (userToken ==null||userId==null){
            ArrayList<UsersModel>  userList= (ArrayList<UsersModel>) getAppDatabaseInstance().userDao().getAllUsers();
            if (userList.size()<=0){
                userToken = null;
            }else {
                userToken = userList.get(0).getJwt();
                userId = userList.get(0).getId();
            }
        }
        return userId;
    }
    public static void updateUserToken(){

        userToken = null;
    }


}
