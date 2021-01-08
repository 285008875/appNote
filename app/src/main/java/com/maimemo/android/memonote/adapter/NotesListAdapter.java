package com.maimemo.android.memonote.adapter;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.maimemo.android.memonote.R;
import com.maimemo.android.memonote.application.MemoNotesApplication;
import com.maimemo.android.memonote.model.NotesModel;
import com.maimemo.android.memonote.network.base.BaseObserver;
import com.maimemo.android.memonote.network.notes.NotesHttpEngine;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import java.util.List;
import es.dmoral.toasty.Toasty;


public class NotesListAdapter extends BaseQuickAdapter<NotesModel, BaseViewHolder> {
    private String TAG = "NotesListAdapter";
    public static final int TAG_PAYLOAD = 899;   // 标识 某块内容发生改变 用于convertPayloads
    public static final int CONTENT_PAYLOAD = 900;
    public static final int IS_PUBLIC_PAYLOAD = 901;
    public float mDownX;
    public float mDownY;
    public RecyclerView mTagRecyclerView;
    public TagAdapter tagAdapter;

    public NotesListAdapter(@Nullable List<NotesModel> data) {
        super(R.layout.recycler_view_notelist_item, data);

    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NotesModel item) {
        SwipeMenuLayout swipeMenuLayout = helper.getView(R.id.swipeMenuLayout);
        //        监听删除按钮
        helper.getView(R.id.delete_note_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NormalDialog dialog = new NormalDialog(v.getContext());
                dialog.isTitleShow(false)
                        .content("确定要删除该笔记吗?")
                        .cornerRadius(5)//
                        .contentGravity(Gravity.CENTER)//
                        .contentTextColor(Color.parseColor("#5D5D5D"))
                        .dividerColor(Color.parseColor("#ECEDEE"))
                        .btnText("删除", "取消")
                        .btnTextSize(15, 15)
                        .btnTextColor(Color.parseColor("#36B59D"), Color.parseColor("#36B59D"))
                        .btnNum(2)
                        .show();



                dialog.setOnBtnClickL(
                        new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                NotesHttpEngine.delNote(item.getId(), new BaseObserver(mContext) {
                                    @Override
                                    public void onNext(Object o) {
                                        MemoNotesApplication.getAppDatabaseInstance().noteListDao().deleteNoteById(item.getId());
                                        remove(helper.getAdapterPosition() - getHeaderLayoutCount() - getFooterLayoutCount());
                                        dialog.dismiss();

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        super.onError(e);
                                        Toasty.Config.getInstance().allowQueue(false).apply();
                                        Toasty.normal(v.getContext(), "删除失败,请重试", Toasty.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        },
                        dialog::dismiss);


            }
        });


        mTagRecyclerView = helper.getView(R.id.title_tag);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(mContext);
        RecyclerView mTagRecyclerView = helper.getView(R.id.title_tag);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.setAlignItems(AlignItems.CENTER);
        mTagRecyclerView.setLayoutManager(layoutManager);
        mTagRecyclerView.setHasFixedSize(true);
        tagAdapter = new TagAdapter(item.getTags());

        mTagRecyclerView.setAdapter(tagAdapter);
        helper.setText(R.id.content_tv, item.getContent());

        helper.setBackgroundRes(R.id.is_public, item.isPublic ? R.drawable.ic_public : R.drawable.ic_private);


    }

    @Override
    protected void convertPayloads(@NonNull BaseViewHolder helper, NotesModel item, @NonNull List<Object> payloads) {
//        super.convertPayloads(helper, item, payloads);
        for (Object p : payloads) {
            int payload = (int) p;
            if (payload == TAG_PAYLOAD) {
                tagAdapter.setNewData(item.getTags());

            } else if (payload == CONTENT_PAYLOAD) {
                helper.setText(R.id.content_tv, item.getContent());
            } else if (payload == IS_PUBLIC_PAYLOAD) {
                helper.setBackgroundRes(R.id.is_public, item.isPublic ? R.drawable.ic_public : R.drawable.ic_private);
            }
        }

    }


}