package me.leefeng.beida.notice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.ProjectApplication;
import me.leefeng.beida.R;
import me.leefeng.beida.dbmodel.NoticeMessage;
import me.leefeng.beida.webview.WebViewActivity;
import me.leefeng.lfrecyclerview.LFRecyclerView;
import me.leefeng.lfrecyclerview.OnItemClickListener;
import me.leefeng.library.utils.LogUtils;
import me.leefeng.library.utils.ToastUtils;
import me.leefeng.library.view.FailView;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;

/**
 * @author FengTing
 * @date 2017/05/17 16:39:04
 */
public class NoticeActivity extends BaseActivity implements NoticeView, LFRecyclerView.LFRecyclerViewListener, OnItemClickListener {
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
//        noticeFailview.setMode(FailView.Style.MODE_REFRESH);
//        noticeFailview.setText("正在加载...");

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
        titleTvRight.setVisibility(View.VISIBLE);
        titleTvRight.setText("清空");
        titleName.setText("通知");

        noticeList.setLoadMore(false);
        noticeList.setLFRecyclerViewListener(this);
        noticeList.setOnItemClickListener(this);

//        noticeFailview.setListener(new FailView.FailViewListener() {
//            @Override
//            public void onClick() {
////                initData();
//            }
//        });
        presenter = new NoticePresenter(this);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_notice;
    }

    @Override
    protected void doReceive(Intent action) {
        if (action.getAction().equals(INTENT_NOTICE)) {
            initData();
        }
    }


    @OnClick({R.id.title_back, R.id.title_tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.title_tv_right:
                PromptButton confirm = new PromptButton("确定", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton promptButton) {
                        ProjectApplication.liteOrm.delete(NoticeMessage.class);
                        setResult(RESULT_OK);
                        finish();

                    }
                });
                confirm.setDelyClick(true);
                promptDialog.showWarnAlert("确定要清空消息吗?", new PromptButton("取消", null), confirm);

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
        titleTvRight.setVisibility(View.VISIBLE);
        noticeList.stopRefresh(true);
    }

    @Override
    public void initSuccessNone() {
        noticeFailview.setMode(FailView.Style.MODE_EMPTY);
        noticeFailview.setText("暂时没有信息");
        titleTvRight.setVisibility(View.GONE);
        noticeList.stopRefresh(true);
    }

    @Override
    public void openWebView(String content) {
        Intent intent=new Intent(mContext,WebViewActivity.class);
        intent.putExtra("url",content);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        titleName.postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onClick(int position) {

        presenter.setPositionRead(position);
        LogUtils.i("" + position);
    }

    @Override
    public void onLongClick(int po) {

    }
}
