package com.example.xunself.x8hot_os;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XunselF on 2017/12/2.
 */

public class WorkorderFragment extends Fragment {

    private final String[] LETTER = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","S","Y","Z","0","1"
            ,"2","3","4","5","6","7","8","9"};
    private View view;

    private RecyclerView workOrderRecyclerView;
    private WorkOrderAdapter workOrderAdapter;

    private LinearLayout footLayout;
    private TextView footerText;                                                                  //尾部数据

    private List<Box> boxList;      //全部纸箱数据
    private List<String> boxWorkIdList;     //工单数据
    private List<String> oldBoxWorkIdList;      //旧的工单数据

    private final int NOT_CARRY_OUT = 0;                                                        //未完成状态
    private final int CARRY_OUT = 1;                                                             //完成状态
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_workorder,container,false);
        setHasOptionsMenu(true);        //通过该方法进行更新标题栏菜单
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);                                      //在菜单中找到对应控件的item
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        ImageView searchButton = (ImageView) searchView.findViewById(R.id.search_button);
        EditText textview = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        textview.setTextColor(getResources().getColor(R.color.colorwhile));
        searchView.setQueryHint("请输入订单号：");
        searchButton.setImageResource(R.drawable.ic_search_white_24dp);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {                          //通过搜索进行检测符合的数据
                setSearchView(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    /**
     * 搜索
     * @param name
     */
    private void setSearchView(String name){
        boxWorkIdList.clear();                                                       //清空数据
        for (int i = 0; i < oldBoxWorkIdList.size(); i++){
            if (oldBoxWorkIdList.get(i).toUpperCase().indexOf(name.toUpperCase()) != -1){        //忽略大小写，当包含该字符串 加入数据
                boxWorkIdList.add(oldBoxWorkIdList.get(i));
            }
        }
        if (name.equals("") && oldBoxWorkIdList.size() == 0){
            getFooterData();
        }else if (name.equals("") && oldBoxWorkIdList.size() == boxWorkIdList.size()){
            getFooterData();
        }else if (!name.equals("") && boxWorkIdList.size() == 0){
            footLayout.setVisibility(View.VISIBLE);
            footerText.setText("您所输入的订单号不存在");
        } else {
            footLayout.setVisibility(View.GONE);
        }
        workOrderAdapter.notifyDataSetChanged() ;                                    //更新数据
    }


    /**
     * 获取全部数据
     */
    private void getBoxList(){
        boxList = new ArrayList<>();
        boxWorkIdList = new ArrayList<>();
        oldBoxWorkIdList = new ArrayList<>();

        boxList = DataSupport.findAll(Box.class);

        if (boxList.size() != 0){
            getBoxWorkId();                                               //当执行完数据之后再执行获取工单
        }

        getFooterData();
    }
    /**
     * 获取尾部数据
     */
    private void getFooterData(){
        footLayout.setVisibility(View.VISIBLE);
        if (boxList.size() == 0){
            footerText.setText("当前没有工单数据~");
        }else{
            footerText.setText("当前有" + boxWorkIdList.size() + "条工单数据~");
        }
    }
    /**
     * 获取工单
     */
    private void getBoxWorkId(){
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
        SortWorkIdList();
        oldBoxWorkIdList.addAll(boxWorkIdList);
    }
    /**
     * 获取单个工单的数据
     */
    private List<Box> getBoxItemList(String Box_Item_workId){
        List<Box> boxItemList = DataSupport.where("work_id = ?",Box_Item_workId).find(Box.class);             //通过子项工单名获取全部该工单的纸箱数据
        return boxItemList;
    }



    /**
     * 初始化
     */
    private void init(){
        footLayout = (LinearLayout) view.findViewById(R.id.foot_Layout);
        footerText = (TextView) view.findViewById(R.id.workOrder_footer);
        getBoxList();                                                                     //获取数据源
        workOrderRecyclerView = (RecyclerView) view.findViewById(R.id.work_order_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        workOrderRecyclerView.setLayoutManager(linearLayoutManager);
        workOrderAdapter = new WorkOrderAdapter();
        workOrderRecyclerView.setAdapter(workOrderAdapter);
    }
    class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.ViewHolder>{

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView work_id;               //工单号
            RecyclerView work_recyclerview;     //用于显示工单每个纸箱
            public ViewHolder(View itemView) {
                super(itemView);
                work_id = (TextView) itemView.findViewById(R.id.work_id);
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
            LinearLayout work_box_item;
            TextView box_id;
            TextView box_carryStatus;
            TextView data_nhnum;
            public ViewHolder(View itemView) {
                super(itemView);
                work_box_item = (LinearLayout) itemView.findViewById(R.id.work_box_item);
                box_id = (TextView) itemView.findViewById(R.id.boxs_id);
                box_carryStatus = (TextView) itemView.findViewById(R.id.boxs_carryStatus);
                data_nhnum = (TextView) itemView.findViewById(R.id.data_nhnum);
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
            final Box box = mBoxsItemList.get(position);
            int box_nhnum = box.getBox_num() - box.getBox_hnum();
            //统计剩余数据

            int data_nhnum = box_nhnum - box.getData_hnum();
            //统计还需订材料数量

            holder.box_id.setText(box.getBox_id());
            //显示纸箱型号

            if (data_nhnum > 0){                                                                    //显示还需材料状况
                holder.data_nhnum.setText("(还需订" + data_nhnum + "个材料！)");
                holder.data_nhnum.setTextColor(getResources().getColor(R.color.boxnocarryout));
            }else{
                holder.data_nhnum.setText("(已经不需要材料)");
                holder.data_nhnum.setTextColor(getResources().getColor(R.color.boxcarryout));
            }

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


            holder.work_box_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {                                  //点击事件：跳转纸箱详细
                    BoxAboutActivity.actionStart(getActivity(),box);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mBoxsItemList.size();
        }
    }

    /**
     * 对数据进行排序
     */
    private void SortWorkIdList(){
        List<String> workIdList = new ArrayList<>();
        workIdList.addAll(boxWorkIdList);
        String[] worksId = new String[boxWorkIdList.size()];
        int[] worksItem = new int[boxWorkIdList.size()];
        String tempworkId = "";
        int tempworkItem = 0;

        for (int i = 0; i < boxWorkIdList.size(); i++){
            worksId[i] = boxWorkIdList.get(i);
            worksItem[i] = i;
        }
        //获取数据的数据id跟纸箱id

        for (int i = 0; i < boxWorkIdList.size(); i++){

            for (int j = i; j < boxWorkIdList.size(); j++){



                if (CompareInitial(worksId[i],worksId[j]) > 0){
                    tempworkItem = worksItem[j];
                    worksItem[j] = worksItem[i];
                    worksItem[i] = tempworkItem;
                    //进行item值的替换

                    tempworkId = worksId[j];
                    worksId[j] = worksId[i];
                    worksId[i] = tempworkId;
                    //进行boxid值的替换
                }else if (CompareInitial(worksId[i],worksId[j]) == 0){


                    if (worksId[i].compareTo(worksId[j]) > 0){
                        tempworkItem = worksItem[j];
                        worksItem[j] = worksItem[i];
                        worksItem[i] = tempworkItem;
                        //进行item值的替换

                        tempworkId = worksId[j];
                        worksId[j] = worksId[i];
                        worksId[i] = tempworkId;
                        //进行boxid值的替换

                    }
                }

            }
        }
        boxWorkIdList.clear();

        for (int i = 0; i < worksId.length; i++){
            this.boxWorkIdList.add(workIdList.get(worksItem[i]));
        }


    }

    /**
     *查询两个字符的首字符
     */
    private int CompareInitial(String boxid1,String boxid2){
        int boxidInitial1 = 0;
        int boxidInitial2 = 0;
        for (int i = 0; i < LETTER.length; i++ ){
            if (boxid1.substring(0,1).toUpperCase().equals(LETTER[i]))
                boxidInitial1 = i;
            if (boxid2.substring(0,1).toUpperCase().equals(LETTER[i]))
                boxidInitial2 = i;
        }
        if (boxidInitial1 > boxidInitial2)
            return 1;
        else if (boxidInitial1 == boxidInitial2)
            return 0;
        else
            return -1;
    }
}
