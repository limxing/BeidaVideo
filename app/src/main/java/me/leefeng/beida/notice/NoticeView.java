package me.leefeng.beida.notice;


/**
 * @author FengTing
 * @date 2017/05/17 16:39:04
 */
public interface NoticeView {
    void setAdapter(NoticeAdapter adapter);

    void initSuccess();

    void initSuccessNone();

}
