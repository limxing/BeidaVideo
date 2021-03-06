package me.leefeng.beida.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by FengTing on 2017/4/25.
 */

public class User extends BmobObject {
    String username;
    String password;
    String phone;
    Long account;
    boolean isBeida;
    String realName;
    String headPic;
    String subject;

    String bdAccount;
    String bdPassword;
    String app="beida";
    public User(){

    }

    public User(User user) {
        setObjectId(user.getObjectId());
        this.username=user.getUsername();
        this.password=user.getPassword();
        this.phone=user.getPhone();
        this.account=user.getAccount();
        this.isBeida=user.isBeida();
        this.realName=user.getRealName();
        this.bdAccount=user.getBdAccount();
        this.bdPassword=user.getBdPassword();
        this.subject=user.getSubject();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBdAccount() {
        return bdAccount;
    }

    public void setBdAccount(String bdAccount) {
        this.bdAccount = bdAccount;
    }

    public String getBdPassword() {
        return bdPassword;
    }

    public void setBdPassword(String bdPassword) {
        this.bdPassword = bdPassword;
    }

    public boolean isBeida() {
        return isBeida;
    }

    public void setBeida(boolean beida) {
        isBeida = beida;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }
}
