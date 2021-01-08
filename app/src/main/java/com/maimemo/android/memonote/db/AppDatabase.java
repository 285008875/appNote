package com.maimemo.android.memonote.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.maimemo.android.memonote.db.notelist.AuthorsDao;
import com.maimemo.android.memonote.db.notelist.NoteListDao;
import com.maimemo.android.memonote.db.user.UserDao;
import com.maimemo.android.memonote.model.AuthorsModel;
import com.maimemo.android.memonote.model.NotesModel;
import com.maimemo.android.memonote.model.UsersModel;

/**
 * @author zga
 */

@TypeConverters({DateConverters.class,ListConverter.class})
@Database(entities = {UsersModel.class, NotesModel.class, AuthorsModel.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract NoteListDao noteListDao();
    public abstract AuthorsDao authorsDao();

}
