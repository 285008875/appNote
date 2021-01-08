package com.maimemo.android.memonote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NoteResModel {
    @SerializedName("remote_notes")
    public List<NotesModel> remoteNotes;

    @SerializedName("remote_notes_del_ids")
    public List<String> remoteNotesDelIds;

    @SerializedName("remote_notes_conflict")
    private List<NotesModel> remoteNotesModelConflict;

    public List<NotesModel> getRemoteNotes() {
        return remoteNotes;
    }

    public void setRemoteNotes(List<NotesModel> remoteNotes) {
        this.remoteNotes = remoteNotes;
    }

    public List<String> getRemoteNotesDelIds() {
        return remoteNotesDelIds;
    }

    public void setRemoteNotesDelIds(List<String> remoteNotesDelIds) {
        this.remoteNotesDelIds = remoteNotesDelIds;
    }

    public List<NotesModel> getRemoteNotesModelConflict() {
        return remoteNotesModelConflict;
    }

    public void setRemoteNotesModelConflict(List<NotesModel> remoteNotesModelConflict) {
        this.remoteNotesModelConflict = remoteNotesModelConflict;
    }
}
