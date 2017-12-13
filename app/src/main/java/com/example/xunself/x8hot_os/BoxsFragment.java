package com.example.xunself.x8hot_os;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

    private Toolbar  main_toolbar;
    private LinearLayout main_footer;
    //尾部板块

    private TextView main_footer_text;
    //尾部板块文字

    private EditText textview;
    //搜索框内容

    private List<Box> boxsList;                                     //数据
    private List<Box> beforeBoxsList;                               //历史纪录

    private List<String> boxsId;         //纸箱型号


    private RecyclerView boxRecyclerView;
    private BoxsAdapter boxsAdapter;
    private SearchView searchView;


    private LinearLayout fragmentBoxLayout;


    private FloatingActionsMenu floatingActionsMenu;
    private FloatingActionButton addBoxButton;
    private FloatingActionButton recordBoxButton;
    private FloatingActionButton appaboutButton;

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
        setHasOptionsMenu(true);
        getBoxList();               //获取数据
        getFooterData();
        if (textview != null){
            setSearchView(textview.getText().toString());
        }
    }
    private void getFooterData(){
        if (beforeBoxsList.size() == 0){
            main_footer_text.setText("现在没有纸箱工单数据~ \n 快点击右下角进行添加纸箱操作吧~");
        }else{
            main_footer_text.setText("总共有" + beforeBoxsList.size() + "个纸箱");
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        ImageView searchButton = (ImageView) searchView.findViewById(R.id.search_button);
        textview = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        textview.setTextColor(getResources().getColor(R.color.text));
        searchView.setQueryHint("请输入纸箱型号：");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:

                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 初始化
     */
    private void init(){

        /**
         * 初始化控件
         */
        main_toolbar = (Toolbar) getActivity().findViewById(R.id.main_toolbar);
        main_footer = (LinearLayout) view.findViewById(R.id.main_footer);
        main_footer_text = (TextView) view.findViewById(R.id.main_footer_text);
        fragmentBoxLayout = (LinearLayout) view.findViewById(R.id.Fragment_BoxLayout);
        floatingActionsMenu = (FloatingActionsMenu) view.findViewById(R.id.fab_menu);
        addBoxButton = (FloatingActionButton) view.findViewById(R.id.fab_add);
        recordBoxButton = (FloatingActionButton) view.findViewById(R.id.fab_record);
        appaboutButton = (FloatingActionButton) view.findViewById(R.id.fab_appabout);

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
        appaboutButton.setOnClickListener(this);

        /**
         * 搜索框搜索功能
         */
//        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) { //搜索后功能
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {    //输入时文字变化
//
//                return true;
//            }
//        });

    }

    /**
     * 对数据进行排序
     */
    private void SortBoxsList(){
        List<Box> boxList = DataSupport.findAll(Box.class);
        String[] boxsId = new String[boxList.size()];
        int[] boxsItem = new int[boxList.size()];
        String tempBoxId = "";
        int tempBoxItem = 0;
        boxList = DataSupport.findAll(Box.class);

        for (int i = 0; i < boxList.size(); i++){
            boxsId[i] = boxList.get(i).getBox_id();
            boxsItem[i] = i;
        }
        //获取数据的数据id跟纸箱id

        for (int i = 0; i < boxList.size(); i++){

            for (int j = 0; j < boxList.size(); j++){


                if (boxsId[i].compareTo(boxsId[j]) < 0){

                    tempBoxItem = boxsItem[j];
                    boxsItem[j] = boxsItem[i];
                    boxsItem[i] = tempBoxItem;
                    //进行item值的替换

                    tempBoxId = boxsId[j];
                    boxsId[j] = boxsId[i];
                    boxsId[i] = tempBoxId;
                    //进行boxid值的替换

                }

                //31 22 54 12 11
            }// 22 31 54 12 11
        }

        for (int i = 0; i < boxList.size(); i++){
            this.boxsList.add(boxList.get(boxsItem[i]));
        }


    }

    private void setSearchView(String name){
        boxsList.clear();                                                       //清空数据
        for (int i = 0; i < boxsId.size(); i++){
            if (boxsId.get(i).toUpperCase().indexOf(name.toUpperCase()) != -1){        //忽略大小写，当包含该字符串 加入数据
                boxsList.add(beforeBoxsList.get(i));
            }
        }
        if (name.equals("") && beforeBoxsList.size() == 0){
            main_footer.setVisibility(View.VISIBLE);
        }else if (name.equals("") && beforeBoxsList.size() == boxsList.size()){
            main_footer.setVisibility(View.VISIBLE);
        }else {
            main_footer.setVisibility(View.GONE);
        }
        boxsAdapter.notifyDataSetChanged() ;                                    //更新数据
    }

    /**
     * 获取数据源
     */
    private void getBoxList(){
        beforeBoxsList = new ArrayList<>();
        boxsList = new ArrayList<>();
        boxsId = new ArrayList<>();

        SortBoxsList();
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
            case R.id.fab_appabout:
                intent = new Intent(getContext(),AboutAppActivity.class);
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
            if (boxNHnum < 0)
                boxNHnum = 0;
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
                                hideInputMethod();
                                break;
                            case R.id.delete_item:
                                DeleteActivity.actionStart(getContext(),box);
                                hideInputMethod();
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
