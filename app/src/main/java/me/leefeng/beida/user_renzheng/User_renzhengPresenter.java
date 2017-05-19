package me.leefeng.beida.user_renzheng;

import java.io.IOException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import me.leefeng.beida.BasePresenter;
import me.leefeng.beida.ProjectApplication;
import me.leefeng.beida.bean.User;
import me.leefeng.beida.utils.SecUtil;
import me.leefeng.library.utils.LogUtils;
import me.leefeng.library.utils.StringUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author FengTing
 * @date 2017/05/18 13:13:57
 */
public class User_renzhengPresenter extends BasePresenter implements User_renzhengPreInterface {
    private User_renzhengView user_renzhengView;
    private String accountStr;

    public User_renzhengPresenter(User_renzhengView user_renzhengView) {
        this.user_renzhengView = user_renzhengView;
    }

    @Override
    public void destory() {
        user_renzhengView = null;
    }

    @Override
    public void renzheng(final String id, final String pass, final boolean isRenzheng) {

        RequestBody formBody = new FormBody.Builder()
                .add("myID", id)
                .add("myPW", pass)
                .add("usersf", "1")
                .build();

        Call call = new OkHttpClient.Builder().build().newCall(new Request.Builder()
                .url("http://learn.pkudl.cn")
                .post(formBody)
                .build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (user_renzhengView == null) return;
                if (isRenzheng) {
                    user_renzhengView.showFail("认证失败，请重试");
                } else {
                    user_renzhengView.initAccountFail();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (user_renzhengView == null) return;
                accountStr = response.body().string();
                LogUtils.i(accountStr);
                if (isRenzheng) {//认证
                    if (accountStr.contains("用户名错误")) {
                        user_renzhengView.showFail("登录失败，用户名错误");
                    } else if (accountStr.contains("密码错误")) {
                        user_renzhengView.showFail("登录失败，密码错误");
                    } else {
                        int i = accountStr.indexOf("<div class=\"head_wid1\">");
                        String realName = accountStr.substring(i + 23, i + 27).trim();
                        i = accountStr.indexOf("http://202.152.177.118/zsphoto");
                        String headPic = accountStr.substring(i, i + 67);
                        i = accountStr.indexOf("专&nbsp;&nbsp;&nbsp;业</strong>：");
                        String ss = accountStr.substring(i);
                        int last = ss.indexOf("</td>");
                        String subject = ss.substring("专&nbsp;&nbsp;&nbsp;业</strong>：".length(), last).trim();

                        final User user = new User(ProjectApplication.user);
                        user.setRealName(realName);
                        user.setHeadPic(headPic);
                        user.setBeida(true);
                        user.setBdAccount(id);
                        user.setSubject(subject);
                        String passw = SecUtil.AESEncrypt(pass, SecUtil.getRawKey());

                        user.setBdPassword(passw);
                        user.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (checkBmobException(e, user_renzhengView)) {
                                    ProjectApplication.user = user;
                                    user_renzhengView.renzhengSuccess();
                                }
                            }
                        });

                    }
                } else {
                    //登录
                    initAccountStr();
                }
            }
        });

    }

    /**
     * 获取北大信息
     */
    @Override
    public void getAccountInfo() {
        if (StringUtils.isEmpty(accountStr)) {//去认证或者登录
            renzheng(ProjectApplication.user.getBdAccount(),
                    SecUtil.AESDecrypt(ProjectApplication.user.getBdPassword(), SecUtil.getRawKey()), false);
        } else {
            initAccountStr();
        }
    }

    private void initAccountStr() {
        int i = accountStr.indexOf("报名号</strong>");
        String bmh = accountStr.substring(i + 13, i + 28).trim();
        String current = "学&nbsp;&nbsp;&nbsp;号</strong>：";
        i = accountStr.indexOf(current);
        String xh = accountStr.substring(i + current.length(), i + current.length() + 14).trim();
        i = accountStr.indexOf("已经获得");
        int j = accountStr.indexOf("</td>", i);
        String xf = accountStr.substring(i, j);

        user_renzhengView.initAccountSuccess(bmh, xh, xf);


    }
}
