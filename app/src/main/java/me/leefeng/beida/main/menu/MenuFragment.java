package me.leefeng.beida.main.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.BannerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.leefeng.beida.Constants;
import me.leefeng.beida.ProjectApplication;
import me.leefeng.beida.R;
import me.leefeng.beida.login.LoginActivity;
import me.leefeng.beida.user.UserActivity;
import me.leefeng.library.utils.LogUtils;
import me.leefeng.library.utils.SharedPreferencesUtil;
import me.leefeng.library.utils.ToastUtils;

/**
 * Created by FengTing on 2017/5/17.
 * https://www.github.com/limxing
 */

public class MenuFragment extends Fragment implements AdapterView.OnItemClickListener, MenuAdapter.MenuItenClickListener {
    private List<MenuItem> list;
    @BindView(R.id.menu_head)
    CircleImageView menuHead;
    @BindView(R.id.menu_username)
    TextView menuUsername;
    @BindView(R.id.menu_cb)
    CheckBox menuCb;
    @BindView(R.id.menu_user_rl)
    RelativeLayout menuUserRl;
    @BindView(R.id.menu_listview)
    RecyclerView menuListview;
    @BindView(R.id.menu_ad_container)
    RelativeLayout menuAd;

    Unbinder unbinder;
    private MenuAdapter adapter;
    private BannerView banner;

    public MenuFragment() {
        list = new ArrayList<MenuItem>();

    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i("MenuFragment:onResume");
        if (list.size() == 0) {
//            String[] ss = getResources().getStringArray(R.array.menu);
//            for (String sss : ss) {
            MenuItem menuItem = new MenuItem();
            menuItem.setName("下载管理");
            menuItem.setIcon(R.drawable.ic_menu_download);
            menuItem.setClick("me.leefeng.beida.down.DownloadingActivity");
            list.add(menuItem);
            menuItem = new MenuItem();
            menuItem.setName("消息通知");
            menuItem.setClick("me.leefeng.beida.notice.NoticeActivity");
            menuItem.setIcon(R.drawable.ic_menu_notice);
            list.add(menuItem);
            menuItem = new MenuItem();
            menuItem.setName("建议反馈");
            menuItem.setClick("me.leefeng.beida.notice.NoticeActivity");
            menuItem.setIcon(R.drawable.ic_menu_advice);
            list.add(menuItem);
            menuItem = new MenuItem();
            menuItem.setName("设置");
            menuItem.setClick("me.leefeng.beida.set.SetActivity");
            menuItem.setIcon(R.drawable.ic_menu_set);
            list.add(menuItem);
//            }
        }
        adapter.notifyDataSetChanged();
//        banner.loadAD();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mian_menu, null);
        unbinder = ButterKnife.bind(this, view);

        adapter = new MenuAdapter(list, this);
        menuListview.setLayoutManager(new LinearLayoutManager(container.getContext()));
        menuListview.setAdapter(adapter);
        OverScrollDecoratorHelper.setUpOverScroll(menuListview, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        // 创建Banner广告AdView对象
//         banner = new BannerView(getActivity(), ADSize.BANNER, Constants.APPID, Constants.BannerPosID);
//        banner.setRefresh(30);
//        menuAd.addView(banner);
        initUsername();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.menu_user_rl)
    public void onViewClicked() {
        Intent intent = null;
        if (ProjectApplication.user == null) {
            intent = new Intent(getContext(), LoginActivity.class);
            intent.putExtra("isBack", true);
        } else {
            intent = new Intent(getContext(), UserActivity.class);
        }
        startActivity(intent);
    }

    /**
     * 条目点击
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * 菜单点击事件
     *
     * @param position
     */
    @Override
    public void onMenuItemClick(int position) {
        MenuItem menuItem = list.get(position);
        Class<?> click = null;
        try {
            click = Class.forName(menuItem.getClick());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (click == null) {
            ToastUtils.showShort(getContext(), "找不到界面");
        } else {
            Intent intent = new Intent(getContext(), click);
            startActivity(intent);
        }
    }

    public void loginSuccess() {
        initUsername();
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
                menuCb.setText("认证学员");
            }
            menuCb.setChecked(isBeida);
        } else {
            menuCb.setText("未认证学员");
            menuCb.setChecked(false);
        }
        if (username == null)
            username = "未登录";
        menuUsername.setText(username);
    }
}
