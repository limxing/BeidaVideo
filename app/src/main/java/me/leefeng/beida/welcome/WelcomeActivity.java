package me.leefeng.beida.welcome;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.litesuits.orm.log.OrmLog;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.BuildConfig;
import me.leefeng.beida.Constants;
import me.leefeng.beida.ProjectApplication;
import me.leefeng.beida.R;
import me.leefeng.beida.bean.BuyData;
import me.leefeng.beida.bean.User;
import me.leefeng.beida.dbmodel.DBBuy;
import me.leefeng.beida.login.LoginActivity;
import me.leefeng.beida.main.MainActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import me.leefeng.library.utils.LogUtils;
import me.leefeng.library.utils.SharedPreferencesUtil;
import me.leefeng.library.utils.StringUtils;
import me.leefeng.library.utils.ToastUtils;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * @author FengTing
 * @@date 2017/04/25 10:39:52
 */

public class WelcomeActivity extends BaseActivity implements WelcomeView, EasyPermissions.PermissionCallbacks, SplashADListener {
    private static final int RC_PERM = 10000;
    @BindView(R.id.welcome_bottom)
    TextView welcomeBottom;
    @BindView(R.id.welcome_splash)
    ImageView mainSplash;
    private static final String SKIP_TEXT = "点击跳过 %d";
    private WelcomePresenter presenter;
    private String phone;
    private ViewGroup container;
    private TextView skipView;
    private SplashAD splashAD;

    @Override
    protected void initData() {
        phone = SharedPreferencesUtil.getStringData(mContext, "phone", null);
        presenter = new WelcomePresenter(this);
        welcomeBottom.setText(getResources().getString(R.string.welcome_bottom) + " V " + BuildConfig.VERSION_NAME);
        welcomeBottom.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermission(new CheckPermListener() {
                                    @Override
                                    public void superPermission() {
                                        permission();
                                    }
                                }, "需要权限处理", Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION);

            }
        }, 2000);

        List<DBBuy> list = ProjectApplication.liteOrm.query(DBBuy.class);
        OrmLog.i("leefeng", list);
        List<BuyData> buyDatas = new ArrayList<>();
        for (DBBuy dbBuy : list) {
            BuyData buyData = new BuyData();
            buyData.setCourseid(dbBuy.getCourseid());
            User user = new User();
            user.setPhone(dbBuy.getUsername());
            buyData.setUser(user);
            buyDatas.add(buyData);
        }
        ProjectApplication.buyList = buyDatas;


    }

    /**
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     *
     * @param activity      展示广告的activity
     * @param adContainer   展示广告的大容器
     * @param skipContainer 自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的样式可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或下面的注意事项。
     * @param appId         应用ID
     * @param posId         广告位ID
     * @param adListener    广告状态监听器
     * @param fetchDelay    拉取广告的超时时长：取值范围[3000, 5000]，设为0表示使用广点通SDK默认的超时时长。
     */
    private void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
                               String appId, String posId, SplashADListener adListener, int fetchDelay) {
        splashAD = new SplashAD(activity, adContainer, skipContainer, appId, posId, adListener, fetchDelay);
    }

    @Override
    public void onADPresent() {
        Log.i("AD_DEMO", "SplashADPresent");
        mainSplash.setVisibility(View.INVISIBLE); // 广告展示后一定要把预设的开屏图片隐藏起来
    }


    private void next() {
        Intent intent = null;
        if (StringUtils.isEmpty(phone)) {
            intent = new Intent(mContext, LoginActivity.class);
        } else {
            intent = new Intent(mContext, MainActivity.class);
        }
        startActivity(intent);
        finish();
//        if (StringUtils.isEmpty(phone)) {
//            mainSplash.setVisibility(View.VISIBLE);
//            mainSplash.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        } else {
//
//
//            Intent intent = null;
//            if (StringUtils.isEmpty(phone)) {
//                intent = new Intent(mContext, LoginActivity.class);
//            } else {
//                intent = new Intent(mContext, MainActivity.class);
//            }
//            startActivity(intent);
//            finish();
//        }

    }

    @Override
    public void onADDismissed() {
        Log.i("AD_DEMO", "SplashADDismissed");
        next();
    }

    @Override
    public void onNoAD(int arg0) {
        Log.i("AD_DEMO", "LoadSplashADFail,ecode=" + arg0);
        next();
    }


    @Override
    public void onADClicked() {
        Log.i("AD_DEMO", "SplashADClicked");
    }

    /**
     * 倒计时回调，返回广告还将被展示的剩余时间。
     * 通过这个接口，开发者可以自行决定是否显示倒计时提示，或者还剩几秒的时候显示倒计时
     *
     * @param millisUntilFinished 剩余毫秒数
     */
    @Override
    public void onADTick(long millisUntilFinished) {
        Log.i("AD_DEMO", "SplashADTick " + millisUntilFinished + "ms");
        skipView.setText(String.format(SKIP_TEXT, Math.round(millisUntilFinished / 1000f)));
    }

    //防止用户返回键退出APP
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void permission() {
        fetchSplashAD(this, container, skipView, Constants.APPID, Constants.SplashPosID, this, 3000);

//        if (ProjectApplication.mApplication == null)
//            initUnipay();

//        String s = Build.MODEL + "==" + Build.VERSION.RELEASE + "==" + Build.VERSION.SDK_INT;
//        if (Build.VERSION.SDK_INT < 21) {
//            PromptDialog promptDialog = new PromptDialog(this);
//            promptDialog.getAlertDefaultBuilder().cancleAble(false);
//            promptDialog.showWarnAlert("当前Android版本：" + Build.VERSION.RELEASE + "\n" +
//                    "最低支持Android 5.0", new PromptButton("确定", new PromptButtonListener() {
//                @Override
//                public void onClick(PromptButton promptButton) {
//                    finish();
//                }
//            }));
//            return;
//        }
//        ToastUtils.showLong(mContext, s);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
        presenter = null;
    }

    @Override
    protected void initView() {
//        mCountdownView.setText("计时");
//        mCountdownView.setTime(5000);
//        mCountdownView.star();
//
//        mCountdownView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                permission();
//            }
//        });
        //广告
        container = (ViewGroup) this.findViewById(R.id.splash_container);
        skipView = (TextView) findViewById(R.id.skip_view);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void doReceive(Intent action) {

    }


    /**
     * 权限回调接口
     */
    private CheckPermListener mListener;

    /**
     * 检查权限
     *
     * @param listener  全县坚挺
     * @param resString 全县提示
     * @param mPerms    全县内容
     */
    public void checkPermission(CheckPermListener listener, String resString, String... mPerms) {
        mListener = listener;
        if (EasyPermissions.hasPermissions(this, mPerms)) {
            if (mListener != null)
                mListener.superPermission();
        } else {
            EasyPermissions.requestPermissions(this, resString,
                    RC_PERM, mPerms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (mListener != null) {
            mListener.superPermission();//同意了全部权限的回调
//            finish();

//            ToastUtils.showLong(mContext,"正在初始化");
//            Intent intent = getBaseContext().getPackageManager()
//                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
//            PendingIntent restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
//            AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10, restartIntent); // 1秒钟后重启应用
//            System.exit(0);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        finish();
    }

    //


}
