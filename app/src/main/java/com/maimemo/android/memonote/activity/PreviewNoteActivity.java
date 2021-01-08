package com.maimemo.android.memonote.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.maimemo.android.memonote.R;
import com.maimemo.android.memonote.activity.base.BaseActivity;
import com.maimemo.android.memonote.model.NotesModel;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PreviewNoteActivity extends BaseActivity {
    private static final String TAG = "PreviewNoteActivity";

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @Nullable
    @BindView(R.id.preview_note_tag_group)
    public FlexboxLayout mTagGroupFlexBoxLayout;
    @Nullable
    @BindView(R.id.preview_note_content_tv)
    public TextView mContentTv;
    private String mNoteId = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_note);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        String noteString = getIntent().getStringExtra("note");
        if (noteString != null) {
//            fetchNoteInfo(mNoteId);
            Gson gson  = new Gson();
            NotesModel noteInfo = new Gson().fromJson(noteString, NotesModel.class);
            fillPageWithNoteInfo(noteInfo);
        }
        // 返回按钮
        mToolbar.setNavigationOnClickListener(view -> {
            finish();
        });
    }

    private void fillPageWithNoteInfo(NotesModel notesModel) {
//        mTagGroupFlexBoxLayout.removeAllViews();
        List<String> tags = notesModel.getTags();
        for (int index = 0; index < tags.size(); index++) {
            if (!tags.get(index).isEmpty()) {
                TextView textView = new TextView(getContext());
//                        textView.setMaxEms(15);
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
                mTagGroupFlexBoxLayout.addView(textView);
            }
        }
        mContentTv.setText(notesModel.getContent());

    }


}
