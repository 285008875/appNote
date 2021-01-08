package com.maimemo.android.memonote.network.user;

import com.maimemo.android.memonote.model.UsersModel;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
/**
 * @author zga
 */
public interface UserService {
    /**
     * 提交用户账号密码
     * @return 服务端返回登录是否成功
     */

    @Headers({"Content-Type:application/json;charset=UTF-8", "Accept:application/json"})
    @POST("auth/login")
    Observable<UsersModel> doLogin(@Body Map user);

    /**
     * 退出登录
     * @return
     */
    @POST("auth/logout")
    Observable<Object> doLogOut();
}
