package me.leefeng.beida.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by limxing on 2017/5/20.
 */

public class Advice extends BmobObject {
    String content;
    User user;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
