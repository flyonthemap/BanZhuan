package com.shuao.banzhuan.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.adapter.AppAdapter;
import com.shuao.banzhuan.adapter.ExchangeAdapter;
import com.shuao.banzhuan.model.ExchangeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flyonthemap on 16/8/4.
 *
 */
public class ExchangeActivity  extends BaseActivity{
    private RecyclerView recyclerView;
    private List<ExchangeInfo>  exchangeInfoList;


    @Override
    protected void init() {
        exchangeInfoList  = new ArrayList<>();
        ExchangeInfo exchangeInfo1 = new ExchangeInfo("支付宝提现","提现",R.drawable.exchange_alipay);
        ExchangeInfo exchangeInfo2 = new ExchangeInfo("微信提现","提现",R.drawable.exchange_wechat);
        ExchangeInfo exchangeInfo3 = new ExchangeInfo("话费充值","充值",R.drawable.exchange_phone);
        exchangeInfoList.add(exchangeInfo1);
        exchangeInfoList.add(exchangeInfo2);
        exchangeInfoList.add(exchangeInfo3);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_exchange);
        recyclerView = (RecyclerView) findViewById(R.id.rv_exchange);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ExchangeAdapter exchangeAdapter = new ExchangeAdapter(this);
        exchangeAdapter.appendData(exchangeInfoList);
        recyclerView.setAdapter(exchangeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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
