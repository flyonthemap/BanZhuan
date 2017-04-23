package com.shuao.banzhuan.holder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.model.TaskInfo;
import com.shuao.banzhuan.tools.PicassoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by flyonthemap on 2017/4/17.
 */

public class TaskHolder extends BaseRecyclerHolder<TaskInfo>{

    @BindView(R.id.iv_task_icon)
    ImageView ivTaskIcon;
    @BindView(R.id.tv_task_name)
    TextView tvTaskName;
    @BindView(R.id.tv_task_state)
    TextView tvTaskState;
    @BindView(R.id.tv_task_time)
    TextView tvTaskTime;
    @BindView(R.id.fl_task_state)
    FrameLayout flTaskState;
    public TaskHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this,view);

    }

    @Override
    public void refreshView(TaskInfo taskInfo) {
        PicassoUtils.loadImageWithHolder(Config.BASE_IMAGE_URL+"?name="+taskInfo.getTaskIconURL(),R.drawable.ic_default,ivTaskIcon);
        tvTaskName.setText(taskInfo.getTaskName());
        tvTaskTime.setText(taskInfo.getTaskTime());
    }
}
