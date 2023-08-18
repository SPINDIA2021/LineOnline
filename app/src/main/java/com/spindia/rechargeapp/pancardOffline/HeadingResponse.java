package com.spindia.rechargeapp.pancardOffline;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeadingResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("headlineof")
    @Expose
    private String headlineof;
    @SerializedName("content")
    @Expose
    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHeadlineof() {
        return headlineof;
    }

    public void setHeadlineof(String headlineof) {
        this.headlineof = headlineof;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
