package com.example.xunself.x8hot_os;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XunselF on 2017/12/2.
 */

public class BoxsFragment extends Fragment implements View.OnClickListener{

    private List<Box> boxsList;
    private View view;

    private RecyclerView boxRecyclerView;
    private android.widget.SearchView searchView;

    private CoordinatorLayout fragmentBoxLayout;

    private InputMethodManager mInputMethodManager;
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
        searchView = (android.widget.SearchView) view.findViewById(R.id.searchView);
        fragmentBoxLayout = (CoordinatorLayout) view.findViewById(R.id.Fragment_BoxLayout);
        boxRecyclerView = (RecyclerView) view.findViewById(R.id.boxs_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        boxRecyclerView.setLayoutManager(linearLayoutManager);
        BoxsAdapter boxsAdapter = new BoxsAdapter();
        boxRecyclerView.setAdapter(boxsAdapter);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * 获取数据源
     */
    private void getBoxList(){
        boxsList = new ArrayList<>();
        for (int i = 0; i < 5; i ++){
            Box box1 = new Box("7544","HD1234",1000,500,200,"2017-12-22","备注");
            boxsList.add(box1);
            Box box2 = new Box("7544","HD5678",500,300,200,"2017-12-23","备注");
            boxsList.add(box2);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Fragment_BoxLayout:
                InputMethodManager imm = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;
            default:
                break;
        }
    }

    class BoxsAdapter extends RecyclerView.Adapter<BoxsAdapter.ViewHolder>{
        class ViewHolder extends RecyclerView.ViewHolder{
            LinearLayout boxItemLayout;
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
            Box box = boxsList.get(position);
            holder.boxItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mInputMethodManager.isActive()) {
                        mInputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);// 隐藏输入法
                        fragmentBoxLayout.requestFocus();//请求焦点
                        fragmentBoxLayout.findFocus();//获取焦点
                    }
                }
            });
            int boxNHnum = box.getBox_num() - box.getBox_hnum();                        //还剩箱子数量
            int dataNHnum = boxNHnum - box.getData_hnum();                              //材料剩余数量
            holder.boxId.setText(box.getBox_id());
            holder.boxNHnum.setText(boxNHnum + "");
            holder.dataHnum.setText(box.getData_hnum() + "");
            if (dataNHnum > 0){
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
                                Toast.makeText(getActivity(), "add", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.about_item:
                                Toast.makeText(getActivity(), "about", Toast.LENGTH_SHORT).show();
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
