package com.maimemo.android.memonote.network.user;

import androidx.collection.ArrayMap;

import com.maimemo.android.memonote.model.UsersModel;
import com.maimemo.android.memonote.network.RetrofitServiceManager;

import java.util.Map;

import io.reactivex.SingleObserver;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserHttpEngine {
    private static UserService userService = RetrofitServiceManager.getInstance().create(UserService.class);

    /*
     * 登录
     * **/
    public static void doLogin(String userName,String password, Observer<UsersModel> observer) {
        Map<String, Object> map = new ArrayMap<>();
        map.put("identity", userName);
        map.put("password",password);

        setSubscribe(userService.doLogin(map),observer);
    }

    /**
     *退出登录
     * @param observer
     */
    public static void doLogOut(Observer<Object> observer) {
        setSubscribe(userService.doLogOut(),observer);
    }


    private static <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(observer);

    }

    private static <T> void setSubscribe(Single<T> observable, SingleObserver<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe();

    }
}
