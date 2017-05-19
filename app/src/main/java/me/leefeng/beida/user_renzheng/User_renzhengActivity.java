package me.leefeng.beida.user_renzheng;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.BannerView;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.Constants;
import me.leefeng.beida.ProjectApplication;
import me.leefeng.beida.R;
import me.leefeng.library.utils.StringUtils;
import me.leefeng.library.view.FailView;

/**
 * @author FengTing
 * @date 2017/05/18 13:13:57
 */
public class User_renzhengActivity extends BaseActivity implements User_renzhengView, FailView.FailViewListener {
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.renzheng_id)
    MaterialEditText renzhengId;
    @BindView(R.id.renzheng_pas)
    MaterialEditText renzhengPas;
    @BindView(R.id.renzheng_banner)
    RelativeLayout renzhengBanner;
    @BindView(R.id.renzheng_failview)
    FailView renzhengFailview;
    @BindView(R.id.renzheng_headpic)
    ImageView renzhengHeadpic;
    @BindView(R.id.renzheng_name)
    TextView renzhengName;
    @BindView(R.id.renzheng_bmh)
    TextView renzhengBmh;
    @BindView(R.id.renzheng_xh)
    TextView renzhengXh;
    @BindView(R.id.renzheng_score)
    TextView renzhengScore;
    @BindView(R.id.renzheng_subject)
    TextView renzhengSubject;
    @BindView(R.id.renzheng_true)
    LinearLayout renzhengTrue;
    private User_renzhengPresenter presenter;

    @Override
    protected void initData() {
        presenter = new User_renzhengPresenter(this);
        BannerView banner = new BannerView(this, ADSize.BANNER, Constants.APPID, Constants.BannerPosID);
        banner.setRefresh(30);
        renzhengBanner.addView(banner);
        banner.loadAD();
        if (ProjectApplication.user.isBeida()) {
            initBeidaAccount();
            titleName.setText("学员已认证");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
        presenter = null;
    }

    @Override
    protected void initView() {
        titleName.setText("学员认证");

        renzhengFailview.setListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_user_renzheng;
    }

    @Override
    protected void doReceive(Intent action) {

    }


    @OnClick({R.id.title_back, R.id.renzheng_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.renzheng_send:
                String account = renzhengId.getText().toString().trim();
                String pass = renzhengPas.getText().toString().trim();
                if (StringUtils.isEmpty(account)) {
                    promptDialog.showInfo("请输入报名号或学号");
                    return;
                }
                if (StringUtils.isEmpty(pass)) {
                    promptDialog.showInfo("请输入密码");
                    return;
                }
                promptDialog.showLoading("正在学员认证");
                presenter.renzheng(account, pass, true);
                break;
        }
    }

    @Override
    public void showFail(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                promptDialog.showError(s);
            }
        });
    }

    @Override
    public void renzhengSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                promptDialog.showSuccess("认证成功");
                initBeidaAccount();
                Intent intent = new Intent(INTENT_LOGIN_OUT);
                sendBroadcast(intent);
            }
        });
    }

    @Override
    public void initAccountFail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                renzhengFailview.setMode(FailView.Style.MODE_CRY);
                renzhengFailview.setText("获取失败，点击重试");
            }
        });
    }

    @Override
    public void initAccountSuccess(final String bmh, final String xh, final String xf) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                renzhengTrue.setVisibility(View.VISIBLE);
                renzhengName.setText("姓名：" + ProjectApplication.user.getRealName());
                renzhengXh.setText("学号：" + xh);
                renzhengBmh.setText("报名号：" + bmh);
                renzhengSubject.setText("专业：" + ProjectApplication.user.getSubject());
                renzhengScore.setText(xf);
                renzhengFailview.setMode(FailView.Style.MODE_SUCCESS);
                Glide.with(mContext).load(ProjectApplication.user.getHeadPic())
                        .placeholder(R.drawable.renzheng_headpic).into(renzhengHeadpic);
            }
        });


    }

    /**
     * 初始化帐号
     */

    private void initBeidaAccount() {
        renzhengFailview.setMode(FailView.Style.MODE_REFRESH);
        renzhengFailview.setText("正在加载认证信息");
        presenter.getAccountInfo();
    }

    @Override
    public void onClick() {
        initBeidaAccount();
    }

}
