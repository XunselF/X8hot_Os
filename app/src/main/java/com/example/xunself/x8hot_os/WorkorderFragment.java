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

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XunselF on 2017/12/2.
 */

public class WorkorderFragment extends Fragment {

    private View view;

    private RecyclerView workOrderRecyclerView;
    private TextView footerText;                                                                  //尾部数据

    private List<Box> boxList;      //全部纸箱数据
    private List<String> boxWorkIdList;     //工单数据

    private final int NOT_CARRY_OUT = 0;                                                        //未完成状态
    private final int CARRY_OUT = 1;                                                             //完成状态
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_workorder,container,false);
        init();
        return view;
    }
    /**
     * 获取全部数据
     */
    private void getBoxList(){
        boxList = new ArrayList<>();
        boxList = DataSupport.findAll(Box.class);
        if (boxList.size() == 0){
            footerText.setText("当前没有工单数据~");
        }else{
            getBoxWorkId();                                               //当执行完数据之后再执行获取工单
            footerText.setText("当前有" + boxWorkIdList.size() + "工单数据~");
        }

    }
    /**
     * 获取工单
     */
    private void getBoxWorkId(){
        boxWorkIdList = new ArrayList<>();
        boxWorkIdList.add(boxList.get(0).getWork_id());
        for (int i = 0; i < boxList.size(); i++){
            for (int j = 0; j < boxWorkIdList.size(); j++){
                if (boxWorkIdList.get(j).equals(boxList.get(i).getWork_id())){          //当找到相同的值   跳出循环
                    break;
                }else if (j == boxWorkIdList.size() - 1 && !boxWorkIdList.get(j).equals(boxList.get(i).getWork_id())){        //当循环到最后一组并且不相同 赋值
                    boxWorkIdList.add(boxList.get(i).getWork_id());
                    break;
                }
            }
        }
    }
    /**
     * 获取单个工单的数据
     */
    private List<Box> getBoxItemList(String Box_Item_workId){
        List<Box> boxItemList = DataSupport.where("work_id = ?",Box_Item_workId).find(Box.class);             //通过子项工单名获取全部该工单的纸箱数据
        return boxItemList;
    }

    /**
     * 获取单个工单的总价
     * @param boxItemList   传入单个工单的所有纸箱数据
     * @return      返回总价
     */
    private String getWorkTotalPrize(List<Box> boxItemList){
        double totalPrize = 0;
        for (int i = 0; i < boxItemList.size(); i++){
            totalPrize += boxItemList.get(i).getBox_num() * boxItemList.get(i).getBox_prize();          //叠加单个纸箱的价格
        }
        return String.valueOf(totalPrize);
    }

    /**
     * 初始化
     */
    private void init(){
        footerText = (TextView) view.findViewById(R.id.workOrder_footer);
        getBoxList();                                                                     //获取数据源
        workOrderRecyclerView = (RecyclerView) view.findViewById(R.id.work_order_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        workOrderRecyclerView.setLayoutManager(linearLayoutManager);
        WorkOrderAdapter workOrderAdapter = new WorkOrderAdapter();
        workOrderRecyclerView.setAdapter(workOrderAdapter);
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
            String box_Item_WorkId = boxWorkIdList.get(position);
            List<Box> boxItemList = getBoxItemList(box_Item_WorkId);                                //获取单个工单数据
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            BoxItemAdapter boxItemAdapter = new BoxItemAdapter(boxItemList);
            holder.work_recyclerview.setLayoutManager(linearLayoutManager);
            holder.work_recyclerview.setAdapter(boxItemAdapter);                                  //显示单个工单的多个纸箱数据

            holder.work_id.setText(box_Item_WorkId);                                               //显示工单名
            holder.work_money.setText(getWorkTotalPrize(boxItemList));                             //显示工单总价

        }

        @Override
        public int getItemCount() {
            return boxWorkIdList.size();
        }
    }
    class BoxItemAdapter extends RecyclerView.Adapter<BoxItemAdapter.ViewHolder>{

        private List<Box> mBoxsItemList;

        public BoxItemAdapter(List<Box> boxsItemList){
            this.mBoxsItemList = boxsItemList;
        }
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView box_id;
            TextView box_carryStatus;
            TextView box_TotalPrize;
            public ViewHolder(View itemView) {
                super(itemView);
                box_id = (TextView) itemView.findViewById(R.id.boxs_id);
                box_carryStatus = (TextView) itemView.findViewById(R.id.boxs_carryStatus);
                box_TotalPrize = (TextView) itemView.findViewById(R.id.boxs_Totalprize);
            }
        }

        @Override
        public BoxItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.work_box_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(BoxItemAdapter.ViewHolder holder, int position) {
            Box box = mBoxsItemList.get(position);
            holder.box_id.setText(box.getBox_id());                                                //显示纸箱型号
            switch (box.getIsCarryOut()){                                                          //显示完成状态
                case CARRY_OUT:
                    holder.box_carryStatus.setText("(已完成)");
                    holder.box_carryStatus.setTextColor(getResources().getColor(R.color.boxcarryout));
                    break;
                case NOT_CARRY_OUT:
                    holder.box_carryStatus.setText("(未完成)");
                    holder.box_carryStatus.setTextColor(getResources().getColor(R.color.boxnocarryout));
                    break;
                default:
                    break;
            }
            holder.box_TotalPrize.setText(box.getBox_num() * box.getBox_prize() + "");            //显示纸箱价格
        }

        @Override
        public int getItemCount() {
            return mBoxsItemList.size();
        }
    }
}
