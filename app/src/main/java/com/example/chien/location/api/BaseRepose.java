package com.example.chien.location.api;

import com.google.gson.annotations.SerializedName;

import androidx.versionedparcelable.ParcelField;

//@ParcelField()
public class BaseRepose {
    @SerializedName("ID")
    private int id;
    @SerializedName("Title")
    private String title;
    @SerializedName("Error")
    private boolean error;
    @SerializedName("Object")
    private Object object;

    public BaseRepose() {
    }

    public BaseRepose(int id, String title, boolean error, Object object) {
        this.id = id;
        this.title = title;
        this.error = error;
        this.object = object;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
