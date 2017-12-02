package com.example.xunself.x8hot_os;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
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

    private int network_status;


    private ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        network_status = intent.getIntExtra("network_status",0);
        Log.d("network_status",network_status+"");
        init();
//        imageButton = (ImageButton) findViewById(R.id.popup_button);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu popupMenu = new PopupMenu(MainActivity.this,imageButton);
//                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()){
//                            case R.id.add_item:
//                                Toast.makeText(MainActivity.this,"add",Toast.LENGTH_SHORT).show();
//                                break;
//                            case R.id.about_item:
//                                Toast.makeText(MainActivity.this,"about",Toast.LENGTH_SHORT).show();
//                                break;
//                            default:
//                                break;
//                        }
//                        return true;
//                    }
//                });
//                popupMenu.show();
//
//            }
//        });
    }

    /**
     * 初始化
     */
    private void init(){
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
            if (BoxsFragment == null || WorkorderFragment == null){
                BoxsFragment = new BoxsFragment();
                transaction.add(R.id.main_frameLayout,BoxsFragment);
                WorkorderFragment = new WorkorderFragment();
                transaction.add(R.id.main_frameLayout,WorkorderFragment);
            }
            transaction.hide(WorkorderFragment);
            transaction.show(BoxsFragment);
            boxsImage.setImageResource(R.drawable.ic_dns_blue_48dp);
            boxsText.setTextColor(getResources().getColor(R.color.colorselectedFragment));
            workOrderImage.setImageResource(R.drawable.ic_markunread_mailbox_black_48dp);
            workOrderText.setTextColor(getResources().getColor(R.color.colornoselectedFragment));
        }else if(fragment_Tag == WORKORDER_FRAGMENT){
            transaction.hide(BoxsFragment);
            transaction.show(WorkorderFragment);
            boxsImage.setImageResource(R.drawable.ic_dns_black_48dp);
            boxsText.setTextColor(getResources().getColor(R.color.colornoselectedFragment));
            workOrderImage.setImageResource(R.drawable.ic_markunread_mailbox_blue_48dp);
            workOrderText.setTextColor(getResources().getColor(R.color.colorselectedFragment));
        }
        transaction.commit();
    }
}
