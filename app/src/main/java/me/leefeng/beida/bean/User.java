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

    String bdAccount;
    String bdPassword;

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
}
