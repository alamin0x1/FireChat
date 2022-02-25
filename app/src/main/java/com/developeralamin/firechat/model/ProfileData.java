package com.developeralamin.firechat.model;

public class ProfileData {

    String name, number, email, password, profilepictureurl;

    public ProfileData() {
    }

    public ProfileData(String name, String number, String email, String password, String profilepictureurl) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.password = password;
        this.profilepictureurl = profilepictureurl;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilepictureurl() {
        return profilepictureurl;
    }

    public void setProfilepictureurl(String profilepictureurl) {
        this.profilepictureurl = profilepictureurl;
    }
}

