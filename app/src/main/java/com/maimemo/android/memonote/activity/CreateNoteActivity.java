package com.maimemo.android.memonote.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import com.flyco.dialog.widget.NormalDialog;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.jakewharton.rxbinding4.widget.RxRadioGroup;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.maimemo.android.memonote.R;
import com.maimemo.android.memonote.activity.base.BaseActivity;
import com.maimemo.android.memonote.application.MemoNotesApplication;
import com.maimemo.android.memonote.model.NotesModel;
import com.maimemo.android.memonote.network.base.BaseObserver;
import com.maimemo.android.memonote.network.base.NetworkError;
import com.maimemo.android.memonote.network.notes.NotesHttpEngine;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static com.blankj.utilcode.util.BarUtils.getActionBarHeight;
import static com.blankj.utilcode.util.ScreenUtils.getScreenHeight;

public class CreateNoteActivity extends BaseActivity {
    private static final String TAG = "CreateNoteActivity";

    @BindView(R.id.toolbar)     // 菜单栏
    public Toolbar mToolbar;

    @BindView(R.id.title_toolbar_tv) //菜单栏title
    public TextView mTitleOfToolbar;

    MenuItem mSaveNoteBtn;

    @BindView(R.id.radio_group_status)
    public RadioGroup mStatusRadioGroup;

    @BindView(R.id.private_radio)      //私有
    public RadioButton mPrivateRadioBtn;

    @BindView(R.id.public_radio)        //公有
    public RadioButton mPublicRadioBtn;

    @BindView(R.id.note_tag_et)    // 创建标签输入框
    public EditText mNoteTagEt;

    @BindView(R.id.scrollView)
    public ScrollView mScrollView;

    @BindView(R.id.note_tag_group)    // 标签组
    public FlexboxLayout mTagGroupFlexboxLayout;

    @BindView(R.id.status_tv)
    public TextView mStatusTv;

    @BindView(R.id.note_content_et)    //笔记content
    public EditText mNoteContentEt;

    @BindView(R.id.view_line_of_bottom_tag)
    public View mViewLine;

    public boolean isPublic = false;

    public NotesModel mNoteInfo = null;     // 笔记id listActivity和crateActivity界面跳转传参

    public String cacheTitle = "";
    // cache 用于缓存 实现保存按钮的状态切换
    public String cacheContent = "";

    public boolean cacheIsPublic = false;

