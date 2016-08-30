package com.shuao.banzhuan.tools;

import android.widget.AbsListView;

import com.squareup.picasso.Picasso;

import cn.finalteam.galleryfinal.PauseOnScrollListener;

/**
 * Created by flyonthemap on 16/8/12.
 */
public class PicassoPauseOnScrollListener extends PauseOnScrollListener {

    public PicassoPauseOnScrollListener(boolean pauseOnScroll, boolean pauseOnFling) {
        super(pauseOnScroll, pauseOnFling);
    }

    @Override
    public void resume() {
        Picasso.with(getActivity()).resumeTag(getActivity());
    }

    @Override
    public void pause() {
        Picasso.with(getActivity()).pauseTag(getActivity());
    }
}
