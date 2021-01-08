package com.maimemo.android.memonote.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.blankj.utilcode.util.NetworkUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.Gson;
import com.maimemo.android.memonote.R;
import com.maimemo.android.memonote.activity.base.BaseActivity;
import com.maimemo.android.memonote.adapter.DiffNoteCallBack;
import com.maimemo.android.memonote.adapter.NotesListAdapter;
import com.maimemo.android.memonote.adapter.VerticalSwipeRefreshLayout;
import com.maimemo.android.memonote.application.MemoNotesApplication;
import com.maimemo.android.memonote.model.AuthorsModel;
import com.maimemo.android.memonote.model.NoteResModel;
import com.maimemo.android.memonote.model.NotesModel;
import com.maimemo.android.memonote.network.base.BaseObserver;
import com.maimemo.android.memonote.network.notes.NotesHttpEngine;
import com.maimemo.android.memonote.network.user.UserHttpEngine;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * @author zga
 */
public class NoteListActivity extends BaseActivity {
    public static final String TAG = "NoteListActivity";
    @BindView(R.id.logout)
    public Button mLogOutBtn;

    @BindView(R.id.search)
    public Button mSearchBtn;

    @BindView(R.id.note_list_rv)
    public RecyclerView mRecyclerView;


    @BindView(R.id.easy_layout)
    public VerticalSwipeRefreshLayout mVerticalSwipeRefreshLayout; //上拉加载下拉刷新

    private NotesListAdapter mAdapter;

    private NoteResModel mInitData;// 页面数据


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        Spannable buttonLabel = initView();
        mSearchBtn.setText(buttonLabel);
        mAdapter = new NotesListAdapter(null);
        mAdapter.openLoadAnimation();
        mAdapter.setEmptyView(R.layout.empty_view, (ViewGroup) mRecyclerView.getParent());

