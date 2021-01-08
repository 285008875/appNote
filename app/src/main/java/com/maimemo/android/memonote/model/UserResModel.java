package com.maimemo.android.memonote.model;

import com.google.gson.annotations.SerializedName;

public class UserResModel {

    @SerializedName("error")
    public int error;

    @SerializedName("message")
    public String message;


    public UserResModel(int error, String message) {
        this.error = error;
        this.message = message;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
