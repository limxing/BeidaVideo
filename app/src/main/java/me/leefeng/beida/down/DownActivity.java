package me.leefeng.beida.down;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.ProjectApplication;
import me.leefeng.beida.R;
import me.leefeng.beida.bean.Course;
import me.leefeng.beida.download.dbcontrol.FileHelper;
import me.leefeng.library.utils.ToastUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by limxing on 2016/11/4.
 */

public class DownActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private VideoAdapter adapter;
    private List<Course> cList;
    private TextView title_name;

    @Override
    protected void initData() {
        findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void initView() {
        cList = new ArrayList<Course>();
        adapter = new VideoAdapter(cList);
        ListView listView = (ListView) findViewById(R.id.down_listview);
        title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText("离线课程");
        ImageView title_iamge = (ImageView) findViewById(R.id.title_right_image);
        title_iamge.setVisibility(View.VISIBLE);
        title_iamge.setImageResource(R.drawable.downloading);
        title_iamge.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        initListData();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_down;
    }

    @Override
    protected void doReceive(Intent action) {
        if (action.getAction().equals("me.leefeng.down")) {
            initListData();
        }
    }

    private void initListData() {
        Observable.create(new Observable.OnSubscribe<List<Course>>() {
            @Override
            public void call(Subscriber<? super List<Course>> subscriber) {
                List<Course> list = new ArrayList<Course>();
                File file = new File(FileHelper.getFileDefaultPath());
                File[] files = file.listFiles();
                if (files.length > 0) {
                    for (File f : files) {
                        if (f.isDirectory()) {
                            for (Course course : ProjectApplication.cList) {
                                if (course.getId().equals(f.getName()) && hasMp4(f)) {
                                    list.add(course);
                                }
                            }
                        }
                    }
                }
                subscriber.onNext(list);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Course>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Course> courses) {
                        cList = courses;
                        adapter.setList(cList);
                        adapter.notifyDataSetChanged();
                    }
                });

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ToastUtils.showShort(mContext, "" + i);
        Intent intent = new Intent(this, DownCourseActivity.class);
        String s = cList.get(i).getId();
        intent.putExtra("id", s);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, DownloadingActivity.class);
        startActivity(intent);
    }

    private boolean hasMp4(File file) {
        boolean b = false;
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.isDirectory() && file1.list().length > 0) {
                b = true;
                break;
            }
        }
        return b;
    }
}
