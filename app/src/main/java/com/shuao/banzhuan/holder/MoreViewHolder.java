package com.shuao.banzhuan.holder;

/**
 * Created by flyonthemap on 2017/3/21.
 */

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.adapter.BaseDefaultRecyclerAdapter;
import com.shuao.banzhuan.tools.UiTools;

/**
 * Created by flyonthemap on 16/8/9.
 */


public class MoreViewHolder extends BaseRecyclerHolder<Integer> {
    // 没有额外数据
    public static final int HAS_NO_MORE = 0;
    // 加载失败
    public static final int LOAD_ERROR = 1;
    // 有额外数据
    public static final int HAS_MORE = 2;

    private boolean  hasMore;
    private RelativeLayout rl_more_loading,rl_more_error;
    private TextView tv_no_more;

    private View mView;
    private BaseDefaultRecyclerAdapter adapter;

    public MoreViewHolder(View mView) {
        super(mView);
        this.adapter = adapter;
        this.hasMore = hasMore;
        // 初始化的时候设置有无更多数据
        bind(hasMore?HAS_MORE:HAS_NO_MORE);
    }

    public MoreViewHolder(View view, boolean hasMore) {
        super(view);
        if(hasMore){
            this.hasMore = hasMore;
        }

    }


    private void loadMore() {
        // 请求服务器，加载下一批数据
        adapter.loadMore();
    }

    @Override
    public void initView(View view) {
        view = UiTools.inflate(R.layout.load_more);
        rl_more_loading = (RelativeLayout) view.findViewById(R.id.rl_more_loading);
        rl_more_error = (RelativeLayout) view.findViewById(R.id.rl_more_error);
        tv_no_more = (TextView) view.findViewById(R.id.tv_no_more);
    }

    @Override
    public void refreshView(Integer data) {
        // 根据不同的状态显示不同的界面
        rl_more_error.setVisibility(data == LOAD_ERROR?View.VISIBLE:View.GONE);
        rl_more_loading.setVisibility(data == HAS_MORE?View.VISIBLE:View.GONE);
        tv_no_more.setVisibility(data == HAS_NO_MORE? View.VISIBLE:View.GONE);
    }

}
