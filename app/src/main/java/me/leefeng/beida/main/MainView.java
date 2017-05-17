package me.leefeng.beida.main;


import me.leefeng.beida.bean.Course;

/**
 * @author FengTing
 * @date 2017/04/24 16:00:40
 */
public interface MainView {
    void payView(String orderId, String sOrderId, Course sVacCode);

    void showToast(String msg);

    void showLoading(String msg);

    void svpDismiss();

    void loginFail();

    void loginSuccess();

    void updateCourseSuccess();

    void showErrorWithStatus(String msg);

    void setAdapter(VideoAdapter adapter);

    void openPlayer(int position);

    void buySuccess();

    void stopFresh(boolean b);

    void showGoldDialog(Course course,int money);

    void showLoading(String msg, boolean withAnim);
}
