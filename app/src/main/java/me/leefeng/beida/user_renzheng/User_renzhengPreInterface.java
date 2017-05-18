package me.leefeng.beida.user_renzheng;

/**
 * @author FengTing
 * @date 2017/05/18 13:13:57
 */
public interface User_renzhengPreInterface {
     void destory();

    void renzheng(String id, String pass, boolean b);

    void getAccountInfo();
}
