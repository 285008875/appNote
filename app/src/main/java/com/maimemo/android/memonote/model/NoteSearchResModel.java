package com.maimemo.android.memonote.model;

import java.util.List;

public class NoteSearchResModel {

    private PageInfo pagination;
    private List<NotesModel> items;

    public NoteSearchResModel() {

    }

    public NoteSearchResModel(PageInfo pagination, List<NotesModel> items) {
        this.pagination = pagination;
        this.items = items;
    }

    public PageInfo getPagination() {
        return pagination;
    }

    public void setPagination(PageInfo pagination) {
        this.pagination = pagination;
    }

    public List<NotesModel> getItems() {
        return items;
    }

    public void setItems(List<NotesModel> items) {
        this.items = items;
    }

    public static class PageInfo {
        private int limit;
        private int skip;
        private int total;

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public int getSkip() {
            return skip;
        }

        public void setSkip(int skip) {
            this.skip = skip;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        @Override
        public String toString() {
            return "PageInfo{" +
                    "limit=" + limit +
                    ", skip=" + skip +
                    ", total=" + total +
                    '}';
        }
    }
}
