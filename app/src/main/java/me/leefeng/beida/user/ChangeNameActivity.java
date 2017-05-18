package me.leefeng.beida.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.ProjectApplication;
import me.leefeng.beida.R;
import me.leefeng.beida.bean.User;
import me.leefeng.library.utils.StringUtils;

/**
 * Created by limxing on 2017/5/18.
 */

public class ChangeNameActivity extends BaseActivity {
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.title_tv_right)
    TextView titleTvRight;
    @BindView(R.id.changename_et)
    EditText changenameEt;
    private String username;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        titleName.setText("修改昵称");
        titleTvRight.setVisibility(View.VISIBLE);
        titleTvRight.setText("保存");
        username = ProjectApplication.user.getUsername();
        if (TextUtils.isEmpty(username)) {
            username = "phone_" + ProjectApplication.user.getPhone().substring(7);
        }

        changenameEt.setText(username + "");
        changenameEt.setSelection(username.length());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_changename;
    }

    @Override
    protected void doReceive(Intent action) {

    }


    @OnClick({R.id.title_back, R.id.title_tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                closeInput();
                finish();

                break;
            case R.id.title_tv_right:
                String changeName = changenameEt.getText().toString().trim();
                if (StringUtils.isEmpty(changeName)) {
                    promptDialog.showInfo("姓名不能为空");
                    return;
                }
                if (username.equals(changeName)) {
                    finish();
                    return;
                }
                toChange(changeName);

                break;
        }
    }

    /**
     * 去修改
     *
     * @param changeName
     */
    private void toChange(final String changeName) {
        promptDialog.showLoading("正在修改");

        User user = new User(ProjectApplication.user);
        user.setUsername(changeName);
        user.update(ProjectApplication.user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Intent intent=new Intent(INTENT_LOGIN_OUT);
                    sendBroadcast(intent);
                    ProjectApplication.user.setUsername(changeName);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    promptDialog.showError("修改失败" + e.getErrorCode());
                }
            }
        });

    }

}
