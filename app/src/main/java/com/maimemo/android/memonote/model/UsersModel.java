package com.maimemo.android.memonote.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "users")
public class UsersModel {

    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "id")
    @SerializedName("id")
    public String id;

    @ColumnInfo(name = "maimemo_id")
    @SerializedName("maimemo_id")
    public int maimemoId;

    @ColumnInfo(name = "email")
    @SerializedName("email")
    public String email;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    public String name;

    @ColumnInfo(name = "jwt")
    @SerializedName("jwt")
    public String jwt;


    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public int getMaimemoId() {
        return maimemoId;
    }

    public void setMaimemoId(int maimemoId) {
        this.maimemoId = maimemoId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public String toString() {
        return "UsersModel{" +
                "id='" + id + '\'' +
                ", maimemoId=" + maimemoId +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", jwt='" + jwt + '\'' +
                '}';
    }
}
