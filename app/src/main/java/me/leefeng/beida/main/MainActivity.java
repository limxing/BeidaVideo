package me.leefeng.beida.main;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.BannerView;

import java.io.File;

import butterknife.BindView;
import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.Constants;
import me.leefeng.beida.ProjectApplication;
import me.leefeng.beida.R;
import me.leefeng.beida.bean.Course;
import me.leefeng.beida.down.DownloadingActivity;
import me.leefeng.beida.login.LoginActivity;
import me.leefeng.beida.main.menu.MenuFragment;
import me.leefeng.beida.newplay.NewPlayerActivity;
import me.leefeng.lfrecyclerview.LFRecyclerView;
import me.leefeng.lfrecyclerview.OnItemClickListener;
import me.leefeng.library.utils.LogUtils;
import me.leefeng.library.utils.SharedPreferencesUtil;
import me.leefeng.library.utils.ToastUtils;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;

/**
 * @author FengTing
 * @date 2017/04/24 16:00:40
 */
public class MainActivity extends BaseActivity implements MainView, OnItemClickListener, LFRecyclerView.LFRecyclerViewListener, View.OnClickListener {
    private static final int LOGIN_REQUEST_CODE = 1000;
    @BindView(R.id.main_bt)
    Button mainBt;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.title_right_image)
    ImageView titleRightImage;
    @BindView(R.id.mian_list)
    LFRecyclerView mianList;
    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.main_banner)
    RelativeLayout adBanner;
    @BindView(R.id.main_menu_container)
    FrameLayout mainMenuContainer;
    @BindView(R.id.main_menu)
    DrawerLayout mainMenu;
    private MainPresenter presenter;
    private String phone;
    private final String course = "course.json";
    private boolean isFirst;
    private BannerView banner;
    private MenuFragment menuFragment;

    @Override
    protected void initData() {
        phone = SharedPreferencesUtil.getStringData(mContext, "phone", null);
        presenter = new MainPresenter(this);
//        // 创建Banner广告AdView对象
        banner = new BannerView(this, ADSize.BANNER, Constants.APPID, Constants.BannerPosID);
        banner.setRefresh(30);
        adBanner.addView(banner);
        banner.loadAD();
        if (!new File(getCacheDir(), course).exists()) {
            isFirst = true;
            presenter.initUpdata();
            return;
        }
        presenter.initUpdata();
        if (ProjectApplication.user == null) {
//            svp.showLoading("正在登录");
            promptDialog.showLoading("正在登录", false);
            presenter.login(phone);
        } else {
            initUser();
        }
//        presenter.initUpdata();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
        presenter = null;
        ProjectApplication.user = null;
    }

    @Override
    protected void initView() {
//        titleBack.setVisibility(View.GONE);
//        mianList.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL));
        mianList.setLoadMore(false);
        mianList.setLFRecyclerViewListener(this);
        mianList.setOnItemClickListener(this);
        titleRightImage.setOnClickListener(this);
        titleRightImage.setImageResource(R.drawable.ic_main_share);
        titleRightImage.setVisibility(View.VISIBLE);
        titleBack.setImageResource(R.drawable.ic_main_user);
        titleBack.setOnClickListener(this);

        menuFragment = new MenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_menu_container, menuFragment).commit();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void doReceive(Intent action) {
        if (action.getAction().equals(INTENT_LOGIN_OUT)) {
            menuFragment.loginSuccess();
        }
    }


    @Override
    public void payView(final String sOrderId, final String sVacCode, final Course course) {
        LogUtils.i("orderId:" + sOrderId + "==vacCode:" + sVacCode);
        promptDialog.dismiss();

    }

    @Override
    public void showToast(final String msg) {

        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showLoading(String msg) {
//        svp.showLoading(msg);
        promptDialog.showLoading(msg);
    }

    @Override
    public void svpDismiss() {
//        svp.dismiss();
        promptDialog.dismiss();
    }

    @Override
    public void loginFail() {
//        svp.showInfoWithStatus("登录失败，进入离线模式");
        promptDialog.showWarnAlert("登录失败，进入离线模式", new PromptButton("确定", null));
    }

    @Override
    public void loginSuccess() {
//        svp.dismiss();
        promptDialog.dismiss();
//        initUser();
        menuFragment.loginSuccess();
    }

    @Override
    public void updateCourseSuccess() {
//        svp.showSuccessWithStatus("课程更新完成");
        promptDialog.showSuccess("课程更新完成");
        mianList.stopRefresh(true);
        if (isFirst) {
            isFirst = false;
            if (ProjectApplication.user == null) {
//                svp.showLoading("正在登录");
                promptDialog.showLoading("正在登录");
                presenter.login(phone);
            } else {
                initUser();
            }
        }
    }

    @Override
    public void showErrorWithStatus(String msg) {
//        svp.showErrorWithStatus(msg);
        promptDialog.showError(msg);
        mianList.stopRefresh(false);
    }

    @Override
    public void setAdapter(VideoAdapter adapter) {
        mianList.setAdapter(adapter);

    }

    @Override
    public void openPlayer(int position) {
        Intent intent = new Intent(mContext, NewPlayerActivity.class);
        intent.putExtra("course", position);
        startActivity(intent);
    }

    /**
     * 购买成功
     *
     * @param
     */
    @Override
    public void buySuccess() {
        initUser();
    }

    @Override
    public void stopFresh(final boolean b) {
        titleBack.postDelayed(new Runnable() {
            @Override
            public void run() {
                mianList.stopRefresh(b);
            }
        }, 500);

        promptDialog.dismissImmediately();
        if (b) banner.loadAD();
    }

    @Override
    public void showGoldDialog(final Course course, final int money) {
        PromptButton confirm = new PromptButton("支付", new PromptButtonListener() {
            @Override
            public void onClick(PromptButton promptButton) {

                promptDialog.showLoading("正在支付");
                presenter.buyWithGold(course, money);
            }
        });
        confirm.setTextColor(getResources().getColor(R.color.colorAccent));
        confirm.setFocusBacColor(Color.parseColor("#FFEFD5"));
        confirm.setDismissAfterClick(false);
        promptDialog.showWarnAlert("您将消费" + money + "金币购买" + course.getName(),
                new PromptButton("取消", null), confirm);
    }

    @Override
    public void showLoading(String msg, boolean withAnim) {
        promptDialog.showLoading(msg, false);
    }

    private void initUser() {

        Long account = ProjectApplication.user.getAccount();
//        if (account == null) {
//            titleTvRight.setText(0 + "");
//        } else {
//            titleTvRight.setText(account.intValue() + "");
//        }
//        svp.showLoading("正在获取购买信息");
//        promptDialog.showLoading("正在获取购买信息", false);
//        presenter.getCourseList();

    }


    /**
     * RecycleView的点击
     *
     * @param position
     */
    @Override
    public void onClick(int position) {
        LogUtils.i("list click:" + position);
        openPlayer(position);
//        if (presenter.isBuy(position)) {
//            openPlayer(position);
//            return;
//        }
//        if (ProjectApplication.user == null) {
//            //去登陆
//            goLogin();
//        } else {
//            presenter.toBuy(position);
//        }
    }

    private void goLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra("isBack", true);
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.i("onActivityResult:" + requestCode + "==" + resultCode + "==");
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case LOGIN_REQUEST_CODE:
//                    initUser();
//                    loginSuccess();
                    break;
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * RecycleView的长安
     *
     * @param
     */
    @Override
    public void onLongClick(int po) {

    }

    /**
     * RecycleView的下拉刷新
     *
     * @param
     */
    @Override
    public void onRefresh() {
        if (isFirst) {
            presenter.initUpdata();
        } else if (ProjectApplication.user == null) {
            ToastUtils.showLong(mContext, "请先登录");
            goLogin();
            mianList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mianList.stopRefresh(false);
                }
            }, 1000);
        } else {
//            initUser();
            presenter.initUpdata();
        }


    }

    /**
     * RecycleView的上拉加载
     *
     * @param
     */
    @Override
    public void onLoadMore() {

    }


    /**
     * 按钮的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right_image:
//                if (ProjectApplication.user == null) {
//                    ToastUtils.showLong(mContext, "请先登录");
//                    goLogin();
//                    return;
//                }
//                Intent intent = new Intent(mContext, DownloadingActivity.class);
//                startActivity(intent);
                break;
            case R.id.title_back:
                mainMenu.openDrawer(Gravity.START);
                break;
        }
    }


    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if (promptDialog.onBackPressed()) {
            if (mainMenu.isDrawerOpen(Gravity.START)) {
                mainMenu.closeDrawers();
            } else if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }

    }

}
