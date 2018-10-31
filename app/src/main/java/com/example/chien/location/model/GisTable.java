package com.example.chien.location.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GisTable implements Serializable {
    private int id;
    private String code;
    private String name;
    private String parent;
    private String type;
    private String createBy;
    private String gisdata;
    private String createTime;
    private String updateTime;
    private List<NodeGis> list;

    public GisTable() {
    }

    public GisTable(String code, String name, String gisdata, String type,String parent,String createTime,String updateTime) {
        this.code = code;
        this.name = name;
        this.gisdata = gisdata;
        this.type = type;
        this.parent=parent;
        this.createTime=createTime;
        this.updateTime=updateTime;
        list = new ArrayList<>();
    }

    public GisTable(int id, String code, String name, String type, String gisdata) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.type = type;
        this.gisdata = gisdata;

        this.list = new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public List<NodeGis> getList() {
        return list;
    }

    public void setList(List<NodeGis> list) {
        this.list = list;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGisdata() {
        return gisdata;
    }

    public void setGisdata(String gisdata) {
        this.gisdata = gisdata;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
