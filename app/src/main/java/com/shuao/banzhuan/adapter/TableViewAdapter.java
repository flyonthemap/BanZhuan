package com.shuao.banzhuan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.view.CustomTableItem;
import com.shuao.banzhuan.view.ItemCell;

import java.util.ArrayList;
import java.util.HashMap;

public class TableViewAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String,Object>> lists;
    private ListView listView = null;
    private boolean isReadOnly = false;
    private int[] arrHeadWidth = null;

    public TableViewAdapter(Context context, ArrayList<HashMap<String,Object>> lists
            ,ListView listView,boolean isReadOnly
            ,int[] arrHeadWidth) {
        super();
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
        this.listView = listView;
        this.isReadOnly = isReadOnly;
        this.arrHeadWidth = arrHeadWidth;
        this.listView.setAdapter(this);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        HashMap map = lists.get(index);
        String type = (String) map.get("rowtype");

        ArrayList<ItemCell> itemCells = new ArrayList();
        for(int i=0;i<map.size()-1;i++){
            ItemCell itemCell = (ItemCell)map.get(i+"");
            itemCells.add(itemCell);
        }
        if(view == null|| view != null && !((CustomTableItem) view.getTag()).getRowType().equals(type)){
            view = inflater.inflate(R.layout.customel_list_item, null);
            CustomTableItem itemCustom = (CustomTableItem)view.findViewById(R.id.custom_item);
            itemCustom.buildItem(context,type ,itemCells,arrHeadWidth,isReadOnly);
            view.setTag(itemCustom);
        }else{
            CustomTableItem itemCustom = (CustomTableItem) view.getTag();
            itemCustom.refreshData(itemCells);
        }
        // 分别设置奇数行和偶数行的颜色
        if( index % 2 == 0 ){
            view.setBackgroundColor(Color.argb(250 ,  255 ,  255 ,  255 ));
        }else{
            view.setBackgroundColor(Color.argb(250 ,  224 ,  243 ,  250 ));
        }
        return view;
    }

}
