package me.leefeng.beida.notice;

import android.content.Intent;
import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.R;

/**
 * @author FengTing
 * @date 2017/05/17 16:39:04
 */
public class NoticeActivity extends BaseActivity implements NoticeView {
    private NoticePresenter presenter;

    @Override
    protected void initData() {
        presenter = new NoticePresenter(this);
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
        return R.layout.activity_notice;
    }

    @Override
    protected void doReceive(Intent action) {

    }
}
