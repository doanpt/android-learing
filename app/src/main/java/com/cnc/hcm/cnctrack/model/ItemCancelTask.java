package com.cnc.hcm.cnctrack.model;

public final class ItemCancelTask {

    public final Type type;
    public final String content;
    public final Data data;

    public ItemCancelTask(Type type, String content, Data data) {
        this.type = type;
        this.content = content;
        this.data = data;
    }

    public static final class Type {
        public final String _id;
        public final long __v;
        public final String title;
        public final String createdDate;

        public Type(String _id, long __v, String title, String createdDate) {
            this._id = _id;
            this.__v = __v;
            this.title = title;
            this.createdDate = createdDate;
        }
    }

    public static final class Data {
        public final String _id;

        public Data(String _id) {
            this._id = _id;
        }
    }
}
