package me.leefeng.beida.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.SMSSDK;
import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.Constants;
import me.leefeng.beida.ProjectApplication;
import me.leefeng.beida.R;
import me.leefeng.beida.main.MainActivity;
import me.leefeng.library.utils.SharedPreferencesUtil;
import me.leefeng.library.utils.StringUtils;
import me.leefeng.library.utils.ToastUtils;

/**
 * @author FengTing
 * @date 2017/04/25 10:52:05
 */
public class LoginActivity extends BaseActivity implements LoginView {
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.title_tv_right)
    TextView titleTvRight;
    @BindView(R.id.login_id)
    MaterialEditText loginId;
    @BindView(R.id.login_pw)
    MaterialEditText loginPw;
    @BindView(R.id.login_phone)
    TextView loginPhone;
    @BindView(R.id.title_back)
    ImageView titleBack;

    @BindView(R.id.welcome_adbanner)
    RelativeLayout welcomeAdbanner;
    private LoginPresenter presenter;
    private TimeCount timer;
    private boolean getPhoneConfirmNumSuccess;
    private String num;
    private boolean isBack;

    @Override
    protected void initData() {
        isBack = getIntent().getBooleanExtra("isBack", false);
        presenter = new LoginPresenter(this);
        timer = new TimeCount(60000, 1000);
        if (isBack) {
            findViewById(R.id.login_title).setVisibility(View.VISIBLE);
            titleName.setVisibility(View.INVISIBLE);
//            findViewById(R.id.title_bac).setBackgroundColor(Color.TRANSPARENT);
            titleBack.setVisibility(View.VISIBLE);
            titleBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            });
        }

        // 创建Banner广告AdView对象
        BannerView banner = new BannerView(this, ADSize.BANNER, Constants.APPID, Constants.BannerPosID);
        banner.setRefresh(30);
//        banner.setADListener(new AbstractBannerADListener() {
//
//            @Override
//            public void onNoAD(int arg0) {
//                Log.i("AD_DEMO", "BannerNoAD，eCode=" + arg0);
//            }
//
//            @Override
//            public void onADReceiv() {
//                Log.i("AD_DEMO", "ONBannerReceive");
//            }
//        });
//        banner.setShowClose(false);
        welcomeAdbanner.addView(banner);
        /* 发起广告请求，收到广告数据后会展示数据   */
        banner.loadAD();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
        presenter.destory();
        presenter = null;
    }

    @Override
    protected void initView() {
        titleName.setText("登录");
        titleBack.setVisibility(View.GONE);
        titleTvRight.setVisibility(View.INVISIBLE);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void doReceive(Intent action) {

    }

    /**
     * 点击登录
     *
     * @param view
     */
    public void login(View view) {
        //测试，直接登录\
        if (ProjectApplication.isDebug) {
            num = "18514528236";
            presenter.getUserInfo(num);
        }

        if (StringUtils.isEmpty(loginId.getText().toString().trim())) {
            ToastUtils.showShort(mContext, "请输入手机号");
            return;
        }
        if (!getPhoneConfirmNumSuccess) {
            ToastUtils.showShort(mContext, "请获取验证码");
            return;
        }
        String confirmNUm = loginPw.getText().toString().trim();
        if (StringUtils.isEmpty(confirmNUm)) {
            ToastUtils.showShort(mContext, "请输入验证码");
            return;
        } else {
            SMSSDK.submitVerificationCode("86", num, confirmNUm);
        }
        closeInput();
        promptDialog.showLoading("正在登录");
//        svp.showLoading("正在登录");

    }

    /**
     * 获取验证码按钮
     *
     */
    @OnClick(R.id.login_phone)
    public void getPhoneConfirmNum() {
        num = loginId.getText().toString().trim();
        String telRegex = "[1][34578]\\d{9}";
        if (num.length() == 11 && num.matches(telRegex)) {
            closeInput();
//            svp.showLoading("正在获取验证码");
            promptDialog.showLoading("正在获取验证码");
            presenter.getPhoneConfirmNum(num);
        } else {
            ToastUtils.showLong(mContext, "手机格式错误");
        }
    }

    @Override
    public void showSvp(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                svp.showInfoWithStatus(msg);
                promptDialog.showInfo(msg);
                getPhoneConfirmNumSuccess = true;
                timer.start();
            }
        });
    }

    @Override
    public void getPhoneConfirmFail(final String message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getPhoneConfirmNumSuccess = false;
//                svp.showInfoWithStatus(message);
                promptDialog.showInfo(message);
                loginPhone.setClickable(true);
                loginPhone.setText("获取验证码");
                timer.cancel();
//                task.cancel();
            }
        });


    }

    @Override
    public void loginFail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                svp.showInfoWithStatus("登录失败，请稍候重试");
                promptDialog.showInfo("登录失败，请稍候重试");
                loginPhone.setClickable(true);
                loginPhone.setText("获取验证码");
//                取消动画
                timer.cancel();
//                task.cancel();
            }
        });
    }

    @Override
    public void loginSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SharedPreferencesUtil.saveStringData(mContext, "phone", num);
                SharedPreferencesUtil.saveStringData(mContext, "username", ProjectApplication.user.getUsername());
                Intent loginSuccess = new Intent(INTENT_LOGIN_OUT);
                sendBroadcast(loginSuccess);
                if (isBack) {
                    setResult(RESULT_OK);
                } else {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });
    }


    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            loginPhone.setClickable(false);
            loginPhone.setText(millisUntilFinished / 1000 + "(s)");
        }

        @Override
        public void onFinish() {
            loginPhone.setText("获取验证码");
            loginPhone.setClickable(true);

        }
    }
}
