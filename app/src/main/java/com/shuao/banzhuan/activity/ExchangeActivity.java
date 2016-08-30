package com.shuao.banzhuan.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.adapter.ExchangeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flyonthemap on 16/8/4.
 *
 */
public class ExchangeActivity  extends BaseActivity{
    private ListView listView;
    private List<String>  data;
//    private String[] data = {"支付宝","话费"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_exchange);
        data = new ArrayList<>();
        data.add("支付宝");
        data.add("支付宝");
        data.add("支付宝");
        data.add("支付宝");
        data.add("支付宝");
        data.add("支付宝");
        listView = (ListView) findViewById(R.id.lv_exchange);
        listView.setAdapter(new ExchangeAdapter(listView,data));

    }

    @Override
    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tl_exchange);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if(ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

    }
}
