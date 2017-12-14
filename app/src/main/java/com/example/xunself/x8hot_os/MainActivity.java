package com.example.xunself.x8hot_os;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{



    /**
     * 控件
     */
    private FrameLayout mainFrameLayout;                                //主页面视图
    private LinearLayout boxsLinearLayout;
    private ImageView boxsImage;
    private TextView boxsText;
    private LinearLayout workOrderLinearLayout;
    private ImageView workOrderImage;
    private TextView workOrderText;

    private Fragment BoxsFragment;
    private Fragment WorkorderFragment;


    /**
     * 参数
     */
    private final int BOXS_FRAGMENT = 1;
    private final int WORKORDER_FRAGMENT = 2;




    private ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        boxsLinearLayout = (LinearLayout) findViewById(R.id.boxs_main);
        workOrderLinearLayout = (LinearLayout) findViewById(R.id.workOrder_main);
        boxsText = (TextView) findViewById(R.id.boxs_text);
        boxsImage = (ImageView) findViewById(R.id.boxs_image);
        workOrderText = (TextView) findViewById(R.id.workOrder_text);
        workOrderImage = (ImageView) findViewById(R.id.workOrder_image);
        boxsLinearLayout.setOnClickListener(this);
        workOrderLinearLayout.setOnClickListener(this);
        initFramgent(BOXS_FRAGMENT);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.boxs_main:
                initFramgent(BOXS_FRAGMENT);
                break;
            case R.id.workOrder_main:
                initFramgent(WORKORDER_FRAGMENT);
                break;
            default:
                break;
        }
    }
    /**
     * 初始化framgment
     */
    private void initFramgent(int fragment_Tag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment_Tag == BOXS_FRAGMENT){
                BoxsFragment = new BoxsFragment();
            transaction.replace(R.id.main_frameLayout,BoxsFragment);
            boxsImage.setImageResource(R.drawable.box_fragment_src);
            boxsText.setTextColor(getResources().getColor(R.color.colorselectedFragment));
            workOrderImage.setImageResource(R.drawable.ic_dns_black_48dp);
            workOrderText.setTextColor(getResources().getColor(R.color.colornoselectedFragment));
        }else if(fragment_Tag == WORKORDER_FRAGMENT){
            WorkorderFragment = new WorkorderFragment();
            transaction.replace(R.id.main_frameLayout,WorkorderFragment);
            boxsImage.setImageResource(R.drawable.box_fragment_src_black);
            boxsText.setTextColor(getResources().getColor(R.color.colornoselectedFragment));
            workOrderImage.setImageResource(R.drawable.workorder_fragment_src);
            workOrderText.setTextColor(getResources().getColor(R.color.colorselectedFragment));
        }
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

}
