package com.sdr.patrollib;

import java.util.List;

/**
 * Created by HyFun on 2018/12/06.
 * Email: 775183940@qq.com
 * Description: 巡检用户
 */

public class PatrolUser {
    private String userId;
    private String userName;
    private String phoneNum;
    private String accessToken;

    // 不使用的功能列表
    private List<String> exceptFuctionList;

    public PatrolUser(String userId, String userName, String phoneNum, String accessToken, List<String> exceptFuctionList) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNum = phoneNum;
        this.accessToken = accessToken;
        this.exceptFuctionList = exceptFuctionList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<String> getExceptFuctionList() {
        return exceptFuctionList;
    }

    public void setExceptFuctionList(List<String> exceptFuctionList) {
        this.exceptFuctionList = exceptFuctionList;
    }
}
