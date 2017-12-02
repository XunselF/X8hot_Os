package com.example.xunself.x8hot_os;

/**
 * Created by XunselF on 2017/12/2.
 */

public class Box {
    private String work_id;     //订单号
    private String box_id;      //纸箱型号
    private int box_num;        //纸箱数量
    private int box_hnum;       //纸箱已做数量
    private int data_hnum;      //已有材料
    private String create_time; //时间
    private String content;     //备注
    public Box(String work_id,String box_id,int box_num,int box_hnum,int data_hnum,String create_time,String content){
        this.work_id = work_id;
        this.box_id = box_id;
        this.box_num = box_num;
        this.box_hnum = box_hnum;
        this.data_hnum = data_hnum;
        this.create_time = create_time;
        this.content = content;
    }

    public int getBox_num() {
        return box_num;
    }

    public int getData_hnum() {
        return data_hnum;
    }

    public String getBox_id() {
        return box_id;
    }

    public String getContent() {
        return content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getWork_id() {
        return work_id;
    }

    public int getBox_hnum() {
        return box_hnum;
    }
}
