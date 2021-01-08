package com.maimemo.android.memonote.db.notelist;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.maimemo.android.memonote.model.NotesModel;

import java.util.List;

import retrofit2.http.DELETE;

@Dao
public interface NoteListDao {
    @Query("SELECT * FROM notes ORDER BY created_at DESC")
    public List<NotesModel> getAllNotes();

    @Query("SELECT * FROM notes WHERE id=:id ")
    public NotesModel getNoteById(String id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertNotes( List<NotesModel> notes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertNote( NotesModel notes);

    @Query("DELETE  FROM notes WHERE id=:id")
    public void deleteNoteById(String id);

    @DELETE

    @Query("DELETE  FROM notes")
    public void deleteNotes();
}
