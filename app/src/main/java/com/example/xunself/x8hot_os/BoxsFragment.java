package com.example.xunself.x8hot_os;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XunselF on 2017/12/2.
 */

public class BoxsFragment extends Fragment implements View.OnClickListener{

    private View view;

    private List<Box> boxsList;                                     //数据
    private List<Box> beforeBoxsList;                               //历史纪录

    private List<String> boxsId;         //纸箱型号


    private RecyclerView boxRecyclerView;
    private BoxsAdapter boxsAdapter;
    private android.widget.SearchView searchView;


    private LinearLayout fragmentBoxLayout;


    private FloatingActionsMenu floatingActionsMenu;
    private FloatingActionButton addBoxButton;
    private FloatingActionButton recordBoxButton;

    private final int NOT_CARRY_OUT = 0;                                        //工单未完成
    private final int CARRY_OUT = 1;                                            //工单已完成
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_boxs,container,false);
        init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getBoxList();               //获取数据
    }


    /**
     * 初始化
     */
    private void init(){

        /**
         * 初始化控件
         */
        searchView = (android.widget.SearchView) view.findViewById(R.id.searchView);
        fragmentBoxLayout = (LinearLayout) view.findViewById(R.id.Fragment_BoxLayout);
        floatingActionsMenu = (FloatingActionsMenu) view.findViewById(R.id.fab_menu);
        addBoxButton = (FloatingActionButton) view.findViewById(R.id.fab_add);
        recordBoxButton = (FloatingActionButton) view.findViewById(R.id.fab_record);

        boxRecyclerView = (RecyclerView) view.findViewById(R.id.boxs_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        boxRecyclerView.setLayoutManager(linearLayoutManager);
        boxsAdapter = new BoxsAdapter();
        boxRecyclerView.setAdapter(boxsAdapter);

        /**
         * 监听器
         */
        fragmentBoxLayout.setOnClickListener(this);
        addBoxButton.setOnClickListener(this);
        recordBoxButton.setOnClickListener(this);

        /**
         * 搜索框搜索功能
         */
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { //搜索后功能
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {    //输入时文字变化
                boxsList.clear();                                                       //清空数据
                for (int i = 0; i < boxsId.size(); i++){
                    if (boxsId.get(i).toUpperCase().indexOf(s.toUpperCase()) != -1){        //忽略大小写，当包含该字符串 加入数据
                        boxsList.add(beforeBoxsList.get(i));
                    }
                }
                boxsAdapter.notifyDataSetChanged() ;                                    //更新数据
                return true;
            }
        });

    }

    /**
     * 获取数据源
     */
    private void getBoxList(){
        beforeBoxsList = new ArrayList<>();
        boxsList = new ArrayList<>();
        boxsId = new ArrayList<>();
        boxsList = DataSupport.findAll(Box.class);
        beforeBoxsList.addAll(boxsList);
        for (int i = 0; i < boxsList.size(); i ++){
            Box box = boxsList.get(i);
            boxsId.add(box.getBox_id());
        }
        boxsAdapter.notifyDataSetChanged();
    }

    /**
     * 隐藏输入法
     */
    private void hideInputMethod(){
        InputMethodManager mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mInputMethodManager.isActive()) {
            mInputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);// 隐藏输入法
            searchView.clearFocus();    //输入框失去焦点
            searchView.setFocusable(false);
        }
        floatingActionsMenu.collapse();         //将按键收回
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.Fragment_BoxLayout:
                hideInputMethod();
                break;
            case R.id.fab_add:
                intent = new Intent(getContext(),AddBoxsActivity.class);
                startActivity(intent);
                hideInputMethod();
                break;
            case R.id.fab_record:
                intent = new Intent(getContext(),RecordActivity.class);
                startActivity(intent);
                hideInputMethod();
                break;
            default:
                break;
        }
    }

    class BoxsAdapter extends RecyclerView.Adapter<BoxsAdapter.ViewHolder>{
        class ViewHolder extends RecyclerView.ViewHolder{
            LinearLayout boxItemLayout;
            TextView box_carryStatus;                       //纸箱完成状态
            TextView boxId;
            TextView boxNHnum;
            TextView dataHnum;
            TextView dataNHnum;
            ImageButton boxMessageMenu;
            ImageView dataWarningImage;
            public ViewHolder(View itemView) {
                super(itemView);
                boxItemLayout = (LinearLayout) itemView.findViewById(R.id.box_item_layout);
                boxId = (TextView)itemView.findViewById(R.id.boxs_id);
                boxNHnum = (TextView) itemView.findViewById(R.id.boxs_nhnum);
                dataHnum = (TextView) itemView.findViewById(R.id.data_hnum);
                dataNHnum = (TextView) itemView.findViewById(R.id.data_nhnum);
                boxMessageMenu = (ImageButton) itemView.findViewById(R.id.popup_button);
                dataWarningImage = (ImageView) itemView.findViewById(R.id.data_warning_image);
                box_carryStatus = (TextView) itemView.findViewById(R.id.boxs_carryStatus);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.box_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Box box = boxsList.get(position);
            holder.boxItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideInputMethod();
                }
            });
            if (box.getIsCarryOut() == NOT_CARRY_OUT){
                holder.box_carryStatus.setText("(未完成)");
                holder.box_carryStatus.setTextColor(getResources().getColor(R.color.boxnocarryout));
            }else if (box.getIsCarryOut() == CARRY_OUT){
                holder.box_carryStatus.setText("(已完成)");
                holder.box_carryStatus.setTextColor(getResources().getColor(R.color.boxcarryout));
            }
            int boxNHnum = box.getBox_num() - box.getBox_hnum();                        //还剩箱子数量
            int dataNHnum = boxNHnum - box.getData_hnum();                              //材料剩余数量
            holder.boxId.setText(box.getBox_id());
            holder.boxNHnum.setText(boxNHnum + "");
            holder.dataHnum.setText(box.getData_hnum() + "");
            if (dataNHnum > 0){
                holder.dataNHnum.setTextColor(getResources().getColor(R.color.colorwarning));
                holder.dataNHnum.setText("(还需要"+ dataNHnum + "个材料)");
                holder.dataWarningImage.setVisibility(View.VISIBLE);
            }else{
                holder.dataNHnum.setTextColor(getResources().getColor(R.color.colorPrimary));
                holder.dataNHnum.setText("(已经不需要材料)");
                holder.dataWarningImage.setVisibility(View.GONE);
            }


            holder.boxMessageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(),holder.boxMessageMenu);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_item:
                                WorkOrderActivity.actionStart(getContext(),box);
                                hideInputMethod();
                                break;
                            case R.id.about_item:
                                BoxAboutActivity.actionStart(getContext(),box);
                                break;
                            case R.id.delete_item:
                                DeleteActivity.actionStart(getContext(),box);
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        }

        @Override
        public int getItemCount() {
            return boxsList.size();
        }
    }
}
