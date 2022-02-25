package com.developeralamin.firechat.model;

public class UserData {

    String id, name, number, email, profilepictureurl;

    public UserData() {
    }

    public UserData(String id, String name, String number, String email, String profilepictureurl) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.email = email;
        this.profilepictureurl = profilepictureurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilepictureurl() {
        return profilepictureurl;
    }

    public void setProfilepictureurl(String profilepictureurl) {
        this.profilepictureurl = profilepictureurl;
    }
}
