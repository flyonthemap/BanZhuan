package com.shuao.banzhuan.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.shuao.banzhuan.R;
import com.shuao.banzhuan.adapter.TableViewAdapter;
import com.shuao.banzhuan.view.CellTypeEnum;

import java.util.ArrayList;
import java.util.HashMap;



public class TaskRecordActivity extends BaseRecordActivity {

    private ListView listView;
    private TableViewAdapter adapter = null;
    private LinearLayout headLayout;
    private ArrayList<HashMap<String,Object>>  lists = new ArrayList<HashMap<String,Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_task_record);
        listView = (ListView) findViewById(R.id.listview);
        headLayout = (LinearLayout)findViewById(R.id.task_record_head);
        initHeadTitle();
    }

    @Override
    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tl_task_record);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void initHeadTitle() {
        HashMap headMap = new HashMap();
        initHeadHashMap(headMap,"任务名称");
        initHeadHashMap(headMap,"任务时间");
        initHeadHashMap(headMap,"任务奖励");
        drawHead(headMap,headLayout);
        HashMap contentMap = new HashMap();
        setContent(contentMap);
        adapter = new TableViewAdapter(this, lists,listView
                ,false,this.arrHeadWidth);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setContent(HashMap contentHashMap) {
        // 将不同的样式和数据加入界面中，这一部分的数据可以来自于服务器
        // 此处将从服务器返回的数据添加到这里面来
        HashMap rowMap1 = new HashMap();
        lists.add(rowMap1);
        this.addRows(rowMap1, 1, "完成了任务的注册", CellTypeEnum.LABEL);
        this.addRows(rowMap1, 1, "1-2(1)", CellTypeEnum.LABEL);
        this.addRows(rowMap1, 1, "1-3(2)", CellTypeEnum.LABEL);
        rowMap1.put("rowtype", "css1");
        lists.add(rowMap1);
        lists.add(rowMap1);
        lists.add(rowMap1);
        lists.add(rowMap1);
        lists.add(rowMap1);
        lists.add(rowMap1);
        lists.add(rowMap1);
        lists.add(rowMap1);
        lists.add(rowMap1);
        lists.add(rowMap1);
        lists.add(rowMap1);
        lists.add(rowMap1);
        lists.add(rowMap1);
        lists.add(rowMap1);
        lists.add(rowMap1);
        lists.add(rowMap1);
        HashMap rowMap2 = new HashMap();
        this.addRows(rowMap2, 1, "1-1(1)", CellTypeEnum.LABEL);
        this.addRows(rowMap2, 1, "1-2(1)", CellTypeEnum.LABEL);
        this.addRows(rowMap2, 1, "1-3(2)", CellTypeEnum.LABEL);
        rowMap2.put("rowtype", "css1");
        lists.add(rowMap2);
        HashMap rowMap3 = new HashMap();
        this.addRows(rowMap3, 1, "1-1(1)", CellTypeEnum.LABEL);
        this.addRows(rowMap3, 1, "1-2(1)", CellTypeEnum.LABEL);
        this.addRows(rowMap3, 1, "1-3(2)", CellTypeEnum.LABEL);
        rowMap3.put("rowtype", "css1");
        lists.add(rowMap3);
        HashMap rowMap4 = new HashMap();
        this.addRows(rowMap4, 1, "1-1(1)", CellTypeEnum.LABEL);
        this.addRows(rowMap4, 1, "1-2(1)", CellTypeEnum.LABEL);
        this.addRows(rowMap4, 1, "1-3(2)", CellTypeEnum.LABEL);
        rowMap4.put("rowtype", "css1");
        lists.add(rowMap4);
        HashMap rowMap5 = new HashMap();
        this.addRows(rowMap5, 1, "1-1(1)", CellTypeEnum.LABEL);
        this.addRows(rowMap5, 1, "1-2(1)", CellTypeEnum.LABEL);
        this.addRows(rowMap5, 1, "1-3(2)", CellTypeEnum.LABEL);
        rowMap5.put("rowtype", "css1");

    }
}
