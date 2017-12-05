package com.example.xunself.x8hot_os;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by XunselF on 2017/12/2.
 */

public class WorkorderFragment extends Fragment {

    private View view;

    private RecyclerView workOrderRecyclerView;

    private List<Box> boxList;      //纸箱数据
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_workorder,container,false);
        return view;
    }

    /**
     * 初始化
     */
    private void init(){
        workOrderRecyclerView = (RecyclerView) view.findViewById(R.id.work_order_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        workOrderRecyclerView.setLayoutManager(linearLayoutManager);
    }
}
