package com.example.xunself.x8hot_os;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by XunselF on 2017/12/2.
 */

public class Box extends DataSupport implements Serializable{
    private String work_id;     //订单号
    private String box_id;      //纸箱型号
    private int box_num;        //纸箱数量
    private int box_hnum;       //纸箱已做数量
    private int data_hnum;      //已有材料
    private double box_prize;     //纸箱单价
    private String create_time; //时间
    private String content;     //备注
    private int isCarryOut;    //是否完成

    public Box(){

    }
    public Box(String work_id,String box_id,int box_num,int box_hnum,int data_hnum,double box_prize,String create_time,String content,int isCarryOut){
        this.work_id = work_id;
        this.box_id = box_id;
        this.box_num = box_num;
        this.box_hnum = box_hnum;
        this.data_hnum = data_hnum;
        this.box_prize = box_prize;
        this.create_time = create_time;
        this.content = content;
        this.isCarryOut = isCarryOut;
    }

    public int getIsCarryOut() {
        return isCarryOut;
    }

    public void setIsCarryOut(int isCarryOut) {
        this.isCarryOut = isCarryOut;
    }

    public void setBox_hnum(int box_hnum) {
        this.box_hnum = box_hnum;
    }

    public void setBox_id(String box_id) {
        this.box_id = box_id;
    }

    public void setBox_num(int box_num) {
        this.box_num = box_num;
    }

    public void setBox_prize(double box_prize) {
        this.box_prize = box_prize;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setData_hnum(int data_hnum) {
        this.data_hnum = data_hnum;
    }

    public void setWork_id(String work_id) {
        this.work_id = work_id;
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

    public double getBox_prize() {
        return box_prize;
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
