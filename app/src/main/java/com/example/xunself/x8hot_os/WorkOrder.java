package com.example.xunself.x8hot_os;

import org.litepal.crud.DataSupport;

/**
 * Created by XunselF on 2017/12/4.
 */

public class WorkOrder extends DataSupport {
    private String work_id;     //订单号
    private String box_id;      //纸箱型号
    private int workOrder_status;       //工单状态
    private int update_BoxNumber;       //更改的纸箱数据
    private int update_DataNumber;      //更改的材料数据
    private String update_time;

    public WorkOrder(String work_id,String box_id,int workOrder_status,int update_BoxNumber,int update_DataNumber,String update_time){
        this.work_id = work_id;
        this.box_id = box_id;
        this.workOrder_status = workOrder_status;
        this.update_BoxNumber = update_BoxNumber;
        this.update_DataNumber = update_DataNumber;
        this.update_time = update_time;
    }

    public String getUpdate_time() {
        return update_time;
    }


    public String getWork_id() {
        return work_id;
    }

    public String getBox_id() {
        return box_id;
    }

    public int getUpdate_BoxNumber() {
        return update_BoxNumber;
    }

    public int getUpdate_DataNumber() {
        return update_DataNumber;
    }

    public int getWorkOrder_status() {
        return workOrder_status;
    }
}
