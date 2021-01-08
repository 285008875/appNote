package com.maimemo.android.memonote.adapter;

import android.graphics.Color;
import android.text.SpannableString;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.maimemo.android.memonote.R;
import com.maimemo.android.memonote.model.NotesModel;
import com.maimemo.android.memonote.util.TextHighLight;

import java.util.List;

public class SearchableAdapter extends BaseQuickAdapter<NotesModel, BaseViewHolder>  {
    private RecyclerView mTagRecyclerView;
    private String mUserId;
    private String mHighLightString;


    public SearchableAdapter(String userId, List data) {
        super(R.layout.recycler_view_searchnote_item, data);
        this.mUserId = userId;
    }
    public String getUserId() {
        return this.mUserId;
    }
    public void setUserId(String userId) {
        this.mUserId = userId;
    }
    public String getHighLightString() {
        return mHighLightString;
    }

    public void setHighLightString(String highLightString) {
        this.mHighLightString = highLightString;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NotesModel item) {
        // 公开私有标签是否显示
        if (item.getAuthor().getId().equals(mUserId)) {
            helper.setGone(R.id.mine, true);
            helper.setGone(R.id.is_public, true);
            helper.setBackgroundRes(R.id.is_public, item.isPublic ? R.drawable.ic_public : R.drawable.ic_private);
        } else {
            helper.setGone(R.id.mine, false);
            helper.setGone(R.id.is_public, false);
        }
        // 设置标签
        mTagRecyclerView = helper.getView(R.id.title_tag);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(mContext);
        RecyclerView mTagRecyclerView = helper.getView(R.id.title_tag);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.setAlignItems(AlignItems.CENTER);
        mTagRecyclerView.setLayoutManager(layoutManager);
        mTagRecyclerView.setHasFixedSize(true);
        TagAdapter tagAdapter = new TagAdapter(item.getTags());
        tagAdapter.setHighLightString(mHighLightString); // 传递高亮词汇
        mTagRecyclerView.setAdapter(tagAdapter);

        // 设置content高亮
        SpannableString content = TextHighLight.matcherSearchContent(Color.parseColor("#36B59D"), item.content, mHighLightString);
        helper.setText(R.id.content_tv, content);

    }


}
