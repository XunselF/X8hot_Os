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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XunselF on 2017/12/2.
 */

public class BoxsFragment extends Fragment {

    private List<Box> boxsList;
    private View view;

    private RecyclerView boxRecyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_boxs,container,false);
        init();
        return view;
    }

    /**
     * 初始化
     */
    private void init(){
        getBoxList();
        boxRecyclerView = (RecyclerView) view.findViewById(R.id.boxs_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        boxRecyclerView.setLayoutManager(linearLayoutManager);
        BoxsAdapter boxsAdapter = new BoxsAdapter();
        boxRecyclerView.setAdapter(boxsAdapter);
    }

    /**
     * 获取数据源
     */
    private void getBoxList(){
        boxsList = new ArrayList<>();
        for (int i = 0; i < 5; i ++){
            Box box = new Box("7544","HD1234",1000,500,200,"2017-12-22","备注");
            boxsList.add(box);
        }
    }
    class BoxsAdapter extends RecyclerView.Adapter<BoxsAdapter.ViewHolder>{
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView boxId;
            TextView boxNHnum;
            TextView dataHnum;
            TextView dataNHnum;
            public ViewHolder(View itemView) {
                super(itemView);
                boxId = (TextView)itemView.findViewById(R.id.boxs_id);
                boxNHnum = (TextView) itemView.findViewById(R.id.boxs_nhnum);
                dataHnum = (TextView) itemView.findViewById(R.id.data_hnum);
                dataNHnum = (TextView) itemView.findViewById(R.id.data_nhnum);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.box_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Box box = boxsList.get(position);
            int boxNHnum = box.getBox_num() - box.getBox_hnum();                        //还剩箱子数量
            int dataNHnum = boxNHnum - box.getData_hnum();                              //材料剩余数量
            holder.boxId.setText(box.getBox_id());
            holder.boxNHnum.setText(boxNHnum + "");
            holder.dataHnum.setText(box.getData_hnum() + "");
            if (dataNHnum > 0){
                holder.dataNHnum.setText("还需要"+ dataNHnum + "个材料");
            }else{
                holder.dataNHnum.setTextColor(getResources().getColor(R.color.colorPrimary));
                holder.dataNHnum.setText("已经不需要材料");
            }

        }

        @Override
        public int getItemCount() {
            return boxsList.size();
        }
    }
}
