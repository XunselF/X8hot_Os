package com.example.xunself.x8hot_os;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class WorkOrderDeleteActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView delete_workOrderText;
    private TextView delete_return;

    private WorkOrder delete_workOrder;                                                          //要删除的工单
    private String update_workId;                                                                 //要改变的订单号
    private String update_boxId;                                                                  //要改变的型号
    private int workOrder_status;                                                                //订单状态
    private int workOrder_boxNumber;                                                            //改变的箱子数量
    private int workOrder_dataNumber;                                                         //改变的材料数量


    private Box update_box;
    private int update_boxNum;                                                                   //纸箱总数
    private int update_boxHNum;                                                                  //纸箱已做数量
    private int update_dataHNum;                                                                 //材料数量
    private int update_boxCarryStatus;                                                          //纸箱完成状态

    private final int NOT_CARRY_OUT = 0;                                                        //未完成状态
    private final int CARRY_OUT = 1;                                                             //完成状态

    /**
     * 工单状态值
     */
    private final int CREATE_NEW_ORDER = 0;                     //创建新订单
    private final int CARRY_BOX_NUMBER = 1;                     //完成订单
    private final int ADD_DATA_NUMBER = 2;                      //添加材料
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order_delete);
        init();
    }
    public static void actionStart(Context context,WorkOrder workOrder){
        Intent intent = new Intent(context,WorkOrderDeleteActivity.class);
        intent.putExtra("delete_workorder",workOrder);
        context.startActivity(intent);
    }
    /**
     * 初始化
     */
    private void init(){
        delete_workOrder = (WorkOrder) getIntent().getSerializableExtra("delete_workorder");       //获取传过来的workorder值p
        update_workId = delete_workOrder.getWork_id();
        update_boxId = delete_workOrder.getBox_id();
        workOrder_status = delete_workOrder.getWorkOrder_status();
        workOrder_boxNumber = delete_workOrder.getUpdate_BoxNumber();
        workOrder_dataNumber = delete_workOrder.getUpdate_DataNumber();


        getboxData();
        delete_workOrderText = (TextView) findViewById(R.id.delete_work_order);
        delete_return = (TextView) findViewById(R.id.delete_return);


        delete_workOrderText.setOnClickListener(this);
        delete_return.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delete_work_order:
                delete_workOrder();
                Toast.makeText(WorkOrderDeleteActivity.this,"",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.delete_return:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 获取数据源
     */
    private void getboxData(){
        List<Box> boxList = DataSupport.where("work_id = ? and box_id = ?",update_workId,update_boxId).find(Box.class);     //按照条件找出数据
        update_box = boxList.get(0);
        update_boxNum = update_box.getBox_num();
        update_boxHNum = update_box.getBox_hnum();
        update_dataHNum = update_box.getData_hnum();
        update_boxCarryStatus = update_box.getIsCarryOut();
    }

    private void delete_workOrder(){
        switch (workOrder_status){
            case CREATE_NEW_ORDER:
                update_boxNum -= workOrder_boxNumber;
                if (update_boxHNum == 0)
                    update_box.setToDefault("box_num");
                else
                    update_box.setBox_num(update_boxNum);
                break;
            case CARRY_BOX_NUMBER:
                update_boxHNum -= workOrder_boxNumber;
                update_dataHNum += workOrder_boxNumber;
                if (update_boxHNum == 0)
                    update_box.setToDefault("box_hnum");
                else
                    update_box.setBox_hnum(update_boxHNum);
                if (update_dataHNum == 0)
                    update_box.setToDefault("data_hnum");
                else
                    update_box.setData_hnum(update_dataHNum);
                break;
            case ADD_DATA_NUMBER:
                update_dataHNum -= workOrder_dataNumber;
                if (update_dataHNum == 0){
                    update_box.setToDefault("data_hnum");
                }else{
                    update_box.setData_hnum(update_dataHNum);
                }
                break;
            default:
                break;
        }
        if (update_boxHNum >= update_boxNum){
            update_box.setIsCarryOut(CARRY_OUT);
        }else{
            update_box.setToDefault("isCarryOut");
        }
        DataSupport.delete(WorkOrder.class,delete_workOrder.getId());                                                                  //工单数据删除
        update_box.updateAll("work_id = ? and box_id = ?",update_workId,update_boxId);
    }
}
