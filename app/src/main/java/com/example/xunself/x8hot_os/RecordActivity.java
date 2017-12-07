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

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    private Toolbar record_toolBar;
    private RecyclerView record_recyclerview;
    private RecordAdapter adapter;

    private List<WorkOrder> recordList;

    private final int CREATE_NEW_ORDER = 0;
    private final int CARRY_BOX_NUMBER = 1;
    private final int ADD_DATA_NUMBER = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        getData();
        init();
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
    }
    /**
     * 初始化
     */
    private void init(){
        record_toolBar = (Toolbar)findViewById(R.id.record_toolBar);
        setSupportActionBar(record_toolBar);
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
            holder.recordTime.setText(workOrder.getUpdate_time());
            switch (workOrder.getWorkOrder_status()){
                case CREATE_NEW_ORDER:
                    holder.recordStatus.setText("添加订单");
                    holder.recordStatus.setTextColor(getResources().getColor(R.color.colorAccent));
                    holder.recordChangedText.setText("纸箱：");
                    holder.recordNumber.setText(workOrder.getUpdate_BoxNumber() + "");
                    break;
                case CARRY_BOX_NUMBER:
                    holder.recordStatus.setText("已完成纸箱");
                    holder.recordStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
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
