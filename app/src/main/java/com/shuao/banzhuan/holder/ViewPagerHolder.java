package com.shuao.banzhuan.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.http.HttpHelper;
import com.shuao.banzhuan.tools.BaseApplication;
import com.shuao.banzhuan.tools.PicassoUtils;
import com.shuao.banzhuan.tools.UiTools;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by flyonthemap on 16/8/9.
 */
public class ViewPagerHolder extends BaseHolder<List<String>> {
    private List<String> data;

    private ViewPager mViewPager;
    private LinearLayout llContainer;
    // 上个圆点位置
    private int mPreviousPos;

    @Override
    public View initView() {
        // 创建根布局, 相对布局
        RelativeLayout rlRoot = new RelativeLayout(UiTools.getContext());
        // 初始化布局参数, 根布局上层控件是listView, 所以要使用listView定义的LayoutParams
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, UiTools.dip2px(150));
        rlRoot.setLayoutParams(params);

        // ViewPager
        mViewPager = new ViewPager(UiTools.getContext());
        RelativeLayout.LayoutParams vpParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        // mViewPager.setLayoutParams(vpParams);
        rlRoot.addView(mViewPager, vpParams);// 把viewpager添加给相对布局

        // 初始化指示器
        llContainer = new LinearLayout(UiTools.getContext());
        llContainer.setOrientation(LinearLayout.HORIZONTAL);// 水平方向

        RelativeLayout.LayoutParams llParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        // 设置内边距
        int padding = UiTools.dip2px(10);
        llContainer.setPadding(padding, padding, padding, padding);

        // 添加规则, 设定展示位置
        llParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);// 底部对齐
        llParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);// 右对齐

        // 添加布局
        rlRoot.addView(llContainer, llParams);

        return rlRoot;
    }

    @Override
    public void refreshView(final List<String> data) {
        this.data = data;
        // 填充viewpager的数据

        mViewPager.setAdapter(new HomeHeaderAdapter());
        mViewPager.setCurrentItem(data.size() * 10000);
        // 当手指按住ViewPager的图片的时候，图片会停止滚动
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        homeHeaderTask.stop();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        homeHeaderTask.start();
                        break;

                }
                return false;
            }
        });

        // 初始化指示器
        for (int i = 0; i < data.size(); i++) {
            ImageView point = new ImageView(UiTools.getContext());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            // 第一个默认选中
            if (i == 0) {
                point.setImageResource(R.drawable.indicator_selected);
            } else {
                point.setImageResource(R.drawable.indicator_normal);

                params.leftMargin = UiTools.dip2px(4);// 左边距
            }

            point.setLayoutParams(params);

            llContainer.addView(point);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                position = position % data.size();

                // 当前点被选中
                ImageView point = (ImageView) llContainer.getChildAt(position);
                point.setImageResource(R.drawable.indicator_selected);

                // 上个点变为不选中
                ImageView prePoint = (ImageView) llContainer
                        .getChildAt(mPreviousPos);
                prePoint.setImageResource(R.drawable.indicator_normal);

                mPreviousPos = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        homeHeaderTask = new HomeHeaderTask();
        homeHeaderTask.start();
        // UiTools.getHandler().postDelayed(r, delayMillis)
        //启动轮播条自动播放

    }
    boolean flag;
    private  HomeHeaderTask homeHeaderTask;
    class HomeHeaderTask implements Runnable {

        public void start() {
            // 移除之前发送的所有消息, 避免消息重复
            if(!flag){
                UiTools.cancel(this);
                flag = true;
                UiTools.postDelayed(this, 3000);
            }

        }

        @Override
        public void run() {
            if(flag){
                UiTools.cancel(this);
                int currentItem = mViewPager.getCurrentItem();
                currentItem++;
                mViewPager.setCurrentItem(currentItem);

                // 继续发延时3秒消息, 实现内循环
                UiTools.postDelayed(this, 3000);

            }
        }

        public void stop(){
            if(flag){
                flag = false;
                UiTools.cancel(this);
            }
        }

    }

    class HomeHeaderAdapter extends PagerAdapter {




        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % data.size();

//            String url = data.get(position);

            ImageView view = new ImageView(UiTools.getContext());
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            String url;
            // 加载适合当前的图片
            if((position%2) ==0)
                url = "http://e.hiphotos.baidu.com/image/pic/item/faedab64034f78f0b31a05a671310a55b3191c55.jpg";
            else {
                url = "http://img2081.poco.cn/mypoco/myphoto/20130429/12/64608683201304291154022905708975271_045.jpg";
            }
            PicassoUtils.loadImageWithCrop(url,view);

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

}