    public boolean isSave = false;   // 弹窗是否保存


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiry_create_note);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Toasty.Config.getInstance().allowQueue(false).apply();
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        mNoteContentEt.setMinHeight(getScreenHeight() - getActionBarHeight());
        // 笔记id listActivity和crateActivity界面跳转传参 获取参数
        // 判断是新建搜记还是界面跳转
        String noteString = getIntent().getStringExtra("note");

        if (noteString != null) {
            mNoteInfo = new Gson().fromJson(noteString, NotesModel.class);
            cacheIsPublic = mNoteInfo.isPublic();
            cacheContent = mNoteInfo.getContent();
            cacheTitle = mNoteInfo.getTitle();
            fillPageWithNoteInfo(mNoteInfo);
            mTitleOfToolbar.setText(R.string.title_of_create_note);
        }

        Observable<String> mNoteTagEtObservable = RxTextView.textChanges(mNoteTagEt)
                .map(CharSequence::toString);
        Observable<String> mNoteContentEtObservable = RxTextView.textChanges(mNoteContentEt)
                .map(CharSequence::toString);

        Observable<Boolean> mStatusRadioGroupObservable = RxRadioGroup.checkedChanges(mStatusRadioGroup).map(integer -> {
            if (integer == mPrivateRadioBtn.getId()) {
                isPublic = false;
                return false;
            } else {
                isPublic = true;
                return true;
            }
        });
        //限制输入200字符和50标签
        mNoteTagEtObservable
                .throttleLast(800, TimeUnit.MILLISECONDS)
                .subscribe(tags -> {
                    if (tags.trim().length() >= 200) {
                        mNoteTagEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
                        Toasty.normal(CreateNoteActivity.this, "最多输入200个字符").show();
                    } else {
                        mNoteTagEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
                    }

                    ArrayList<String> tagListOverFlow = divideStringToTag(tags);

                    if (tagListOverFlow.size() > 50) {
                        ArrayList<String> tagListMatch = new ArrayList<String>(tagListOverFlow.subList(0, tagListOverFlow.size() - 1));
                        StringBuilder temp = formatTag(tagListMatch);
                        mNoteTagEt.setText(temp);
                        fillTagWithTagInfo(tagListMatch);
                        Toasty.normal(CreateNoteActivity.this, "最多输入50个标签").show();

                    }

                    mNoteTagEt.setSelection(mNoteTagEt.getText().toString().length());
                });
        //限制内容输入长度
        mNoteContentEtObservable.subscribe(tags -> {

            if (tags.getBytes(Charset.defaultCharset()).length >= 25000) {

                mNoteTagEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25000)});
                Toasty.normal(CreateNoteActivity.this, "最多输入200个字符").show();
            } else {

                mNoteTagEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25000)});
            }
            mNoteTagEt.setSelection(mNoteTagEt.getText().toString().length());
        });
        // 监听内容是否发生变化用于改变保存按钮状态
        Observable.combineLatest(mNoteTagEtObservable, mNoteContentEtObservable, mStatusRadioGroupObservable, (title, content, isPub) -> {

            if (title.equals(cacheTitle) && content.equals(cacheContent)||content.trim().length()==0&& cacheIsPublic == isPub) {
                return true;
            }

            return false;
        }).debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isChange -> {
                    if (isChange) {
                        changeBtnInUnsaveStatus();
                    } else {
                        changeBtnInSaveStatus();
                    }
                });
        //监听输入框在页面实时生成tag
        mNoteTagEtObservable
                .throttleLast(800, TimeUnit.MILLISECONDS)
                .map(charSequence -> {
                    String tags = charSequence.toString();
                    return (ArrayList) divideStringToTag(tags);

                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::fillTagWithTagInfo);


        // 返回按钮
        mToolbar.setNavigationOnClickListener(view -> {


            if (isSave) {
                NotesModel noteInfo = getNoteInfoFromEditText();
                if (noteInfo.getTitle().trim().length()>0&&noteInfo.getContent().trim().equals("")){

                    NormalDialog dialog = new NormalDialog(CreateNoteActivity.this);
                    dialog.isTitleShow(false)
                            .content("若放弃编辑，此次编辑的内容将会丢失")
                            .cornerRadius(10)//
                            .contentGravity(Gravity.CENTER)//
                            .contentTextColor(Color.parseColor("#5D5D5D"))
                            .dividerColor(Color.parseColor("#ECEDEE"))
                            .btnText("确定", "取消")
                            .btnTextSize(15, 15)
                            .btnTextColor(Color.parseColor("#36B59D"), Color.parseColor("#36B59D"))
                            .btnNum(2)
                            .show();
                    dialog.setOnBtnClickL(this::finish, dialog::dismiss);
                }else {
                    finish();
                }

            } else {
                NormalDialog dialog = new NormalDialog(CreateNoteActivity.this);
                dialog.isTitleShow(false)
                        .content("内容未保存是否要保存笔记内容?")
                        .cornerRadius(10)//
                        .contentGravity(Gravity.CENTER)//
                        .contentTextColor(Color.parseColor("#5D5D5D"))
                        .dividerColor(Color.parseColor("#ECEDEE"))
                        .btnText("放弃", "保存")
                        .btnTextSize(15, 15)
                        .btnTextColor(Color.parseColor("#36B59D"), Color.parseColor("#36B59D"))
                        .btnNum(2)
                        .show();

                dialog.setOnBtnClickL(() -> {

                    dialog.dismiss();
                    Intent intent = new Intent(CreateNoteActivity.this, NoteListActivity.class);
                    startActivity(intent);
                    finish();

                }, () -> {

                    saveNote(true);
                    dialog.dismiss();


                });
            }

        });
    }

    @Override
    public void onBackPressed() {
        //点击返回键
        super.onBackPressed();
        finish();
    }

    /**
     * 将输入字符串根据 “ / ” 划分为数组
     *
     * @param tags 字符串
     * @return
     */
    @NotNull
    private ArrayList<String> divideStringToTag(String tags) {
        String[] tagArr = tags.split("/");

        ArrayList<String> tempTags = new ArrayList<String>();
        for (int index = 0; index < tagArr.length; index++) {
            if (!tagArr[index].trim().toString().equals("")) {
                tempTags.add(tagArr[index].trim().toString());
            }
        }
        return tempTags;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        mSaveNoteBtn = menu.findItem(R.id.save);
        return true;
    }
    // 改变保存按钮状态
    private void changeBtnInSaveStatus() {
        isSave = false;
        SpannableString spannableString = new SpannableString(mSaveNoteBtn.getTitle());
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.login_btn_light_color)), 0, spannableString.length(), 0);
        mSaveNoteBtn.setTitle(spannableString);
        mSaveNoteBtn.setEnabled(true);
    }
    // 改变保存按钮状态
    private void changeBtnInUnsaveStatus() {
        isSave = true;
        SpannableString spannableString = new SpannableString(mSaveNoteBtn.getTitle());
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fontColor)), 0, spannableString.length(), 0);
        mSaveNoteBtn.setTitle(spannableString);
        mSaveNoteBtn.setEnabled(false);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save) {
            saveNote(false);
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * 用从本地或远程获取的笔记信息填充到Recycle'rView
     *
     * @param notesModel from local or server
     */
    private void fillPageWithNoteInfo(NotesModel notesModel) {
        runOnUiThread(() -> {
            mNoteContentEt.setText(notesModel.getContent());
            mNoteTagEt.setText(notesModel.getTitle());
            if (notesModel.isPublic) {
                mPublicRadioBtn.setChecked(true);
                mPrivateRadioBtn.setChecked(false);
            } else {
                mPrivateRadioBtn.setChecked(true);
                mPublicRadioBtn.setChecked(false);
            }
            fillTagWithTagInfo(notesModel.getTags());
        });
    }

    private void fillTagWithTagInfo(List<String> tags) {
        mTagGroupFlexboxLayout.removeAllViews();
        mTagGroupFlexboxLayout.removeAllViewsInLayout();
        for (int index = 0; index < tags.size(); index++) {
            if (!tags.get(index).isEmpty()) {
                TextView textView = new TextView(getContext());

                textView.setText(tags.get(index));
                textView.setTextSize(11);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 8, 8);//4个参数按顺序分别是左上右下
                textView.setLayoutParams(layoutParams);
                textView.setPadding(8, 2, 8, 2);
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setSingleLine(true);
                textView.setBackgroundResource(R.drawable.test);
                textView.setTextColor(mContext.getResources().getColor(R.color.title_text_color));
                mTagGroupFlexboxLayout.addView(textView);
            }
        }
    }

    public StringBuilder formatTag(ArrayList<String> tags) {
        StringBuilder tagString = new StringBuilder();
        for (int index = 0; index < tags.size(); index++) {
            if (index == tags.size() - 1) {
                tagString.append(tags.get(index));
            } else {
                tagString.append(tags.get(index) + "/");
            }

        }
        return tagString;
    }
    // 获取输入框信息
    private NotesModel getNoteInfoFromEditText() {
        ArrayList<String> tags = divideStringToTag(mNoteTagEt.getText().toString());
        StringBuilder tagString = new StringBuilder();
        String title = formatTag(tags).toString();
        String content = mNoteContentEt.getText().toString();
        CRC32 checksum = new CRC32();
        checksum.update((title + content + isPublic).getBytes());
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'z'");
        String localUpdatedAt = df.format(new Date(System.currentTimeMillis())).toString();
        return new NotesModel(title, content, isPublic, String.valueOf(checksum.getValue()), localUpdatedAt);
    }

    /**
     * 在返回上一级按钮处调用 和 保存按钮处调用  因此需判断是否跳转界面
     *
     * @param isShift
     */
    public void saveNote(boolean isShift) {
        // 首先判断是新建笔记还是更新笔记依据mNoteid
        //新建笔记保存之后更新mNoteid下次保存是更新状态不是新建状态

        if (mNoteInfo!=null) {
            NotesModel noteTemp = getNoteInfoFromEditText();
            Hashtable note = new Hashtable();
            // todo :转成模型 不用hash
            note.put("title", noteTemp.getTitle());
            note.put("content", noteTemp.getContent());
            note.put("is_public", noteTemp.isPublic());
            note.put("checksum", noteTemp.getChecksum());
            note.put("local_updated_at", noteTemp.getLocalUpdatedAt());

            NotesHttpEngine.updateNote(mNoteInfo.getId(), note, new BaseObserver<NotesModel>(this) {
                @Override
                public void onNext(NotesModel note) {
                    // 保存笔记
                    cacheContent = note.getContent();
                    cacheIsPublic = note.isPublic();
                    cacheTitle = note.getTitle();
                    changeBtnInUnsaveStatus();
                    Toasty.normal(CreateNoteActivity.this, R.string.save_success, Toasty.LENGTH_SHORT).show();
                    mAppDatabase.noteListDao().insertNote(note);
                    mAppDatabase.authorsDao().insertAuthor(note.getAuthor());
                    if (isShift) {
                        onBackPressed();
                    }
                }


            });
        } else {
            NotesModel note = getNoteInfoFromEditText();

            NotesHttpEngine.createNote(note, new BaseObserver<NotesModel>(this) {

                @Override
                public void onNext(NotesModel result) {
                    // 是否跳转界面
                    if (isShift) {
                        onBackPressed();
                    }
                    mTitleOfToolbar.setText(R.string.title_of_create_note);
                    Toasty.normal(CreateNoteActivity.this, R.string.save_success, Toasty.LENGTH_SHORT).show();
                    changeBtnInUnsaveStatus();
                    NotesHttpEngine.getNote(result.getId(), new BaseObserver<NotesModel>() {
                        @Override
                        public void onNext( NotesModel notesModel) {
                            //将信息填充的界面
                            mNoteInfo = notesModel;
                            cacheTitle = notesModel.getTitle();
                            cacheIsPublic = notesModel.isPublic();
                            cacheContent = notesModel.getContent();
                            mAppDatabase.noteListDao().insertNote(notesModel);
                            mAppDatabase.authorsDao().insertAuthor(notesModel.getAuthor());

                        }
                    });
                }


            });

        }
    }
}
