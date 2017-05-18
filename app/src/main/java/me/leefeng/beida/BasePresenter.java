package me.leefeng.beida;

import cn.bmob.v3.exception.BmobException;
import me.leefeng.beida.user_renzheng.User_renzhengView;

/**
 * @author FengTing
 * @date 2017/04/24 16:00:40
 */
public class BasePresenter {
    protected boolean checkBmobException(BmobException e, User_renzhengView user_renzhengView) {
        if (e != null) {


            return false;
        }
        return true;
    }
}
