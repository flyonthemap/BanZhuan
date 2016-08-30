package com.shuao.banzhuan.view;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.tools.UiTools;
import com.shuao.banzhuan.tools.ViewUtils;

/**
 * Created by flyonthemap on 16/8/4.
 * 将LoadingPage抽取出来
 */


public abstract class LoadingPage extends FrameLayout {
    public static final int STATE_UNKNOWN = 0;
    //    加载中
    public static final int STATE_LOADING = 1;
    //    加载错误
    public static final int STATE_ERROR = 2;
    //    服务器没有相关的数据
    public static final int STATE_EMPTY = 3;
    //    加载成功
    public static final int STATE_SUCCESS = 4;
    //    注意这个状态不能设置为静态的，主要是为了避免所有的页面共享这一个状态
    public int curState = STATE_UNKNOWN;
    //    几种不同的加载界面
    private View loadingView;
    private View errorView;
    private View emptyView;
    private View successView;
    //利用枚举来表示加载的结果
    public enum LoadResult {
        error(2), empty(3), success(4);

        int value;

        LoadResult(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }
    public LoadingPage(Context context) {
        super(context);
        init();
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }




    private void init() {
        loadingView = createLoadingView();
        if (loadingView != null) {
            this.addView(loadingView, new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        errorView = createErrorView();
        if (errorView != null) {
            this.addView(errorView, new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        emptyView = createEmptyView();
        if (emptyView != null) {
            this.addView(emptyView, new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        showPage();
    }

    //    load the different interface according to the load result
    private void showPage() {
        if (loadingView != null) {
            loadingView.setVisibility(curState == STATE_UNKNOWN
                    || curState == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
        }
        if (errorView != null) {
            errorView.setVisibility(curState == STATE_ERROR ? View.VISIBLE
                    : View.INVISIBLE);
        }
        if (emptyView != null) {
            emptyView.setVisibility(curState == STATE_EMPTY ? View.VISIBLE
                    : View.INVISIBLE);
        }
        if (curState == STATE_SUCCESS) {
            successView = createSuccessView();
            if (successView != null) {
                this.addView(successView, new FrameLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                successView.setVisibility(View.VISIBLE);
            }
        }
    }

    //    change the result according to load result
    public void changeState() {
        if (curState == STATE_ERROR || curState == STATE_EMPTY) {
            curState = STATE_LOADING;
        }
        // 请求服务器 获取服务器上数据 进行判断
        // 请求服务器 返回一个结果
        new Thread() {
            public void run() {
                SystemClock.sleep(2000);
//                请求服务器之后进行状态变换
                final LoadResult result = load();
                if (UiTools.getContext() != null) {
                    UiTools.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (result != null) {
                                curState = result.getValue();
                                // 状态改变了,重新判断当前应该显示哪个界面
                                showPage();
                            }
                        }
                    });
                }
            };
        }.start();
        showPage();

    }


    //    创建了空的界面
    private View createEmptyView() {
        View view = View.inflate(UiTools.getContext(), R.layout.loadpage_empty, null);
        return view;
    }

    //    创建了错误界面
    private View createErrorView() {
        View view = View.inflate(UiTools.getContext(), R.layout.loadpage_error, null);
        RelativeLayout loadError = (RelativeLayout) view.findViewById(R.id.rl_load_error);
        loadError.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                changeState();
            }
        });
        return view;
    }

    //    create the loading
    private View createLoadingView() {

        View view = View
                .inflate(UiTools.getContext(), R.layout.loadpage_loading, null);
        return view;
    }

    public abstract View createSuccessView();
    //  load data from sever
    public abstract LoadResult load() ;


}

