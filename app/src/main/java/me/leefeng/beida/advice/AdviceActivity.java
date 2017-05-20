package me.leefeng.beida.advice;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.ProjectApplication;
import me.leefeng.beida.R;
import me.leefeng.beida.bean.Advice;
import me.leefeng.beida.login.LoginActivity;
import me.leefeng.library.utils.StringUtils;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by limxing on 2017/5/20.
 */

public class AdviceActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.title_tv_right)
    TextView titleTvRight;
    @BindView(R.id.advice_et)
    EditText adviceEt;
    @BindView(R.id.advice_length)
    TextView adviceLength;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        titleName.setText("建议反馈");
        titleTvRight.setText("提交");
        titleTvRight.setVisibility(View.VISIBLE);
        adviceEt.addTextChangedListener(this);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_advice;
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
                send();
                break;
        }
    }

    private void send() {
        final String content = adviceEt.getText().toString().trim();
        if (StringUtils.isEmpty(content)) {
            promptDialog.showInfo("请填写内容");
            return;
        }
        if (ProjectApplication.user == null) {
            promptDialog.getAlertDefaultBuilder().touchAble(false);
            promptDialog.showWarnAlert("请先登录", new PromptButton("取消", null),
                    new PromptButton("登录", new PromptButtonListener() {
                        @Override
                        public void onClick(PromptButton promptButton) {
                            Intent i = new Intent(mContext, LoginActivity.class);
                            startActivity(i);
                        }
                    }));
            return;
        }
        promptDialog.showLoading("正在提交");


        Advice advice = new Advice();
        advice.setContent(content);
        advice.setUser(ProjectApplication.user);
        advice.saveObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        promptDialog.showError("提交失败，请重试");
                    }

                    @Override
                    public void onNext(String s) {
                        promptDialog.showSuccess("提交成功");
                        titleName.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1300);
                    }
                });

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        adviceLength.setText(editable.length() + "/200");
    }
}
