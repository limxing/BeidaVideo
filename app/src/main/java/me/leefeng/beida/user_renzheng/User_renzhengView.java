package me.leefeng.beida.user_renzheng;


/**
 * @author FengTing
 * @date 2017/05/18 13:13:57
 */
public interface User_renzhengView {
    void showFail(String s);

    void renzhengSuccess();

    void initAccountFail();

    void initAccountSuccess(String bmh, String xh, String xf);
}
