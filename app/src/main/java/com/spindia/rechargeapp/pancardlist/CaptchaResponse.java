package com.spindia.rechargeapp.pancardlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CaptchaResponse {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("img")
    @Expose
    private String img;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
