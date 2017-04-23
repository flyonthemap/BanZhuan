package com.shuao.banzhuan.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.adapter.TaskAdapter;
import com.shuao.banzhuan.holder.DetailInfoHolder;
import com.shuao.banzhuan.holder.DetailScreenHolder;
import com.shuao.banzhuan.model.AppInfo;
import com.shuao.banzhuan.model.TaskInfo;
import com.shuao.banzhuan.protocol.TaskRecordProtocol;
import com.shuao.banzhuan.view.LoadingPage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 任务详情界面
 */
public class TaskActivity extends BaseActivity {

    private LoadingPage mLoadingPage;
    @BindView(R.id.rl_task)
    RecyclerView rlTaskRecyclerView;
    @BindView(R.id.tl_task)
    Toolbar taskToolbar;

    private List<TaskInfo> taskInfos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 创建加载的界面
        mLoadingPage = new LoadingPage(this) {

            @Override
            public View createSuccessView() {
                return TaskActivity.this.onCreateSuccessView();
            }

            @Override
            public LoadResult load() {
                return TaskActivity.this.onLoad();
            }

        };

        setContentView(mLoadingPage);// 直接将一个view对象设置给activity


        // 开始加载网络数据
        mLoadingPage.changeState();

    }

    @Override
    protected void initView() {




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public View onCreateSuccessView() {
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tl_task);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        rlTaskRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        TaskAdapter taskAdapter = new TaskAdapter(this);


        taskAdapter.appendData(taskInfos);
//        exchangeAdapter.appendData(exchangeInfoList);
        rlTaskRecyclerView.setAdapter(taskAdapter);
        // 这里必须要return null，防止布局之间的重叠
        return null;
    }

    public LoadingPage.LoadResult onLoad() {
        // 请求网络,加载数据
        TaskRecordProtocol protocol = new TaskRecordProtocol();
        taskInfos = protocol.load(0);
//        // 根据加载数据的结果
        if (taskInfos != null) {
            return  LoadingPage.LoadResult.success;
        } else {
            return  LoadingPage.LoadResult.error;
        }
    }


}
