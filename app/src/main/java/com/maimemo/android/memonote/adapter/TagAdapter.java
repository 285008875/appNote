package com.maimemo.android.memonote.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.SpannableString;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.maimemo.android.memonote.R;
import com.maimemo.android.memonote.util.TextHighLight;

import java.util.List;

public class TagAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public String mHighLightString;

    public TagAdapter(@Nullable List<String> data) {
        super(R.layout.recyc_notelist_item_tag, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {

        if (mHighLightString != null) {
            SpannableString highLightContent = TextHighLight.matcherSearchContent(Color.parseColor("#36B59D"), item.toString(), mHighLightString);
            helper.setText(R.id.tag_name, highLightContent);
        } else {

            if (!item.toString().equals("")) {
                helper.setVisible(R.id.tag_name_gone,false);
                helper.setVisible(R.id.tag_name,true);
                helper.setText(R.id.tag_name, item.toString());
            } else {
                helper.setVisible(R.id.tag_name_gone,true);
                helper.setVisible(R.id.tag_name,false);
                helper.setText(R.id.tag_name_gone,"无标签");

            }

        }
    }

    public void setHighLightString(String highLightString) {
        this.mHighLightString = highLightString;
    }


    public String getHighLightString() {
        return this.mHighLightString;
    }
}
