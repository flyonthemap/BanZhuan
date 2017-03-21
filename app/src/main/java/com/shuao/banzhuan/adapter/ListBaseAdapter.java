package com.shuao.banzhuan.adapter;

/**
 * Created by flyonthemap on 16/8/9.
 */

import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.shuao.banzhuan.activity.BaseActivity;
import com.shuao.banzhuan.activity.DetailActivity;
import com.shuao.banzhuan.activity.MainActivity;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.holder.AppHolder;
import com.shuao.banzhuan.holder.BaseHolder;
import com.shuao.banzhuan.model.AppInfo;
import com.shuao.banzhuan.tools.UiTools;

import java.util.List;



public abstract class ListBaseAdapter extends DefaultAdapter<AppInfo> {
    public ListBaseAdapter(List<AppInfo> data,ListView listView) {
        super(data,listView);
        Log.d(Config.DEBUG,"ListBaseAdapter()");
    }

    @Override
    protected BaseHolder<AppInfo> getHolder() {
        Log.d(Config.DEBUG,"getHolder()");
        return null;
    }

    @Override
    protected abstract List<AppInfo> onLoadMore();


    @Override
    public void onInnerItemClick(int position) {
        super.onInnerItemClick(position);
        Toast.makeText(UiTools.getContext(), "position:"+position, Toast.LENGTH_SHORT).show();
        AppInfo appInfo = data.get(position);
        Intent intent=new Intent(UiTools.getContext(), DetailActivity.class);
        intent.putExtra("appID", appInfo.getAppId());
        intent.putExtra("taskID",appInfo.getTaskId());
        // 启动Activity
        if(BaseActivity.activity == null){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            UiTools.getContext().startActivity(intent);
        }else {
            BaseActivity.activity.startActivity(intent);
        }
    }
}
