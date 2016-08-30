package com.shuao.banzhuan.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.model.ExchangeInfo;
import com.shuao.banzhuan.tools.UiTools;

import java.util.List;

/**
 * Created by flyonthemap on 16/8/13.
 */
public class ExchangeAdapter extends BaseAdapter {

    private ListView listView;
    private List<String> data;

    public ExchangeAdapter(ListView listView, List<String> data) {
        this.listView = listView;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(UiTools.getContext(), R.layout.exchange_item,null);
        return convertView;
    }
}
