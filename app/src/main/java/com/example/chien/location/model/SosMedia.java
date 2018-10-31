package com.example.chien.location.model;

import java.io.Serializable;

public class SosMedia implements Serializable {
    private int idsos_media;
    private String sos_code;
    private String file_path;


    public SosMedia(String sos_code, String file_path) {
        this.sos_code = sos_code;
        this.file_path = file_path;
    }

    public SosMedia(int idsos_media, String sos_code, String file_path) {
        this.idsos_media = idsos_media;
        this.sos_code = sos_code;
        this.file_path = file_path;
    }

    public SosMedia() {

    }

    public int getIdsos_media() {
        return idsos_media;
    }

    public void setIdsos_media(int idsos_media) {
        this.idsos_media = idsos_media;
    }

    public String getSos_code() {
        return sos_code;
    }

    public void setSos_code(String sos_code) {
        this.sos_code = sos_code;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
}
