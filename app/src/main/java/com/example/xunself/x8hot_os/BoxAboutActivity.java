package com.example.xunself.x8hot_os;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BoxAboutActivity extends AppCompatActivity {

    private TextView box_idText;                            //纸箱型号
    private TextView work_idText;                           //订单号
    private TextView box_numText;                           //纸箱总数
    private TextView box_hnumText;                          //已完成纸箱
    private TextView box_prizeText;                         //纸箱单价
    private TextView box_sumprizeText;                      //纸箱总价
    private TextView box_nhnumText;                         //还剩纸箱
    private TextView data_hnumText;                         //剩余材料
    private TextView data_nhnumText;                        //还需材料订阅

    private Box seletedBox;

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
    /**
     * 初始化
     */
    private void init(){
        box_idText = (TextView) findViewById(R.id.boxs_id);
        work_idText = (TextView) findViewById(R.id.work_id);
        box_numText = (TextView) findViewById(R.id.boxs_num);
        box_hnumText = (TextView) findViewById(R.id.boxs_hnum);
        box_prizeText = (TextView) findViewById(R.id.boxs_prize);
        box_sumprizeText = (TextView) findViewById(R.id.boxs_sumprize);
        box_nhnumText = (TextView) findViewById(R.id.boxs_nhnum);
        data_hnumText = (TextView) findViewById(R.id.data_hnum);
        data_nhnumText = (TextView) findViewById(R.id.data_nhnum);

        getBoxData();
    }

    /**
     * 获取数据源
     */
    private void getBoxData(){
        seletedBox = (Box) getIntent().getSerializableExtra("selected_box");
        int boxNHnum = seletedBox.getBox_num() - seletedBox.getBox_hnum();                        //还剩箱子数量
        int dataNHnum = boxNHnum - seletedBox.getData_hnum();                              //材料剩余数量
        String sumprize = String.format("%.2f",seletedBox.getBox_prize() * (double)seletedBox.getBox_num() );
        box_idText.setText(seletedBox.getBox_id());
        work_idText.setText(seletedBox.getWork_id());
        box_numText.setText(seletedBox.getBox_num() + "");
        box_hnumText.setText(seletedBox.getBox_hnum() + "");
        box_nhnumText.setText(boxNHnum + "");
        box_prizeText.setText(seletedBox.getBox_prize() + "");
        box_sumprizeText.setText(sumprize);
        data_hnumText.setText(seletedBox.getData_hnum() + "");
        if (dataNHnum > 0){
            data_nhnumText.setTextColor(getResources().getColor(R.color.colorwarning));
            data_nhnumText.setText("(还需要"+ dataNHnum + "个材料)");
            dataWarningImage.setVisibility(View.VISIBLE);
        }else{
            holder.dataNHnum.setTextColor(getResources().getColor(R.color.colorPrimary));
            holder.dataNHnum.setText("(已经不需要材料)");
            holder.dataWarningImage.setVisibility(View.GONE);
        }
    }
}
