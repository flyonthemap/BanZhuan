package com.shuao.banzhuan.holder;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.deque.LinkedBlockingDeque;
import com.shuao.banzhuan.R;
import com.shuao.banzhuan.activity.ExchangeActivity;
import com.shuao.banzhuan.activity.ExchangeDetailActivity;
import com.shuao.banzhuan.model.ExchangeInfo;
import com.shuao.banzhuan.tools.UiTools;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by flyonthemap on 2017/3/24.
 */

public class ExchangeHolder extends BaseRecyclerHolder<ExchangeInfo> implements View.OnClickListener{
    // 兑换方式的名字
    private TextView tvExchangeName;
    private CircleImageView ci_exchange;
    private Button btn_action;
    private LinearLayout llExchangeItem;
    public ExchangeHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void initView(View view) {
        view.setOnClickListener(this);
        llExchangeItem = (LinearLayout) view.findViewById(R.id.ll_exchange_item);
        tvExchangeName = (TextView) view.findViewById(R.id.tv_exchangeName);
        ci_exchange = (CircleImageView) view.findViewById(R.id.ci_exchange);
        btn_action = (Button) view.findViewById(R.id.btn_action);
        btn_action.setOnClickListener(this);
    }

    @Override
    public void refreshView(ExchangeInfo exchangeInfo) {
        tvExchangeName.setText(exchangeInfo.getExchangeName());
        ci_exchange.setImageResource(exchangeInfo.getImageId());
        btn_action.setText(exchangeInfo.getRequire());
    }

    @Override
    public void onClick(View v) {
        // 启动到提交兑换界面
        switch (v.getId()){
            case R.id.ll_exchange_item:
            case R.id.btn_action:
                Intent intent = new Intent(ExchangeActivity.activity, ExchangeDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                UiTools.getContext().startActivity(intent);
                break;
        }

    }
}
