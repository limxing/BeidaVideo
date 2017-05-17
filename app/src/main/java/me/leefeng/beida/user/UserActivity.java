package me.leefeng.beida.user;

import android.content.Intent;
import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.R;

/**
 * @author FengTing
 * @date 2017/05/17 14:58:49
 */
public class UserActivity extends BaseActivity implements UserView {
    private UserPresenter presenter;

    @Override
    protected void initData() {
        presenter = new UserPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
        presenter = null;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_user;
    }

    @Override
    protected void doReceive(Intent action) {

    }
}
