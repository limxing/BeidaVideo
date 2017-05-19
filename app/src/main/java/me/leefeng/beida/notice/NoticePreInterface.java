package me.leefeng.beida.notice;

import java.util.List;

import me.leefeng.beida.dbmodel.NoticeMessage;

/**
 * @author FengTing
 * @date 2017/05/17 16:39:04
 */
public interface NoticePreInterface {
     void destory();

    void initData();

    List<NoticeMessage> getList();

    void setPositionRead(int position);
}
