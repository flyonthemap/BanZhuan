package com.shuao.banzhuan.holder;

import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.data.Config;
import com.shuao.banzhuan.entity.SubjectInfo;
import com.shuao.banzhuan.tools.PicassoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by flyonthemap on 2017/4/19.
 */

public class SubjectHolder extends BaseRecyclerHolder<SubjectInfo> {
    @BindView(R.id.iv_pic)
    ImageView imageView;
    @BindView(R.id.tv_des)
    TextView tvTitle;
    public SubjectHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this,view);
    }

    @Override
    public void refreshView(SubjectInfo subjectInfo) {
        PicassoUtils.loadImageWithHolder(Config.BASE_IMAGE_URL+"?name="+subjectInfo.url,R.drawable.ic_default,imageView);
        tvTitle.setText(subjectInfo.des);
    }


}
