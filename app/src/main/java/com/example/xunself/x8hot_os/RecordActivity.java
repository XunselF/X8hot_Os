package com.example.xunself.x8hot_os;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    private Toolbar record_toolBar;
    private RecyclerView record_recyclerview;
    private RecordAdapter adapter;

    private List<WorkOrder> recordList;

    private final int CREATE_NEW_ORDER = 0;
    private final int CARRY_BOX_NUMBER = 1;
    private final int ADD_DATA_NUMBER = 2;

    private TextView dataFooterText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        init();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取数据源
     */
    private void getData(){
        recordList = new ArrayList<>();
        recordList = DataSupport.findAll(WorkOrder.class);
        if (recordList.size() == 0){
            dataFooterText.setText("当前列表没有数据~");
        }else{
            dataFooterText.setText("已经到达底部~");
        }
        adapter.notifyDataSetChanged();
    }
    /**
     * 初始化
     */
    private void init(){
        record_toolBar = (Toolbar)findViewById(R.id.record_toolBar);
        setSupportActionBar(record_toolBar);
        dataFooterText = (TextView) findViewById(R.id.footer_text);
        record_recyclerview = (RecyclerView) findViewById(R.id.record_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        record_recyclerview.setLayoutManager(linearLayoutManager);
        adapter = new RecordAdapter();
        record_recyclerview.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:;
                break;
        }
        return true;
    }

    /**
     * 获取时间
     */
    private String getTime(String time){
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy年MM月dd号 HH:mm");

        SimpleDateFormat sdf3=new SimpleDateFormat("EEEE");
        SimpleDateFormat sdf4=new SimpleDateFormat("HH");

        StringBuilder strTime;

        try {
            Date oldDate = sdf1.parse(time);
            String oldTime = sdf2.format(oldDate);

            String day = sdf3.format(oldDate);
             int oldHour = Integer.parseInt(sdf4.format(oldDate));

            strTime = new StringBuilder(oldTime);
            int index = oldTime.indexOf(" ");
            if (oldHour < 5){
                //凌晨
                strTime.insert(index+1,day + " 凌晨 ");
            }else if (oldHour >= 6 && oldHour < 12){
                //早上
                strTime.insert(index+1,day + " 早上 ");
            } else if (oldHour >= 12 && oldHour < 14){
                //中午
                strTime.insert(index+1,day + " 中午 ");
            }else if (oldHour >= 14 && oldHour < 18){
                //下午
                strTime.insert(index+1,day + " 下午 ");
            }else{
                //晚上
                strTime.insert(index+1,day + " 晚上 ");
            }

        }catch (Exception e){
            e.printStackTrace();
            strTime = new StringBuilder();
        }


        return strTime.toString();
    }

    class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{

        class ViewHolder extends RecyclerView.ViewHolder{
            LinearLayout recordLayout;
            TextView recordTime;
            TextView recordBoxId;
            TextView recordWorkId;
            TextView recordStatus;
            TextView recordChangedText;
            TextView recordNumber;
            public ViewHolder(View itemView) {
                super(itemView);
                recordLayout = (LinearLayout) itemView.findViewById(R.id.record_layout);
                recordTime = (TextView) itemView.findViewById(R.id.record_time);
                recordBoxId = (TextView) itemView.findViewById(R.id.record_box_id);
                recordWorkId = (TextView) itemView.findViewById(R.id.record_work_id);
                recordStatus = (TextView) itemView.findViewById(R.id.record_status);
                recordChangedText = (TextView) itemView.findViewById(R.id.record_text);
                recordNumber = (TextView) itemView.findViewById(R.id.record_number);
            }
        }

        @Override
        public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(RecordActivity.this).inflate(R.layout.record_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecordAdapter.ViewHolder holder, int position) {
            final WorkOrder workOrder = recordList.get(recordList.size() - 1 - position);
            holder.recordWorkId.setText(workOrder.getWork_id());
            holder.recordBoxId.setText(workOrder.getBox_id());



            holder.recordTime.setText( getTime(workOrder.getUpdate_time()));
            switch (workOrder.getWorkOrder_status()){
                case CREATE_NEW_ORDER:
                    holder.recordStatus.setText("添加订单");
                    holder.recordStatus.setTextColor(getResources().getColor(R.color.colorAccent));
                    holder.recordChangedText.setText("纸箱：");
                    holder.recordNumber.setText(workOrder.getUpdate_BoxNumber() + "");
                    break;
                case CARRY_BOX_NUMBER:
                    holder.recordStatus.setText("已完成纸箱");
                    holder.recordStatus.setTextColor(getResources().getColor(R.color.boxcarryout));
                    holder.recordChangedText.setText("纸箱：");
                    holder.recordNumber.setText(workOrder.getUpdate_BoxNumber() + "");
                    break;
                case ADD_DATA_NUMBER:
                    holder.recordStatus.setText("新订材料");
                    holder.recordStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                    holder.recordChangedText.setText("材料：");
                    holder.recordNumber.setText(workOrder.getUpdate_DataNumber() + "");
                    break;
                default:
                    break;
            }
            holder.recordLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu longClick_menu = (PopupMenu) new PopupMenu(RecordActivity.this,holder.recordLayout);
                    longClick_menu.getMenuInflater().inflate(R.menu.longclick_delete,longClick_menu.getMenu());
                    longClick_menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.longclick_delete:
                                    WorkOrderDeleteActivity.actionStart(RecordActivity.this,workOrder);
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
            return recordList.size();
        }
    }
}
