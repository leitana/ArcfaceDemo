package com.arcsoft.arcfacedemo.model;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private int trackId;
    private String face_id;
    private String user_name;
    private String gender;
    private String organization;
    private String face_imgs;
    private String face_aspect;

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public String getFace_id() {
        return face_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getFace_imgs() {
        return face_imgs;
    }

    public void setFace_imgs(String face_imgs) {
        this.face_imgs = face_imgs;
    }

    public String getFace_aspect() {
        return face_aspect;
    }

    public void setFace_aspect(String face_aspect) {
        this.face_aspect = face_aspect;
    }
}
