package com.robosoft.flickrapp.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahul on 25/5/16.
 */
public class Photo {
    private String mId;
    private String mTitle;
    private String mSecret;
    private String mServer;
    private String mFarm;
    public List<String> mImageUrlList = new ArrayList<>();

    public List<String> getmImageUrlList() {
        return mImageUrlList;
    }
    public void setmImageUrlList(List<String> mImageUrlList) {
        this.mImageUrlList = mImageUrlList;
    }

    public String getmSecret() {
        return mSecret;
    }

    public void setmSecret(String mSecret) {
        this.mSecret = mSecret;
    }

    public String getmServer() {
        return mServer;
    }

    public void setmServer(String mServer) {
        this.mServer = mServer;
    }

    public String getmFarm() {
        return mFarm;
    }

    public void setmFarm(String mFarm) {
        this.mFarm = mFarm;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

}
