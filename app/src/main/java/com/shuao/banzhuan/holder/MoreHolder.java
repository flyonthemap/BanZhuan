package com.shuao.banzhuan.holder;

/**
 * Created by flyonthemap on 16/8/9.
 */

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.adapter.DefaultAdapter;
import com.shuao.banzhuan.tools.UiTools;


public class MoreHolder extends BaseHolder<Integer> {
    // 没有额外数据了
    public static final int HAS_NO_MORE = 0;
    // 加载失败
    public static final int LOAD_ERROR = 1;
    // 有额外数据
    public static final int HAS_MORE = 2;

    private boolean  hasMore;
    private RelativeLayout rl_more_loading,rl_more_error;
    private TextView tv_no_more;

    // 当Holder显示的时候，显示的视图
    @Override
    public View initView() {
        View view = UiTools.inflate(R.layout.load_more);
        rl_more_loading = (RelativeLayout) view.findViewById(R.id.rl_more_loading);
        rl_more_error = (RelativeLayout) view.findViewById(R.id.rl_more_error);
        tv_no_more = (TextView) view.findViewById(R.id.tv_no_more);
        return view;
    }
    private DefaultAdapter adapter;

    public MoreHolder(DefaultAdapter adapter, boolean hasMore) {
        super();
        this.adapter = adapter;
        this.hasMore = hasMore;
        // 初始化的时候设置有无更多数据
        setData(hasMore?HAS_MORE:HAS_NO_MORE);
    }


    @Override
    public View getContentView() {
        if(hasMore){
            loadMore();
        }
        return super.getContentView();
    }

    private void loadMore() {
        // 请求服务器，加载下一批数据
        adapter.loadMore();
    }
    @Override
    public void refreshView(Integer data) {
        // 根据不同的状态显示不同的界面
        rl_more_error.setVisibility(data == LOAD_ERROR?View.VISIBLE:View.GONE);
        rl_more_loading.setVisibility(data == HAS_MORE?View.VISIBLE:View.GONE);
        tv_no_more.setVisibility(data == HAS_NO_MORE? View.VISIBLE:View.GONE);
    }

}
