package com.example.xunself.x8hot_os;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.ViewHolder>{

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView work_id;               //工单号
            TextView work_money;            //总金额
            RecyclerView work_recyclerview;     //用于显示工单每个纸箱
            public ViewHolder(View itemView) {
                super(itemView);
                work_id = (TextView) itemView.findViewById(R.id.work_id);
                work_money = (TextView) itemView.findViewById(R.id.work_money);
                work_recyclerview = (RecyclerView) itemView.findViewById(R.id.work_box_recyclerview);
            }
        }
        @Override
        public WorkOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.work_order_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(WorkOrderAdapter.ViewHolder holder, int position) {
            Box box = boxList.get(position);
            holder.work_id.setText(box.getWork_id());
        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
