package com.maimemo.android.memonote.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.gson.Gson;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.maimemo.android.memonote.R;
import com.maimemo.android.memonote.activity.base.BaseActivity;
import com.maimemo.android.memonote.model.UserResModel;
import com.maimemo.android.memonote.model.UsersModel;
import com.maimemo.android.memonote.network.base.BaseObserver;
import com.maimemo.android.memonote.network.user.UserHttpEngine;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * @author zga
 */
public class LoginActivity extends BaseActivity {

    public final Integer INPUT_TEXT_LENGTH = 50;  //输入框长度
    private String TAG = "LoginActivity";

    @BindView(R.id.user_name_text)
    public EditText mUserNameEt;          //账号

    @BindView(R.id.password_text)
    public EditText mPassword;           // 密码

    @BindView(R.id.login_err)
    public TextView mLoginErr;         // 错误提示

    //todo :修改android官方
    @BindView(R.id.progressbar_circular)
    public AVLoadingIndicatorView mProgressBar;

    public Circle mCircleDrawable;

    @BindView(R.id.login_btn)
    public Button mLoginBtn;        // 登录按钮

    public boolean isHidden = false;

    public boolean isLogin = true;

    @BindView(R.id.toggle_pwd_btn)  // 密码切换按钮
    public ImageButton mShowPwdImgBtn;

    @BindColor(R.color.title_text_color)
    public int mNormalColor;

    @BindColor(R.color.login_err_text_color)
    public int mErrColor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mAppDatabase.userDao().getAllUsers().size() > 0) {
            Intent intent = new Intent(LoginActivity.this, NoteListActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);



        Toasty.Config.getInstance()
                .allowQueue(false)
                .apply();
        Observable<String> mUserNameObservable = RxTextView.textChanges(mUserNameEt)
                .map(CharSequence::toString);
        mUserNameObservable
                .subscribe(s -> {
                    if (s.trim().length() > INPUT_TEXT_LENGTH) {
                        mUserNameEt.setText(s.toString().substring(0, INPUT_TEXT_LENGTH));
                        mUserNameEt.setSelection(INPUT_TEXT_LENGTH);
                        Toasty.normal(LoginActivity.this, R.string.max_input_length_tip, Toasty.LENGTH_SHORT).show();


                    }
                });
        Observable<String> mPasswordObservable = RxTextView.textChanges(mPassword)
                .map(CharSequence::toString);
        mPasswordObservable
                .subscribe(s -> {
                    if (s.trim().length() > INPUT_TEXT_LENGTH) {
                        mPassword.setText(s.substring(0, INPUT_TEXT_LENGTH));
                        mPassword.setSelection(INPUT_TEXT_LENGTH);
                        Toasty.normal(LoginActivity.this, R.string.max_input_length_tip, Toasty.LENGTH_SHORT).show();
                    }
                });
        Observable
                .combineLatest(mUserNameObservable, mPasswordObservable, (username, password) -> (username.length() > 0 && password.length() > 0))
                .subscribe(aBoolean -> {

                    if (isLogin == false) {
                        changeLoginStatusInRight();
                    }
                    if (aBoolean) {
                        mLoginBtn.setBackgroundColor(getResources().getColor(R.color.login_btn_light_color));
                        mLoginBtn.setEnabled(true);
                    } else {
                        mLoginBtn.setBackgroundColor(getResources().getColor(R.color.login_btn_bg_color));
                        mLoginBtn.setEnabled(false);
                    }
                });
    }
    @OnTouch(R.id.user_name_text)
    public boolean toggleUser(View view, MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:

                Drawable drawable = mUserNameEt.getCompoundDrawables()[2];
                if (drawable != null && event.getRawX() >= (mUserNameEt.getRight() - drawable.getBounds().width())) {
                    if (drawable.getConstantState().equals(getResources().getDrawable(R.drawable.ic_eye).getConstantState())) {
                        mUserNameEt.setCompoundDrawablesWithIntrinsicBounds(null,
                                null, getResources().getDrawable(R.drawable.ic_eye_close), null);
                        mUserNameEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    else {
                        mUserNameEt.setCompoundDrawablesWithIntrinsicBounds(null,
                                null, getResources().getDrawable(R.drawable.ic_eye), null);
                        mUserNameEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    return false;
                }
                break;

        }
        return false;
    }
    @OnClick(R.id.toggle_pwd_btn)
    public void showPassword() {
        if (isHidden) {
            mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mShowPwdImgBtn.setImageResource(R.drawable.ic_eye);
        } else {
            mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mShowPwdImgBtn.setImageResource(R.drawable.ic_eye_close);
        }
        isHidden = !isHidden;
        mPassword.postInvalidate();
        //切换后将EditText光标置于末尾
        Spannable charSequence = mPassword.getText();
        if (charSequence != null) {
            Selection.setSelection(charSequence, charSequence.length());
        }

    }

    @OnClick(R.id.login_btn)
    void submitUserInfo() {

            mProgressBar.smoothToShow();
            String userName = mUserNameEt.getText().toString();
            String password = mPassword.getText().toString();
            UserHttpEngine.doLogin(userName, password, new BaseObserver<UsersModel>(mContext){
                @Override
                public void onNext(@NonNull UsersModel usersModel) {
                    //界面跳转 服务端返回信息存入数据库
                    Toasty.normal(LoginActivity.this, "登陆成功", Toasty.LENGTH_SHORT).show();
                    mAppDatabase.userDao().insertUser(usersModel);
                    Intent intent = new Intent(LoginActivity.this, NoteListActivity.class);
                    startActivity(intent);
                    mProgressBar.smoothToHide();
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
                @Override
                public void onError(@NonNull Throwable e) {

                    isLogin = false;
                    mProgressBar.smoothToHide();

                    if (e instanceof HttpException){
                        ResponseBody body = Objects.requireNonNull(((HttpException) e).response()).errorBody();
                        try {
                            Gson gson = new Gson();
                            UserResModel result = gson.fromJson(body.string(), UserResModel.class);

                            changeLoginStatusInErr(result);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            });


    }

    private void changeLoginStatusInErr(UserResModel result) {
        mPassword.setTextColor(mErrColor);
        mUserNameEt.setTextColor(mErrColor);
        mLoginErr.setVisibility(View.VISIBLE);
        Log.i(TAG, "changeLoginStatusInErr: "+result.getMessage());
        mLoginErr.setText(result.getMessage());
    }

    private void changeLoginStatusInRight() {
        mPassword.setTextColor(mNormalColor);
        mUserNameEt.setTextColor(mNormalColor);
        mLoginErr.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

