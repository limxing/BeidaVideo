package me.leefeng.beida.user;

/**
 * @author FengTing
 * @date 2017/05/17 14:58:49
 */
public class UserPresenter implements UserPreInterface {
    private UserView userView;

    public UserPresenter(UserView userView) {
        this.userView = userView;
    }

    @Override
    public void destory() {
        userView = null;
    }
}
