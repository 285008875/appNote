package com.maimemo.android.memonote.network.notes;

import com.google.gson.annotations.SerializedName;
import com.maimemo.android.memonote.model.AuthorsModel;

import java.util.List;

public class NotesModel {
    public String id;

    public String title;

    public List<String> tags;

    public String content;

    @SerializedName("is_public")
    public boolean isPublic;

    public String checksum;

    @SerializedName("is_favorited")
    public boolean isFavorited;

    @SerializedName("deleted_at")
    public String deletedAt;

    @SerializedName("modified_at")
    public String modifiedAt;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("local_updated_at")
    public String localUpdatedAt;

    @SerializedName("author")
    public AuthorsModel author;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
                '}';
    }
}
