package me.leefeng.beida.notice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.R;
import me.leefeng.lfrecyclerview.LFRecyclerView;
import me.leefeng.library.view.FailView;

/**
 * @author FengTing
 * @date 2017/05/17 16:39:04
 */
public class NoticeActivity extends BaseActivity implements NoticeView {
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.title_tv_right)
    TextView titleTvRight;
    @BindView(R.id.notice_list)
    LFRecyclerView noticeList;
    @BindView(R.id.notice_failview)
    FailView noticeFailview;
    private NoticePresenter presenter;

    @Override
    protected void initData() {
        noticeFailview.setMode(FailView.Style.MODE_REFRESH);
        noticeFailview.setText("正在加载...");
        presenter.initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
        presenter = null;
    }

    @Override
    protected void initView() {
        noticeFailview.setListener(new FailView.FailViewListener() {
            @Override
            public void onClick() {
//                initData();
            }
        });
        presenter = new NoticePresenter(this);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_notice;
    }

    @Override
    protected void doReceive(Intent action) {

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
    public void setAdapter(NoticeAdapter adapter) {
        noticeList.setAdapter(adapter);
    }

    @Override
    public void initSuccess() {
        noticeFailview.setMode(FailView.Style.MODE_SUCCESS);
    }

    @Override
    public void initSuccessNone() {
        noticeFailview.setMode(FailView.Style.MODE_NONET);
        noticeFailview.setText("暂时没有数据");
    }
}
