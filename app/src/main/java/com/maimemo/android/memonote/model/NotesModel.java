package com.maimemo.android.memonote.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity(tableName = "notes")
public class NotesModel {

    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "id")
    @SerializedName("id")
    public String id;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    public String title;

    @SerializedName("tags")
    @ColumnInfo(name = "tags")
    public List<String> tags;

    @ColumnInfo(name = "content")
    @SerializedName("content")
    public String content;

    @ColumnInfo(name = "is_public")
    @SerializedName("is_public")
    public boolean isPublic;

    @ColumnInfo(name = "checksum")
    @SerializedName("checksum")
    public String checksum;

    @ColumnInfo(name = "is_favorited")
    @SerializedName("is_favorited")
    public boolean isFavorited;

    @ColumnInfo(name = "deleted_at")
    @SerializedName("deleted_at")
    public String deletedAt;

    @ColumnInfo(name = "modified_at")
    @SerializedName("modified_at")
    public String modifiedAt;

    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    public String createdAt;

    @ColumnInfo(name = "local_updated_at")
    @SerializedName("local_updated_at")
    public String localUpdatedAt;

    @SerializedName("author")
    @Ignore
    public AuthorsModel author;

    @ColumnInfo(name = "authorId")
    public String authorId;

    // 服务端
    @Ignore
    public NotesModel(@NonNull String id, String title, List<String> tags, String content, boolean isPublic, String checksum, boolean isFavorited, String deletedAt, String modifiedAt, String createdAt, String localUpdatedAt, AuthorsModel author) {
        this(id, title, tags, content, isPublic, checksum, isFavorited, deletedAt, modifiedAt, createdAt, localUpdatedAt, author.getId());
    }

    @Ignore
    public NotesModel(@NonNull String id, String title, List<String> tags, String content, boolean isPublic, String checksum, boolean isFavorited, String deletedAt, String modifiedAt, String createdAt, String localUpdatedAt, String authorId) {
        this.id = id;
        this.title = title;
        this.tags = tags;
        this.content = content;
        this.isPublic = isPublic;
        this.checksum = checksum;
        this.isFavorited = isFavorited;
        this.deletedAt = deletedAt;
        this.modifiedAt = modifiedAt;
        this.createdAt = createdAt;
        this.localUpdatedAt = localUpdatedAt;
        this.authorId = authorId;
    }

    // 通用型
    public NotesModel(@NonNull String id, String title, List<String> tags, String content, boolean isPublic, String checksum, boolean isFavorited, String deletedAt, String modifiedAt, String createdAt, String localUpdatedAt, AuthorsModel author, String authorId) {
        this.id = id;
        this.title = title;
        this.tags = tags;
        this.content = content;
        this.isPublic = isPublic;
        this.checksum = checksum;
        this.isFavorited = isFavorited;
        this.deletedAt = deletedAt;
        this.modifiedAt = modifiedAt;
        this.createdAt = createdAt;
        this.localUpdatedAt = localUpdatedAt;
        this.author = author;
        this.authorId = author.getId();
    }

    @Ignore
    public NotesModel(String title, String content, boolean isPublic) {

        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
        this.checksum = (title + content + new Date());
        this.localUpdatedAt = new String(String.valueOf(System.currentTimeMillis()));
    }

    public NotesModel( String title, String content, boolean isPublic, String checksum, String localUpdatedAt) {

        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
        this.checksum = checksum;
        this.localUpdatedAt = localUpdatedAt;
        this.authorId = authorId;
    }


    public NotesModel(String id, String title, String content, Boolean is_public, String checksum, String local_updated_at) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.checksum = checksum;
        this.isPublic = is_public;
        this.localUpdatedAt = localUpdatedAt;
        this.authorId = authorId;
    }

    public NotesModel(String id, String title, ArrayList<String> tag, String content, boolean isPublic, String checksum, String localUpdatedAt, String userId) {
        this.id = id;
        this.title = title;
        this.tags = tag;
        this.content = content;
        this.checksum = checksum;
        this.isPublic = isPublic;
        this.localUpdatedAt = localUpdatedAt;
        this.authorId = authorId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLocalUpdatedAt() {
        return localUpdatedAt;
    }

    public void setLocalUpdatedAt(String localUpdatedAt) {
        this.localUpdatedAt = localUpdatedAt;
    }

    public AuthorsModel getAuthor() {
        return author;
    }

    public void setAuthor(AuthorsModel author) {
        this.author = author;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "NoteModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", tags=" + tags +
                ", content='" + content + '\'' +
                ", isPublic=" + isPublic +
                ", checksum='" + checksum + '\'' +
                ", isFavorited=" + isFavorited +
                ", deletedAt='" + deletedAt + '\'' +
                ", modifiedAt='" + modifiedAt + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", localUpdatedAt='" + localUpdatedAt + '\'' +
                ", author=" + author +
                ", authorId='" + authorId + '\'' +
                '}';
    }


}
