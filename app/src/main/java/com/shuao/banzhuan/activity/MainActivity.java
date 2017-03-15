package com.shuao.banzhuan.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.fragment.FragmentFactory;
import com.shuao.banzhuan.tools.BaseApplication;
import com.shuao.banzhuan.tools.OKClientManager;
import com.shuao.banzhuan.tools.PicassoUtils;
import com.shuao.banzhuan.tools.UiTools;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity implements DrawerLayout.DrawerListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    //    Tab标签的名字
    private String[] tabNames;
    private long mExitTime;
    private SharedPreferences sharedPreferences;
    private CircleImageView circlePortrait;
    private TextView tv_income, tv_nickname;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //  初始化出界面中的控件
    @Override
    protected void initView() {
        //setContentView方法要在最前面调用才能避免空指针异常。
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // 设置左上角的点击事件
        if(mDrawerLayout != null)
            mDrawerLayout.addDrawerListener(this);
        // 设置侧滑导航栏
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            viewPager.setAdapter(new MainAdapter(getSupportFragmentManager()));
        }


        // 对Tab标签的时间进行设置
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if(tabLayout != null)
            tabLayout.setupWithViewPager(viewPager);
        if(viewPager!=null)
            viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    //当页面被选中的时候
                    FragmentFactory.createFragment(position).changeState();
                }
            });
    }


    @Override
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        // 给Toolbar增加头像
        //  ab.setIcon(R.mipmap.ic_launcher);
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
            ab.setDisplayUseLogoEnabled(true);
        }

    }

    @Override
    protected void init() {
        tabNames = UiTools.getStringArray(R.array.tab_names);
        sharedPreferences = getSharedPreferences("banzhuan", MODE_PRIVATE);
    }



    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        // 加载Header中的内容
        final View header = navigationView.getHeaderView(0);
        // 当点击用户头像的时候，进入个人信息界面
        circlePortrait = (CircleImageView) header.findViewById(R.id.big_iv_portrait);
        tv_income = (TextView) header.findViewById(R.id.tv_income);
        tv_nickname = (TextView) header.findViewById(R.id.tv_nickname);
        getNavigationInfo();
        circlePortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.big_iv_portrait:
                        Intent launchPersonalInfo = new Intent(MainActivity.this, PersonalInfoActivity.class);
                        startActivity(launchPersonalInfo);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }
            }
        });
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }


    private class MainAdapter extends FragmentStatePagerAdapter {
        public MainAdapter(FragmentManager fm) {
            super(fm);
        }

        //  创建Fragment使用的是工厂设计模式
        @Override
        public Fragment getItem(int position) {
            return FragmentFactory.createFragment(position);
        }

        @Override
        public int getCount() {
            return tabNames.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames[position];
        }

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - mExitTime) > 1500) {
                Toast.makeText(this, "亲，宝宝舍不得你啊~", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tb_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 这是左上角home菜单默认的id必须要是R.id.home。
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_exchange:
                Intent exchangeRecord = new Intent(MainActivity.this,ExchangeRecordActivity.class);
                startActivity(exchangeRecord);
                break;
            case R.id.action_task:
                Intent taskRecord = new Intent(MainActivity.this,TaskRecordActivity.class);
                startActivity(taskRecord);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                // 监听navigation的事件
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_my_exchange:
                                Intent intentExchange = new Intent(MainActivity.this, ExchangeActivity.class);
                                startActivity(intentExchange);
                                break;
                            case R.id.nav_settings:
                                Intent intentSetting = new Intent(MainActivity.this, SettingActivity.class);
                                startActivity(intentSetting);
                                break;
                            case R.id.nav_my_task:
                                Intent intentTask = new Intent(MainActivity.this,TaskActivity.class);
                                startActivity(intentTask);
                                break;
                            case R.id.nav_about:
                                Intent intentAbout = new Intent(MainActivity.this,AboutActivity.class);
                                startActivity(intentAbout);
                                break;
                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });
    }

    private void getNavigationInfo() {


        // 加载个人的昵称和总收入

        if(Config.nickName == null || Config.income == 0){
            //从服务器加载数据
        }else {
            Config.nickName = sharedPreferences.getString(Config.STR_NICKNAME, null);
            Config.income = sharedPreferences.getLong(Config.STR_INCOME,0);
        }
        if (tv_nickname != null) {
            tv_nickname.setText("");
            tv_nickname.setText(Config.nickName);
        }
        if(tv_income != null){
            tv_income.setText("");
            tv_income.setText(Config.income+"万");
        }

        // 加载头像，首先检查之前有没有缓存过本地文件
        Config.PORTRAIT_PATH = sharedPreferences.getString(Config.STR_PORTRAIT_PATH, null);
        if (Config.PORTRAIT_PATH != null) {
            File file = new File(Config.PORTRAIT_PATH);
            // 如果本地文件没有被删除
            if (file.exists()) {
                Picasso.with(getApplicationContext()).load(file).into(circlePortrait);
            } else if (!file.exists()) {
                // 本地文件被删除则从服务器上加载图片
                OKClientManager.getOkManager().asyncByURL(Config.DOWNLOAD_PORTRAIT_URL, new OKClientManager.LoadBitmap() {
                    @Override
                    public void onResponse(final Bitmap result) {
                        UiTools.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                circlePortrait.setImageBitmap(result);
                            }
                        });
                    }
                });

            }
            // 头像没有在本地缓存过
        }else{
            OKClientManager.getOkManager().asyncByURL(Config.DOWNLOAD_PORTRAIT_URL, new OKClientManager.LoadBitmap() {
                @Override
                public void onResponse(final Bitmap result) {
                    if (result != null) ;
                    UiTools.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            circlePortrait.setImageBitmap(result);
                        }
                    });
                }
            });
        }

    }
    public Toolbar getToolbar(){
        return toolbar;
    }

}
