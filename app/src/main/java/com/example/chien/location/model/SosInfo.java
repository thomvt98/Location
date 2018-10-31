package com.example.chien.location.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SosInfo implements Serializable {
    int id;
    private String sos_code;
    private String sos_title;
    private String sos_note;
    private String createdby;
    private String sos_createdTime;
    private String priority;
    private String gisdata;
    private List<SosMedia> list;
    public SosInfo() {
    }

    public SosInfo(String sos_code, String sos_note,String gisdata, String sos_title,String priority,String sos_createdTime,String createdby) {
        this.sos_title = sos_title;
        this.sos_note = sos_note;
        this.sos_code=sos_code;
        this.priority = priority;
        this.sos_createdTime=sos_createdTime;
        this.gisdata=gisdata;
        this.createdby=createdby;
        list = new ArrayList<>();
    }


    public SosInfo(int id, String sos_code, String sos_title, String sos_note, String createdby, String sos_createdTime, String priority, String gisdata) {
        this.id = id;
        this.sos_code = sos_code;
        this.sos_title = sos_title;
        this.sos_note = sos_note;
        this.createdby = createdby;
        this.sos_createdTime = sos_createdTime;
        this.priority = priority;
        this.gisdata = gisdata;
        list = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSos_code() {
        return sos_code;
    }

    public void setSos_code(String sos_code) {
        this.sos_code = sos_code;
    }

    public String getSos_title() {
        return sos_title;
    }

    public void setSos_title(String sos_title) {
        this.sos_title = sos_title;
    }

    public String getSos_note() {
        return sos_note;
    }

    public void setSos_note(String sos_note) {
        this.sos_note = sos_note;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getSos_createdTime() {
        return sos_createdTime;
    }

    public void setSos_createdTime(String sos_createdTime) {
        this.sos_createdTime = sos_createdTime;
    }

    public List<SosMedia> getList() {
        return list;
    }

    public void setList(List<SosMedia> list) {
        this.list = list;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getGisdata() {
        return gisdata;
    }

    public void setGisdata(String gisdata) {
        this.gisdata = gisdata;
    }


}

