package me.leefeng.beida.set;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.R;

/**
 * Created by limxing on 2017/5/20.
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        titleName.setText("关于");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_about;
    }

    @Override
    protected void doReceive(Intent action) {

    }


    @OnClick(R.id.title_back)
    public void onViewClicked() {
        finish();
    }
}
