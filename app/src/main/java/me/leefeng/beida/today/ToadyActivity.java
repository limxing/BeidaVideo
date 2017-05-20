package me.leefeng.beida.today;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.R;
import me.leefeng.library.view.FailView;

/**
 * Created by limxing on 2017/5/20.
 */

public class ToadyActivity extends BaseActivity implements TodayView, FailView.FailViewListener {
    @BindView(R.id.title_tv_right)
    TextView titleTvRight;
    @BindView(R.id.today_webview)
    WebView todayWebview;
    @BindView(R.id.today_failview)
    FailView todayFailview;
    @BindView(R.id.title_name)
    TextView titleName;
    private TodayPresenter presenter;

    @Override
    protected void initData() {
        presenter = new TodayPresenter(this);
        onClick();

    }

    @Override
    protected void initView() {
        titleName.setText("今日答疑");
        todayWebview.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
//        OverScrollDecoratorHelper.setUpStaticOverScroll(todayWebview,OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        todayFailview.setListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_today;
    }

    @Override
    protected void doReceive(Intent action) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
        presenter = null;
    }

    @Override
    public void setWebViewData(String s) {
        todayFailview.setMode(FailView.Style.MODE_SUCCESS);
        todayWebview.loadData(s, "text/html; charset=UTF-8", null);
    }

    @Override
    public void showError(String s) {
        todayFailview.setMode(FailView.Style.MODE_FAIL);
        todayFailview.setText(s);

    }


    @OnClick({R.id.title_back, R.id.title_tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.title_tv_right:
                break;
        }
    }


    @Override
    public void onClick() {
        todayFailview.setMode(FailView.Style.MODE_REFRESH);
        todayFailview.setText("正在获取今日答疑");
        titleName.postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.getData(1);
            }
        },500);

    }
}
