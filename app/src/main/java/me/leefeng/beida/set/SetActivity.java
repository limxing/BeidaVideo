package me.leefeng.beida.set;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.xiaomi.mipush.sdk.MiPushClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.ProjectApplication;
import me.leefeng.beida.R;
import me.leefeng.library.utils.SharedPreferencesUtil;
import me.leefeng.library.view.ItemView;
import me.leefeng.library.view.SwitchView;

/**
 * @author FengTing
 * @date 2017/05/17 16:39:26
 */
public class SetActivity extends BaseActivity implements SetView {
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.set_push_switch)
    SwitchButton setPushSwitch;
    @BindView(R.id.set_scrollview)
    ScrollView setScrollview;
    private SetPresenter presenter;

    @Override
    protected void initData() {
        presenter = new SetPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
        presenter = null;
    }

    @Override
    protected void initView() {
        titleName.setText("设置");
        OverScrollDecoratorHelper.setUpOverScroll(setScrollview);
        if (SharedPreferencesUtil.getBooleanData(mContext, "push", true)) {
            setPushSwitch.setChecked(true);

        }
        setPushSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    MiPushClient.disablePush(ProjectApplication.getContext());
                    SharedPreferencesUtil.saveBooleanData(mContext, "push", true);
                }else{
                    MiPushClient.enablePush(ProjectApplication.getContext());
                    SharedPreferencesUtil.saveBooleanData(mContext, "push", false);
                }
            }
        });



    }

    @Override
    protected int getContentView() {
        return R.layout.activity_set;
    }

    @Override
    protected void doReceive(Intent action) {

    }


    @OnClick({R.id.title_back, R.id.set_item_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.set_item_about:
                Intent intent=new Intent(mContext,AboutActivity.class);
                startActivity(intent);
                break;

        }
    }


}
