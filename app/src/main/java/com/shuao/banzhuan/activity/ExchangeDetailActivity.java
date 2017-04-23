package com.shuao.banzhuan.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.tools.OKClientManager;
import com.shuao.banzhuan.tools.SPUtils;
import com.shuao.banzhuan.tools.UiTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by flyonthemap on 2017/4/20.
 */

public class ExchangeDetailActivity extends BaseActivity {
    @BindView(R.id.tv_has_no_withdraw)
    TextView tvHasNoWithdraw;
    @BindView(R.id.tv_has_withdraw)
    TextView tvHasWithdraw;
    @BindView(R.id.et_withdraw_account)
    EditText etWithdrawAccount;
    @BindView(R.id.et_withdraw_name)
    EditText getEtWithdrawName;
    @BindView(R.id.btn_withdraw)
    Button btnWithdraw;
    private OKClientManager okClientManager;
    @Override
    protected void init() {
        okClientManager = OKClientManager.getOkManager();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_exchange_detail);
        ButterKnife.bind(this);
        Map<String,String>  params = new HashMap<>();

        params.put(Config.PHONE, (String) SPUtils.get(UiTools.getContext(),Config.PHONE,""));
        okClientManager.requestPostBySyn(Config.USER_PERSON_INFO_URI, params, new OKClientManager.LoadJsonString() {
            @Override
            public void onResponse(String result) {
                Log.e(Config.TAG,result);
                JSONObject json = null;
                try {
                    json = new JSONObject(result);
                    JSONObject jsonObject = json.getJSONObject("personInfo");
                    final int exchangeIntegral = Integer.parseInt(jsonObject.getString("exchangedIntegral"));
                    final int curIntegral = Integer.parseInt(jsonObject.getString("curIntegral"));
                    UiTools.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvHasNoWithdraw.setText(curIntegral+"元");
                            tvHasWithdraw.setText(exchangeIntegral+"元");
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tl_exchange_detail);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if(ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

    }
    @OnClick(R.id.btn_withdraw)
    void submit(){
        Toast.makeText(UiTools.getContext(),"已提交，请耐心等待审核",Toast.LENGTH_SHORT).show();

    }
}


