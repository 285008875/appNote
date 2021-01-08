package com.maimemo.android.memonote.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.maimemo.android.memonote.R;
import com.maimemo.android.memonote.activity.base.BaseActivity;
import com.maimemo.android.memonote.adapter.SearchableAdapter;
import com.maimemo.android.memonote.application.MemoNotesApplication;
import com.maimemo.android.memonote.model.NoteSearchResModel;
import com.maimemo.android.memonote.model.NotesModel;
import com.maimemo.android.memonote.network.base.BaseObserver;
import com.maimemo.android.memonote.network.notes.NotesHttpEngine;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchableActivity extends BaseActivity {
    private static final String TAG = "SearchableActivity";
    @BindView(R.id.search_note)
    public EditText mSearchEt;

    @BindView(R.id.note_list_rv)
    public RecyclerView mRecyclerView;

    @BindView(R.id.cancel_btn)
    public Button mCancelBtn;

    private SearchableAdapter mAdapter;
    private String mHighLightString;
    private View noSearchResultView;
    private String mUserId;
    private Integer mSkip = 0;
    private Integer mLimit = 30;
    private Integer mTotal;
    private int mCurrentCounter;

    @SuppressLint({"ClickableViewAccessibility", "CheckResult"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mUserId = MemoNotesApplication.getUserId();
        mAdapter = new SearchableAdapter(mUserId, null);
        mAdapter.bindToRecyclerView(mRecyclerView);
        mAdapter.setEmptyView(R.layout.no_search_result, mRecyclerView);
        mAdapter.disableLoadMoreIfNotFullPage();
        mAdapter.openLoadAnimation();
        mAdapter.setEnableLoadMore(false);
        mRecyclerView.setAdapter(mAdapter);
        mSearchEt.setFocusable(true);
        mSearchEt.requestFocus();
        Toasty.Config.getInstance().allowQueue(false).apply();
        // 监听点击跳转
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                NotesModel item = (NotesModel) adapter.getItem(position);

                if (item != null && item.getAuthor().getId().toString().equals(mUserId)) {

                    Intent intent = new Intent(SearchableActivity.this, CreateNoteActivity.class);
                    Gson gson=new Gson();
                    intent.putExtra("note", gson.toJson(item));
                    startActivity(intent);

                } else {

                    Intent intent = new Intent(SearchableActivity.this, PreviewNoteActivity.class);
                    Gson gson=new Gson();
                    intent.putExtra("note",  gson.toJson(item));
                    startActivity(intent);
                }
            }
        });
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (null != SearchableActivity.this.getCurrentFocus()) {
                    /**
                     * 点击空白位置 隐藏软键盘
                     */
                    InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    return mInputMethodManager.hideSoftInputFromWindow(SearchableActivity.this.getCurrentFocus().getWindowToken(), 0);
                }
                return false;
            }


        });
        mAdapter.setOnLoadMoreListener(() -> mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= mTotal) {
                    mAdapter.loadMoreEnd();
                } else {
                    NotesHttpEngine.searchNotes(mSkip += 30, mLimit, mHighLightString, new BaseObserver<NoteSearchResModel>(mContext) {

                        @Override
                        public void onNext(@NonNull NoteSearchResModel noteSearchResModel) {
                            mSkip = noteSearchResModel.getPagination().getSkip();
                            mLimit = noteSearchResModel.getPagination().getLimit();
                            mTotal = noteSearchResModel.getPagination().getTotal();
                            mCurrentCounter = mCurrentCounter + noteSearchResModel.getItems().size();
                            mAdapter.addData(noteSearchResModel.getItems());
                            mAdapter.loadMoreComplete();
                        }
                        @Override
                        public void onError(@NonNull Throwable e) {
                            mAdapter.loadMoreFail();
                        }
                    });
                }
            }

        }, 1000), mRecyclerView);
        Observable<CharSequence> searchEtObservable = RxTextView.textChanges(mSearchEt)
                .throttleLast(300, TimeUnit.MILLISECONDS);


        searchEtObservable
                .map(CharSequence::toString)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .flatMap((Function<String, ObservableSource<NoteSearchResModel>>) searchContent -> {
                    if (TextUtils.isEmpty(searchContent.trim())) {
                        return Observable.just(new NoteSearchResModel());
                    } else {
                        return Observable.create((ObservableOnSubscribe<NoteSearchResModel>) emitter -> {
                            mHighLightString = searchContent;

                            if (searchContent.equals("")) {
                                return;
                            } else {
                                mSkip = 0;
                                mLimit = 30;
                                NotesHttpEngine.searchNotes(mSkip, mLimit, searchContent, new BaseObserver<NoteSearchResModel>(this) {

                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {
                                        super.onSubscribe(d);
                                        if (d.isDisposed()){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toasty.normal(mContext, "网络无状态", Toasty.LENGTH_LONG).show();
                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onNext(NoteSearchResModel searchResult) {

                                        emitter.onNext(searchResult);
                                    }
                                });

                            }
                        });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(noteSearchResModel -> {
                    if (noteSearchResModel.getItems() == null) {
                        mAdapter.setNewData(null);
//                            mAdapter.setEmptyView(R.layout.no_search_result,mRecyclerView);
                    } else {
                        mAdapter.setHighLightString(mHighLightString); //传递高亮词汇
                        mLimit = noteSearchResModel.getPagination().getLimit();
                        mSkip = noteSearchResModel.getPagination().getSkip();
                        mTotal = noteSearchResModel.getPagination().getTotal();
                        mCurrentCounter = noteSearchResModel.getItems().size();
                        mAdapter.setNewData(noteSearchResModel.getItems());
                        mAdapter.loadMoreComplete();

                    }

                });


    }
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_SPACE) {
//            Log.d("test", "[Dispatch] Space bar pressed! " + event);
//            return true;
//        }
//        return super.dispatchKeyEvent(event);
//    }

    @OnClick(R.id.cancel_btn)
    public void backToNoteList() {

        finish();
    }



}
