package com.maimemo.android.memonote.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.diff.BaseQuickDiffCallback;
import com.maimemo.android.memonote.model.NotesModel;

import java.util.List;

public class DiffNoteCallBack extends BaseQuickDiffCallback<NotesModel> {
    public DiffNoteCallBack(List<NotesModel> notesModels, @Nullable List<NotesModel> newList) {
        super(newList);
    }

    public DiffNoteCallBack(List<NotesModel> notesModels) {
        super(notesModels);
    }

    @Override
    protected boolean areItemsTheSame(@NonNull NotesModel oldItem, @NonNull NotesModel newItem) {

        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    protected boolean areContentsTheSame(@NonNull NotesModel oldItem, @NonNull NotesModel newItem) {

        return oldItem.getChecksum().equals(newItem.getChecksum());
    }

    @Nullable
    @Override
    protected Object getChangePayload(@NonNull NotesModel oldItem, @NonNull NotesModel newItem) {
        if (!oldItem.getTitle().equals(newItem.getTitle())) {
            // if only title change（如果标题变化了）
            return NotesListAdapter.TAG_PAYLOAD;
        } else if (!oldItem.getContent().equals(newItem.getContent())) {
            // if only content change（如果内容变化了）
            return NotesListAdapter.CONTENT_PAYLOAD;
        }else if (oldItem.isPublic()!=newItem.isPublic()){
            return NotesListAdapter.IS_PUBLIC_PAYLOAD;
        }
        return null;
    }
}
