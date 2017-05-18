package me.leefeng.beida.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.ProjectApplication;
import me.leefeng.beida.R;
import me.leefeng.library.utils.ToastUtils;
import me.leefeng.library.view.ItemView;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;

/**
 * @author FengTing
 * @date 2017/05/17 14:58:49
 */
public class UserActivity extends BaseActivity implements UserView {
    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.user_head)
    CircleImageView userHead;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_item_name)
    ItemView userItemName;
    @BindView(R.id.user_item_phone)
    ItemView userItemPhone;
    @BindView(R.id.user_item_isbeida)
    ItemView userItemIsbeida;
    @BindView(R.id.user_overscoll)
    LinearLayout userOverscoll;
    @BindView(R.id.title_bac)
    LinearLayout titleBac;
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
        titleBac.setBackgroundColor(Color.TRANSPARENT);
        titleName.setVisibility(View.GONE);
        titleName.setText("个人信息");
        initUsername();
        OverScrollDecoratorHelper.setUpStaticOverScroll(userOverscoll, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_user;
    }

    @Override
    protected void doReceive(Intent action) {

    }


    @OnClick(R.id.title_back)
    public void onViewClicked() {
        finish();
    }

    private void initUsername() {
        String username = null;
        if (ProjectApplication.user != null) {
            username = ProjectApplication.user.getUsername();
            if (TextUtils.isEmpty(username)) {
                username = "phone_" + ProjectApplication.user.getPhone().substring(7);
            }
            boolean isBeida = ProjectApplication.user.isBeida();
            if (isBeida) {
                userItemIsbeida.getValueTextView().setText("认证学员");
            }
            userItemPhone.getValueTextView().setText(ProjectApplication.user.getPhone());

        }
        if (username != null) {
            userName.setText(username);
            userItemName.getValueTextView().setText(username);
        }
    }


    @OnClick({R.id.user_head, R.id.user_item_name, R.id.user_item_phone, R.id.user_item_isbeida, R.id.user_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_head:
                break;
            case R.id.user_item_name:
                break;
            case R.id.user_item_phone:
                break;
            case R.id.user_item_isbeida:
                ToastUtils.showShort(this, "认证");
                break;
            case R.id.user_logout:
                showLogoutDialog();
                break;
        }
    }

    private void showLogoutDialog() {
        PromptButton confirm = new PromptButton("退出", new PromptButtonListener() {
            @Override
            public void onClick(PromptButton promptButton) {
                ProjectApplication.user = null;
                Intent brodcast = new Intent(INTENT_LOGIN_OUT);
                sendBroadcast(brodcast);
                finish();
            }
        });
        confirm.setTextColor(getResources().getColor(R.color.colorAccent));
        confirm.setFocusBacColor(Color.parseColor("#40961319"));
        confirm.setDelyClick(true);
        promptDialog.showWarnAlert("确定要退出吗？", new PromptButton("取消", null), confirm);

    }


}
