package com.example.xunself.x8hot_os;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.ArrayRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WorkOrderActivity extends AppCompatActivity implements View.OnClickListener{

    private Box mBox;
    private String box_id;
    private String work_id;
    private int box_num;
    private int box_hnum;
    private int data_hnum;
    private String mUpdateTime;
    private Spinner operaSpinner;
    private final String[] OPERA = {"添加新的订单","已完成纸箱","新订材料"};
    private ArrayAdapter adapter;


    private EditText inputNum;
    private TextView changeText;
    private TextView commitButton;
    private TextView returnButton;

    private int workOrder_status;

    private final int CREATE_NEW_ORDER = 0;
    private final int CARRY_BOX_NUMBER = 1;
    private final int ADD_DATA_NUMBER = 2;


    private final int NOT_CARRY_OUT = 0;
    private final int CARRY_OUT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order);
        init();
    }


    public static void actionStart(Context context, Box box){
        Intent intent = new Intent(context,WorkOrderActivity.class);
        intent.putExtra("selected_Box",box);
        context.startActivity(intent);
    }
    private void init(){
        mBox = (Box)getIntent().getSerializableExtra("selected_Box");
        box_id = mBox.getBox_id();
        work_id = mBox.getWork_id();
        box_num = mBox.getBox_num();
        box_hnum = mBox.getBox_hnum();
        data_hnum = mBox.getData_hnum();
        workOrder_status = CREATE_NEW_ORDER;
        inputNum = (EditText) findViewById(R.id.work_order_inputNum) ;
        changeText = (TextView) findViewById(R.id.work_order_changeText);
        commitButton = (TextView) findViewById(R.id.work_order_commit);
        returnButton = (TextView) findViewById(R.id.work_order_return);
        operaSpinner = (Spinner) findViewById(R.id.opera_spinner);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,OPERA);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operaSpinner.setAdapter(adapter);
        commitButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        operaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                workOrder_status = i;
                changeUi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.work_order_return:
                finish();
                break;
            case R.id.work_order_commit:
                String inputText = inputNum.getText().toString();
                if (inputText == null || inputText.trim().equals("") || inputText.trim().equals("0")){
                    Toast.makeText(WorkOrderActivity.this,"数量不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    int input = Integer.parseInt(inputText.trim());
                    updateData(input);
                    finish();
                }
                break;
        }
    }
    private void updateData(int input){
        int isCarryOut = 0;
        WorkOrder workOrder;
        Box updateBox;
        getTime();
        switch (workOrder_status){
            case CREATE_NEW_ORDER:
                workOrder = new WorkOrder(work_id,box_id,workOrder_status,input,0,mUpdateTime);
                workOrder.save();
                updateBox = mBox;
                updateBox.setBox_num(mBox.getBox_num() + input);
                updateBox.setToDefault("isCarryOut");
                updateBox.updateAll("box_id = ? and work_id = ?",box_id,work_id);

                break;
            case CARRY_BOX_NUMBER:
                workOrder = new WorkOrder(work_id,box_id,workOrder_status,input,0,mUpdateTime);
                workOrder.save();
                if (input + box_hnum >= box_num ) {
                    isCarryOut = CARRY_OUT;
                }
                data_hnum = data_hnum - input;
                input = input + box_hnum;
                updateBox = mBox;
                updateBox.setIsCarryOut(isCarryOut);
                if (data_hnum == 0){
                    updateBox.setToDefault("data_hnum");
                }else{
                    updateBox.setData_hnum(data_hnum);
                }
                updateBox.setBox_hnum(input);
                updateBox.updateAll("box_id = ? and work_id = ?",box_id,work_id);
                break;
            case ADD_DATA_NUMBER:
                workOrder = new WorkOrder(work_id,box_id,workOrder_status,0,input,mUpdateTime);
                workOrder.save();
                updateBox = mBox;
                if (input + data_hnum == 0){
                    updateBox.setToDefault("data_hnum");
                }else{
                    updateBox.setData_hnum(input + data_hnum);
                }
                updateBox.updateAll("box_id = ? and work_id = ?",box_id,work_id);
                break;
            default:
                break;
        }
        Toast.makeText(WorkOrderActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
    }


    private void changeUi(){
        switch (workOrder_status){
            case CREATE_NEW_ORDER:
                changeText.setText("个纸盒");
                break;
            case CARRY_BOX_NUMBER:
                changeText.setText("个纸盒");
                break;
            case ADD_DATA_NUMBER:
                changeText.setText("个材料");
                break;
            default:
                break;
        }
    }
    /**
     * 获取时间
     */
    private void getTime(){
        Date date = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        mUpdateTime = sdf.format(date);
    }
}
