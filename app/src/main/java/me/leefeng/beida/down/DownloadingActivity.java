package me.leefeng.beida.down;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.R;
import me.leefeng.beida.download.DownLoadListener;
import me.leefeng.beida.download.DownLoadManager;
import me.leefeng.beida.download.DownLoadService;
import me.leefeng.beida.download.TaskInfo;
import me.leefeng.beida.download.dbcontrol.SQLDownLoadInfo;
import me.leefeng.library.utils.LogUtils;
import me.leefeng.library.utils.ToastUtils;
import me.leefeng.library.view.FailView;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;


/**
 * Created by limxing on 2016/11/4.
 */

public class DownloadingActivity extends BaseActivity implements DownLoadListener {
    private RecyclerView recycleView;
    private DownloadingAdapter adapter;
    private DownLoadManager downManager;
    private ArrayList<TaskInfo> taskList;
//    private FailView failview;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
//        failview=(FailView)findViewById(R.id.downloading_failview);
//        failview.setMode(FailView.MODE_REFRESH);
        ((TextView) findViewById(R.id.title_name)).setText("下载管理");
        recycleView = (RecyclerView) findViewById(R.id.downloading_recycleview);
        downManager = DownLoadService.getDownLoadManager();
        taskList = downManager.getAllTask();
        LogUtils.i(this, taskList.size() + "");
        downManager.setAllTaskListener(this);
        adapter = new DownloadingAdapter(taskList);
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this));


        findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recycleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                failView();
            }
        }, 300);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_downloading;
    }


    @Override
    public void onStart(SQLDownLoadInfo sqlDownLoadInfo) {
        for (int i = 0; i < taskList.size(); i++) {
            if (sqlDownLoadInfo.getTaskID().equals(taskList.get(i).getTaskID())) {
                taskList.get(i).setOnDownloading(true);
//                adapter.notifyItemChanged(i);
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void onProgress(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {
        for (int i = 0; i < taskList.size(); i++) {
            if (sqlDownLoadInfo.getTaskID().equals(taskList.get(i).getTaskID())) {
                taskList.get(i).setDownFileSize(sqlDownLoadInfo.getDownloadSize());
//                adapter.notifyItemChanged(i);
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void onStop(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {
        for (int i = 0; i < taskList.size(); i++) {
            if (sqlDownLoadInfo.getTaskID().equals(taskList.get(i).getTaskID())) {
                taskList.get(i).setOnDownloading(false);
//                adapter.notifyItemChanged(i);
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void onError(SQLDownLoadInfo sqlDownLoadInfo) {
        for (int i = 0; i < taskList.size(); i++) {
            if (sqlDownLoadInfo.getTaskID().equals(taskList.get(i).getTaskID())) {
                taskList.get(i).setOnDownloading(false);
//                adapter.notifyItemChanged(i);
                ToastUtils.showShort(mContext, "文件不存在");
                adapter.notifyDataSetChanged();
                return;
            }
        }
        failView();
    }

    public void failView() {
        if (taskList.size() == 0) {
//            failview.setMode(FailView.MODE_NONET);
            PromptButton confirm = new PromptButton("确定", new PromptButtonListener() {
                @Override
                public void onClick(PromptButton promptButton) {
                    finish();
                }
            });
            confirm.setDelyClick(true);
            promptDialog.showWarnAlert("没有下载任务", confirm);
        }
    }

    @Override
    public void onSuccess(SQLDownLoadInfo sqlDownLoadInfo) {
        for (int i = 0; i < taskList.size(); i++) {
            if (sqlDownLoadInfo.getTaskID().equals(taskList.get(i).getTaskID())) {
                taskList.remove(i);
//                adapter.notifyItemMoved(i, taskList.size());
                adapter.notifyDataSetChanged();
                return;
            }
        }

        failView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        downManager.removeAllDownLoadListener();
    }

    @Override
    protected void doReceive(Intent action) {

    }
}
