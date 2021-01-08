package com.maimemo.android.memonote.network.base;

import android.content.Context;
import android.content.Intent;

import com.maimemo.android.memonote.activity.LoginActivity;

import java.io.IOException;

import es.dmoral.toasty.Toasty;

public class NetworkError {
    /**
     * @param context 可以用于跳转Activity等操作
     */
    public static void error(Context context, Throwable throwable) throws IOException {
        RetrofitException.ResponseThrowable responseThrowable = RetrofitException.retrofitException(throwable);
        // 此处可以通过判断错误代码来实现根据不同的错误代码做出相应的反应
        switch (responseThrowable.msgCode) {
            case RetrofitException.ERROR.UNKNOWN:
            case RetrofitException.ERROR.PARSE_ERROR:
            case RetrofitException.ERROR.NETWORD_ERROR:
            case RetrofitException.ERROR.HTTP_ERROR:
            case RetrofitException.ERROR.SSL_ERROR:

                Toasty.normal(context, responseThrowable.getMessage() , Toasty.LENGTH_SHORT).show();
                break;
            case -1:
                // 跳转到登陆页面
                context.startActivity(new Intent(context, LoginActivity.class));
                // 结束除LoginActivity之外的所有Activity
//                AppManager.finishAllActivity(LoginActivity.class);
                break;
            default:
                Toasty.normal(context, responseThrowable.serverMessage, Toasty.LENGTH_SHORT).show();
                break;
        }
    }
}
