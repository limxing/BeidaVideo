package me.leefeng.beida.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by FengTing on 2017/4/26.
 */

public class PayType extends BmobObject {
    Long payid;
    Long price;
    String name;

    public Long getPayid() {
        return payid;
    }

    public void setPayid(Long payid) {
        this.payid = payid;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
