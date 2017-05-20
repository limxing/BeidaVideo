package me.leefeng.beida.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.R;
import me.leefeng.beida.bean.User;
import me.leefeng.beida.login.LoginActivity;
import me.leefeng.library.utils.LogUtils;
import me.leefeng.library.utils.StringUtils;
import me.leefeng.library.utils.ToastUtils;
import me.leefeng.promptlibrary.PromptButton;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by limxing on 2017/5/20.
 */

public class ChangePhoneActivity extends BaseActivity {
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.changephone_phone_et)
    MaterialEditText changephonePhoneEt;
    @BindView(R.id.changephone_yan_et)
    MaterialEditText changephoneYanEt;
    @BindView(R.id.changephone_yan)
    TextView changePhoneYan;
    private String num;
    private boolean getPhoneConfirmNumSuccess;
    private TimeCount timer;

    @Override
    protected void initData() {
        timer = new TimeCount(60000, 1000);
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, final Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        LogUtils.i("提交成功");
                        //提交验证码成功
                        setResult(RESULT_OK);
                        finish();

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        LogUtils.i("下发成功");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                svp.showInfoWithStatus(msg);
                                promptDialog.showInfo("验证码下发成功");
                                getPhoneConfirmNumSuccess = true;
                                timer.start();
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    String message = null;
                    try {
                        message = JSON.parseObject(((Throwable) data).getMessage()).getString("detail");
                    } catch (Exception e) {
                        message = "网络异常，请稍后重试";
                    }
                    promptDialog.showError(message);
                    ((Throwable) data).printStackTrace();
                    LogUtils.i(getClass(), ((Throwable) data).getMessage());

                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    @Override
    protected void initView() {
        titleName.setText("修改手机号");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_changephone;
    }

    @Override
    protected void doReceive(Intent action) {

    }


    @OnClick({R.id.title_back, R.id.changephone_yan, R.id.changephone_bt_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                closeInput();
                finish();
                break;
            case R.id.changephone_yan:
                getYanMa();
                break;
            case R.id.changephone_bt_change:
                toYan();
                break;
        }
    }

    private void toYan() {
        num = changephonePhoneEt.getText().toString().trim();
        if (StringUtils.isEmpty(num)) {
            ToastUtils.showShort(mContext, "请输入手机号");
            return;
        }
        if (!getPhoneConfirmNumSuccess) {
            ToastUtils.showShort(mContext, "请获取验证码");
            return;
        }
        final String confirmNUm = changephoneYanEt.getText().toString().trim();
        if (StringUtils.isEmpty(confirmNUm)) {
            ToastUtils.showShort(mContext, "请输入验证码");
            return;
        }
        closeInput();
        promptDialog.showLoading("正在登录");
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("phone", num);
        query.findObjectsObservable(User.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();

                        promptDialog.showWarnAlert("请点击验证重试", new PromptButton("确定", null));
                    }

                    @Override
                    public void onNext(List<User> list) {
                        if (list.size() == 0) {
                            SMSSDK.submitVerificationCode("86", num, confirmNUm);

                        } else {
                            promptDialog.showWarnAlert("手机号已存在，换一个试试", new PromptButton("确定", null));
                        }
                    }
                });


    }

    private void getYanMa() {
        num = changephonePhoneEt.getText().toString().trim();
        String telRegex = "[1][34578]\\d{9}";
        if (num.length() == 11 && num.matches(telRegex)) {
            closeInput();
//            svp.showLoading("正在获取验证码");
            promptDialog.showLoading("正在获取验证码");
            SMSSDK.getVerificationCode("86", num);
        } else {
            ToastUtils.showLong(mContext, "手机格式错误");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;

    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            changePhoneYan.setClickable(false);
            changePhoneYan.setText(millisUntilFinished / 1000 + "(s)");
        }

        @Override
        public void onFinish() {
            changePhoneYan.setText("获取验证码");
            changePhoneYan.setClickable(true);

        }
    }
}
