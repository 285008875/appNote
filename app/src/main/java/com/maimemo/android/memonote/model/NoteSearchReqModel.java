package com.maimemo.android.memonote.model;

public class NoteSearchReqModel {
    private int skip;
    private int limit;
    private String search;

    public NoteSearchReqModel(int skip, int limit, String search) {
        this.skip = skip;
        this.limit = limit;
        this.search = search;
    }

    public NoteSearchReqModel(String search) {
        this.skip = 0;
        this.limit = 30;
        this.search = search;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int  skip) {
        this.skip = skip;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
