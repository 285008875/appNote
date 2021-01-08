package com.maimemo.android.memonote.db.user;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.maimemo.android.memonote.model.UsersModel;

import java.util.List;




/**
 * @author zga
 */
@Dao
public interface  UserDao {

    @Query("SELECT * FROM users")
     public List<UsersModel> getAllUsers();

    @Query("SELECT * FROM users WHERE id=:id")
    public UsersModel getUser(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUser(UsersModel user);

    @Update
    public void updateUser(UsersModel user);

    @Query("DELETE FROM users")
     public  void deleteUser();
}
