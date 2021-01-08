package com.maimemo.android.memonote.activity.base;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.maimemo.android.memonote.application.MemoNotesApplication;
import com.maimemo.android.memonote.db.AppDatabase;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

/**
 * @author zga
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected AppDatabase mAppDatabase;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MemoNotesApplication.getContextInstance();
        mAppDatabase = MemoNotesApplication.getAppDatabaseInstance();
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public AppDatabase getAppDatabase() {
        return mAppDatabase;
    }

    public void setAppDatabase(AppDatabase mAppDatabase) {
        this.mAppDatabase = mAppDatabase;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
