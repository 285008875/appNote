package com.maimemo.android.memonote.db.notelist;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.maimemo.android.memonote.model.AuthorsModel;

import java.util.List;
@Dao
public interface AuthorsDao {
    @Transaction
    @Query("SELECT * FROM anthors")
    public List<AuthorsModel> getUsersWithNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public  void insertAuthors(List<AuthorsModel> author);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public  void insertAuthor(AuthorsModel author);

    @Query("DELETE  FROM anthors")
    public void deleteAuthor();
}
