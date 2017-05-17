package me.leefeng.beida.set;

import android.content.Intent;
import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.R;

/**
 * @author FengTing
 * @date 2017/05/17 16:39:26
 */
public class SetActivity extends BaseActivity implements SetView {
    private SetPresenter presenter;

    @Override
    protected void initData() {
        presenter = new SetPresenter(this);
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
        return R.layout.activity_set;
    }

    @Override
    protected void doReceive(Intent action) {

    }
}
