package com.maimemo.android.memonote.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


@Entity(tableName = "anthors")
public class AuthorsModel {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private String id;

    @ColumnInfo(name = "maimemo_id")
    @SerializedName("maimemo_id")
    private String maimemoId;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaimemoId() {
        return maimemoId;
    }

    public void setMaimemoId(String maimemoId) {
        this.maimemoId = maimemoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AuthorModel{" +
                "id='" + id + '\'' +
                ", maimemoId='" + maimemoId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}