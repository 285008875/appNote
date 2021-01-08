package com.maimemo.android.memonote.network.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.blankj.utilcode.util.NetworkUtils;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class BaseObserver<T> implements Observer<T> {
    private Context mContext;

    public BaseObserver() {

    }

    public BaseObserver(Context context) {

       this.mContext = context;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        if (!NetworkUtils.isConnected()){
            d.dispose();
        }
    }

    @Override
    public void onNext(@NonNull T t) {

    }

    @Override
    public void onError(@NonNull Throwable e) {
        try {
            NetworkError.error(mContext, e);
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public void onComplete() {

    }
}
