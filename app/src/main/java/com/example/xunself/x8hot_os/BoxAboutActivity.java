package com.example.xunself.x8hot_os;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class BoxAboutActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private TextView box_idText;                            //纸箱型号
    private TextView work_idText;                           //订单号
    private TextView box_numText;                           //纸箱总数
    private TextView box_hnumText;                          //已完成纸箱
    private TextView box_nhnumText;                         //还剩纸箱
    private ImageView dataWarningImage;                     //数据红字图片
    private TextView data_hnumText;                         //剩余材料
    private TextView data_nhnumText;                        //还需材料订阅
    private TextView box_contentText;                       //备注
    private TextView box_createtime;                        //创建时间
    private TextView box_carryStatus;                       //纸箱完成状态
    private TextView footerText;
    private Box seletedBox;
    private List<Box> boxList;
    private RecyclerView box_workOrder_recyclerview;
    private BoxWorkOrderAdapter boxWorkOrderAdapter;
    private List<WorkOrder> workOrderList;


    private final int CREATE_NEW_ORDER = 0;
    private final int CARRY_BOX_NUMBER = 1;
    private final int ADD_DATA_NUMBER = 2;

    private final int NOT_CARRY_OUT = 0;
    private final int CARRY_OUT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_about);
        init();
    }
    public static void actionStart(Context context,Box seletedBox){
        Intent intent = new Intent(context,BoxAboutActivity.class);
        intent.putExtra("selected_box",seletedBox);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boxList = new ArrayList<>();
        boxList = DataSupport.where("box_id = ? and work_id = ?",seletedBox.getBox_id(),seletedBox.getWork_id()).find(Box.class);
            seletedBox = boxList.get(0);
        workOrderList = new ArrayList<>();
        workOrderList = DataSupport.where("box_id = ? and work_id = ?",seletedBox.getBox_id(),seletedBox.getWork_id()).find(WorkOrder.class);
        boxWorkOrderAdapter.notifyDataSetChanged();
        getBoxData();
        getFooterData();
    }
    private void getFooterData(){
        if (workOrderList.size() == 0){
            footerText.setText("没有工单数据~");
        }else{
            footerText.setText("总共有" + workOrderList.size() + "条工单数据~");
        }

    }

    /**
     * 初始化
     */
    private void init(){
        seletedBox = (Box) getIntent().getSerializableExtra("selected_box");
        toolBar = (Toolbar) findViewById(R.id.boxabout_toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        box_idText = (TextView) findViewById(R.id.boxs_id);
        work_idText = (TextView) findViewById(R.id.work_id);
        box_numText = (TextView) findViewById(R.id.boxs_num);
        box_hnumText = (TextView) findViewById(R.id.boxs_hnum);
        box_nhnumText = (TextView) findViewById(R.id.boxs_nhnum);
        dataWarningImage = (ImageView) findViewById(R.id.data_warning_image);
        data_hnumText = (TextView) findViewById(R.id.data_hnum);
        data_nhnumText = (TextView) findViewById(R.id.data_nhnum);
        box_contentText = (TextView) findViewById(R.id.boxs_content);
        box_createtime = (TextView) findViewById(R.id.boxs_createtime);
        box_workOrder_recyclerview = (RecyclerView) findViewById(R.id.box_workorder_recyclerview);
        footerText = (TextView) findViewById(R.id.footer);
        box_carryStatus = (TextView) findViewById(R.id.boxs_carryStatus);
        boxWorkOrderAdapter = new BoxWorkOrderAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        box_workOrder_recyclerview.setLayoutManager(linearLayoutManager);
        box_workOrder_recyclerview.setAdapter(boxWorkOrderAdapter);
        getBoxData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_edit:
                WorkOrderActivity.actionStart(BoxAboutActivity.this,seletedBox);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 获取数据源
     */
    private void getBoxData(){

        int boxNHnum = seletedBox.getBox_num() - seletedBox.getBox_hnum();                        //还剩箱子数量
        if (boxNHnum < 0){
            boxNHnum = 0;
        }
        int dataNHnum = boxNHnum - seletedBox.getData_hnum();                              //材料剩余数量
        String sumprize = String.format("%.2f",seletedBox.getBox_prize() * (double)seletedBox.getBox_num() );
        box_idText.setText(seletedBox.getBox_id());
        work_idText.setText(seletedBox.getWork_id());
        box_numText.setText(seletedBox.getBox_num() + "");
        box_hnumText.setText(seletedBox.getBox_hnum() + "");
        box_nhnumText.setText(boxNHnum + "");
        data_hnumText.setText(seletedBox.getData_hnum() + "");
        box_contentText.setText(seletedBox.getContent());
        box_createtime.setText(seletedBox.getCreate_time());
        if (seletedBox.getIsCarryOut() == NOT_CARRY_OUT){
            box_carryStatus.setText("(未完成)");
            box_carryStatus.setTextColor(getResources().getColor(R.color.boxnocarryout));
        }else if (seletedBox.getIsCarryOut() == CARRY_OUT){
            box_carryStatus.setText("(已完成)");
            box_carryStatus.setTextColor(getResources().getColor(R.color.boxcarryout));
        }

        if (dataNHnum > 0){
            data_nhnumText.setTextColor(getResources().getColor(R.color.colorwarning));
            data_nhnumText.setText("(还需要"+ dataNHnum + "个材料)");
            dataWarningImage.setVisibility(View.VISIBLE);
        }else{
            data_nhnumText.setTextColor(getResources().getColor(R.color.colorPrimary));
            data_nhnumText.setText("(已经不需要材料)");
            dataWarningImage.setVisibility(View.GONE);
        }
    }
    class BoxWorkOrderAdapter extends RecyclerView.Adapter<BoxWorkOrderAdapter.ViewHolder>{

        class ViewHolder extends RecyclerView.ViewHolder{
            LinearLayout workOrder_layout;
            TextView workOrder_time;
            TextView workOrder_status;
            TextView workOrder_number;
            TextView workOrder_Text;
            public ViewHolder(View itemView) {
                super(itemView);
                workOrder_layout = (LinearLayout) itemView.findViewById(R.id.box_about_layout);
                workOrder_time = (TextView) itemView.findViewById(R.id.box_update_time);
                workOrder_status = (TextView) itemView.findViewById(R.id.box_udpate_status);
                workOrder_number = (TextView) itemView.findViewById(R.id.box_udpate_number);
                workOrder_Text = (TextView) itemView.findViewById(R.id.box_udpate_text);
            }
        }

        @Override
        public BoxWorkOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(BoxAboutActivity.this).inflate(R.layout.box_about_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final WorkOrder workOrder = workOrderList.get(workOrderList.size() - 1 - position);
            holder.workOrder_time.setText(workOrder.getUpdate_time());
            switch (workOrder.getWorkOrder_status()){
                case CREATE_NEW_ORDER:
                    holder.workOrder_status.setText("添加订单");
                    holder.workOrder_number.setText(workOrder.getUpdate_BoxNumber() + "");
                    holder.workOrder_status.setTextColor(getResources().getColor(R.color.colorAccent));
                    holder.workOrder_Text.setText("个纸盒");
                    break;
                case CARRY_BOX_NUMBER:
                    holder.workOrder_status.setText("已完成");
                    holder.workOrder_number.setText(workOrder.getUpdate_BoxNumber() + "");
                    holder.workOrder_status.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    holder.workOrder_Text.setText("个纸盒");
                    break;
                case ADD_DATA_NUMBER:
                    holder.workOrder_status.setText("已新订");
                    holder.workOrder_number.setText(workOrder.getUpdate_DataNumber() + "");
                    holder.workOrder_status.setTextColor(getResources().getColor(R.color.colorPrimary));
                    holder.workOrder_Text.setText("个材料");
                    break;
                default:
                    break;
            }
            holder.workOrder_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu longClick_menu = (PopupMenu) new PopupMenu(BoxAboutActivity.this,holder.workOrder_layout);
                    longClick_menu.getMenuInflater().inflate(R.menu.longclick_delete,longClick_menu.getMenu());
                    longClick_menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.longclick_delete:
                                    WorkOrderDeleteActivity.actionStart(BoxAboutActivity.this,workOrder);
                                    break;
                                    default:
                                        break;
                            }
                            return true;
                        }
                    });
                    longClick_menu.show();
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return workOrderList.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.box_about_menu,menu);
        return true;
    }

}