        mRecyclerView.setMotionEventSplittingEnabled(false);
        mRecyclerView.setAdapter(mAdapter);
        fetchInitData(false);
        initEvent();
        mVerticalSwipeRefreshLayout.setColorSchemeResources(R.color.login_btn_light_color,R.color.colorAccent,R.color.text_cursor_color);
        mVerticalSwipeRefreshLayout.setDistanceToTriggerSync(700);
        mVerticalSwipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> fetchInitData(true),2000));

    }

    @NotNull
    private Spannable initView() {
        Spannable buttonLabel = new SpannableString("  搜 索 ");
        buttonLabel.setSpan(new ImageSpan(getApplicationContext(), R.drawable.ic_search,
                ImageSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return buttonLabel;
    }

    private void initEvent() {
        View notDataView = getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) mRecyclerView.getParent(), false);
        notDataView.setOnClickListener(v -> {
            Intent intent = new Intent(NoteListActivity.this, CreateNoteActivity.class);
            startActivity(intent);
        });
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                NotesModel item = (NotesModel) adapter.getItem(position);
                Intent intent = new Intent(NoteListActivity.this, CreateNoteActivity.class);
                Gson gson = new Gson();
                intent.putExtra("note", gson.toJson(item));
                startActivity(intent);
            }
        });
        Toasty.Config.getInstance().allowQueue(false).apply();
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        fetchInitData(true);
////        mRecyclerView.smoothScrollToPosition(0);
//    }

    @OnClick(R.id.logout)
    void doLogOut() {
        NormalDialog dialog = new NormalDialog(NoteListActivity.this);
        dialog.isTitleShow(false)
                .content("确定要退出登录吗?")
                .cornerRadius(5)//
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(Color.parseColor("#5D5D5D"))
                .dividerColor(Color.parseColor("#ECEDEE"))
                .btnText("确定", "取消")
                .btnTextSize(15, 15)
                .btnTextColor(Color.parseColor("#36B59D"), Color.parseColor("#36B59D"))
                .btnNum(2)
                .show();

        dialog.setOnBtnClickL(() -> {
            UserHttpEngine.doLogOut(new BaseObserver(mContext) {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    super.onSubscribe(d);
                    if (d.isDisposed()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toasty.normal(mContext, "网络无状态", Toasty.LENGTH_LONG).show();
                            }
                        });

                    }
                }

                @Override
                public void onNext(@NonNull Object s) {
                    mAppDatabase.userDao().deleteUser();
                    mAppDatabase.noteListDao().deleteNotes();
                    mAppDatabase.authorsDao().deleteAuthor();
                    MemoNotesApplication.updateUserToken();
                    Intent intent = new Intent(NoteListActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            dialog.dismiss();
        }, dialog::dismiss);

    }

    @OnClick({R.id.create})
    public void createNote() {
        Intent intent = new Intent(NoteListActivity.this, CreateNoteActivity.class);
        startActivity(intent);


    }

    @OnClick(R.id.search)
    public void searchNotes() {
        Intent intent = new Intent(NoteListActivity.this, SearchableActivity.class);
        startActivity(intent);

    }

    /**
     * 页面初始加载数据-------
     * 1.先读取本地数据然后上传
     * 2. 收到数据后存入数据库进行冲突解决
     * 3.渲染列表
     */

    protected void fetchInitData(Boolean isRefresh) {

        List<NotesModel> originNotesList = mAppDatabase.noteListDao().getAllNotes();
        Observable.create(emitter -> {
            // 读取本地数据
            //本地不为空上传
            //如果为空上传空
            if (originNotesList.size() > 0) {
                Hashtable noteList = new Hashtable();
                for (NotesModel note : originNotesList) {
                    Map<String, String> mapBody = new ArrayMap<>();
                    mapBody.put("local_updated_at", note.getLocalUpdatedAt());
                    mapBody.put("checksum", note.getChecksum());
                    noteList.put(note.getId(), mapBody);
                }
                emitter.onNext(noteList);
            } else {
                emitter.onNext(new Hashtable<>());
            }

        })
                .subscribeOn(Schedulers.io())
                .flatMap((Function<Object, ObservableSource<List<NotesModel>>>) (Object reqHeader) -> Observable.create((ObservableOnSubscribe<List<NotesModel>>) (ObservableEmitter<List<NotesModel>> emitter) -> {
                    // 发起网络请求
                    if (!NetworkUtils.isConnected()) {

                        emitter.onNext(originNotesList);

                    }
                    NotesHttpEngine.uploadLocalNotes((Map<String, Object>) reqHeader, new BaseObserver<NoteResModel>(this) {

                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            super.onSubscribe(d);
                            if (d.isDisposed()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toasty.normal(mContext, "网络无状态", Toasty.LENGTH_LONG).show();
                                    }
                                });

                            }
                        }
                        @Override
                        public void onNext(@NonNull NoteResModel noteResModel) {
                            mInitData = noteResModel;
                            List<NotesModel> noteList = noteResModel.getRemoteNotes();
                            List<NotesModel> conflictNoteList = noteResModel.getRemoteNotesModelConflict();
                            List<String> notesDelIds = noteResModel.getRemoteNotesDelIds();
                            // 存入数据库
                            //覆盖响应冲突
                            if (noteList.size() == 0 && conflictNoteList.size() == 0 && notesDelIds.size() == 0) {
                                emitter.onNext(originNotesList);
                            } else {
                                if (noteList.size() > 0) {
                                    List<AuthorsModel> authorList = new ArrayList<AuthorsModel>();
                                    for (NotesModel note : noteList) {
                                        note.authorId = note.author.getId();
                                        authorList.add(note.author);
                                    }
                                    mAppDatabase.noteListDao().insertNotes(noteList);
                                    mAppDatabase.authorsDao().insertAuthors(authorList);

                                }
                                for (int index = 0; index < notesDelIds.size(); index++) {
                                    mAppDatabase.noteListDao().deleteNoteById(notesDelIds.get(index));
                                }
                                if (conflictNoteList.size() > 0) {
                                    List<AuthorsModel> authorList = new ArrayList<AuthorsModel>();
                                    for (NotesModel note : conflictNoteList) {
                                        note.authorId = note.author.getId();
                                        authorList.add(note.author);
                                    }
                                    mAppDatabase.noteListDao().insertNotes(conflictNoteList);
                                    mAppDatabase.authorsDao().insertAuthors(authorList);

                                }
                                List<NotesModel> notes = mAppDatabase.noteListDao().getAllNotes();
                                emitter.onNext(notes);

                            }

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.i(TAG, "onError: 错误" + e);
                            super.onError(e);
                            mVerticalSwipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onComplete() {
                            Log.i(TAG, "onComplete: 完成");
                            emitter.onNext(originNotesList);
                            mVerticalSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notesModels -> {
                    Log.i(TAG, "onComplete: 最后" + notesModels);
                    if (notesModels.size() > 0) {
                        DiffNoteCallBack callback = new DiffNoteCallBack(notesModels);
                        mAdapter.setNewDiffData(callback);
                        mRecyclerView.smoothScrollToPosition(0);
                    } else {
                        mVerticalSwipeRefreshLayout.setRefreshing(false);
                    }
                    if (isRefresh) {

                        mVerticalSwipeRefreshLayout.setRefreshing(false);

                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
