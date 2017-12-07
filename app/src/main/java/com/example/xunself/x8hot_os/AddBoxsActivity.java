package com.example.xunself.x8hot_os;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddBoxsActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar AddBoxs_toolbar;
    private LinearLayout addBoxLayout;
    private TextView welcome_1;
    private TextView welcome_2;
    private EditText inputBoxId;
    private EditText inputWorkId;
    private EditText inputBoxNumber;
    private EditText inputBoxContent;
    private EditText inputBoxPrize;
    private EditText inputBoxHNumber;
    private EditText inputDataHNumber;

    private String mBoxId;
    private String mWorkId;
    private int mBoxNumber;
    private int mBoxHNumber = 0;
    private double mBoxPrize;
    private int mDataHNumber = 0;
    private String mBoxCreateTime;
    private String mBoxContent;

    private Button commitButton;
    private Button clearButton;
    private Button finallyCommitButton;
    private Button backButton;

    private final int NOT_CARRY_OUT = 0;
    private final int CARRY_OUT = 1;
    private boolean isInputMoreMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_boxs);
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        AddBoxs_toolbar = (Toolbar) findViewById(R.id.addbox_toolbar);
        setSupportActionBar(AddBoxs_toolbar);
        welcome_1 = (TextView) findViewById(R.id.welcome_1);
        welcome_2 = (TextView) findViewById(R.id.welcome_2);
        addBoxLayout = (LinearLayout) findViewById(R.id.add_boxs_layout);
        inputBoxId = (EditText) findViewById(R.id.input_boxId);
        inputWorkId = (EditText) findViewById(R.id.input_workId);
        inputBoxNumber = (EditText) findViewById(R.id.input_boxNumber);
        inputBoxContent = (EditText) findViewById(R.id.input_boxContent);
        inputBoxPrize = (EditText) findViewById(R.id.input_boxPrize);
        inputBoxHNumber = (EditText) findViewById(R.id.input_boxHNumber);
        inputDataHNumber = (EditText) findViewById(R.id.input_dataHNumber);
        commitButton = (Button) findViewById(R.id.commit_button);
        clearButton = (Button) findViewById(R.id.clear_button);
        finallyCommitButton = (Button) findViewById(R.id.finallyCommit_button);
        backButton = (Button) findViewById(R.id.Back_button);

        addBoxLayout.setOnClickListener(this);
        commitButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
        finallyCommitButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        LitePal.getDatabase();
    }
    /**
     * 隐藏输入法
     */
    private void hideInputMethodManager(){                                       //隐藏键盘
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mInputMethodManager.isActive()) {
            mInputMethodManager.hideSoftInputFromWindow(addBoxLayout.getWindowToken(), 0);// 隐藏输入法
            addBoxLayout.clearFocus();    //输入框失去焦点
            addBoxLayout.setFocusable(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (isInputMoreMessage){
                    isInputMoreMessage = false;
                    transformView();
                }else{
                    finish();
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_boxs_layout:
                hideInputMethodManager();
                break;
            case R.id.finallyCommit_button:
                String boxHNumber = inputBoxHNumber.getText().toString();
                String dataHNumber = inputDataHNumber.getText().toString();
                if (!boxHNumber.trim().equals("")){
                    mBoxHNumber = Integer.parseInt(boxHNumber);
                    if (mBoxHNumber >= mBoxNumber){
                        Toast.makeText(AddBoxsActivity.this,"已完成数量不能比总数大",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (!dataHNumber.trim().equals("")){
                mDataHNumber = Integer.parseInt(dataHNumber);
            }
                getTime();
                showDialogContent();
                break;
            case R.id.Back_button:
                isInputMoreMessage = false;
                transformView();
                break;
            case R.id.commit_button:
                try{
                    String boxId = inputBoxId.getText().toString();
                    String workId = inputWorkId.getText().toString();
                    String boxNumber = inputBoxNumber.getText().toString();
                    String boxContent = inputBoxContent.getText().toString();
                    String boxPrize = inputBoxPrize.getText().toString();
                    if (boxId.trim().equals("") || boxId == null){
                        Toast.makeText(AddBoxsActivity.this,"请输入纸箱型号！",Toast.LENGTH_SHORT).show();
                    }else if (workId.trim().equals("") || workId == null){
                        Toast.makeText(AddBoxsActivity.this,"请输入订单号！",Toast.LENGTH_SHORT).show();
                    }else if (boxNumber.trim().equals("") || boxNumber.trim().equals("0")){
                        Toast.makeText(AddBoxsActivity.this,"纸箱数量不可为0！",Toast.LENGTH_SHORT).show();
                    }else if (boxPrize.trim().equals("") || boxPrize == null){
                        Toast.makeText(AddBoxsActivity.this,"请输入单价！",Toast.LENGTH_SHORT).show();
                    }else if (boxId.length() > 30){
                        Toast.makeText(AddBoxsActivity.this,"纸箱型号过长！不得超过30个字符",Toast.LENGTH_SHORT).show();
                    }else if (workId.length() > 40) {
                        Toast.makeText(AddBoxsActivity.this,"订单型号过长！不得超过40个字符",Toast.LENGTH_SHORT).show();
                    }else{
                        mBoxId = boxId;
                        mWorkId = workId;
                        mBoxNumber = Integer.parseInt(boxNumber);
                        mBoxPrize = Double.parseDouble(boxPrize);
                        mBoxContent = boxContent;
                        AlertDialog.Builder dialog = new AlertDialog.Builder(AddBoxsActivity.this);
                        dialog.setTitle("询问");
                        dialog.setMessage("是否输入已有纸箱数量或者已有材料数量？");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                isInputMoreMessage = true;
                                transformView();
                            }
                        });
                        dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getTime();
                                showDialogContent();
                            }
                        });
                        dialog.show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
            case R.id.clear_button:             //清空
                inputBoxId.setText("");
                inputWorkId.setText("");
                inputBoxContent.setText("");
                inputBoxPrize.setText("");
                inputBoxNumber.setText("");
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
        mBoxCreateTime = sdf.format(date);
    }
    /**
     * 变换视图
     */
    private void transformView(){
        if (isInputMoreMessage){
            welcome_1.setText("请输入已有纸箱数量或者已有材料数量");
            welcome_2.setText("如果没有某一参数请留空即可");
            inputBoxHNumber.setVisibility(View.VISIBLE);
            inputDataHNumber.setVisibility(View.VISIBLE);
            finallyCommitButton.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
            inputBoxId.setVisibility(View.GONE);
            inputWorkId.setVisibility(View.GONE);
            inputBoxContent.setVisibility(View.GONE);
            inputBoxPrize.setVisibility(View.GONE);
            inputBoxNumber.setVisibility(View.GONE);
            commitButton.setVisibility(View.GONE);
            clearButton.setVisibility(View.GONE);
        }else{
            welcome_1.setText("欢迎来到X8hot_Os!");
            welcome_2.setText("请依次填写纸箱型号、型号、纸箱数量、备注");
            inputBoxHNumber.setVisibility(View.GONE);
            inputDataHNumber.setVisibility(View.GONE);
            finallyCommitButton.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);
            inputBoxId.setVisibility(View.VISIBLE);
            inputWorkId.setVisibility(View.VISIBLE);
            inputBoxContent.setVisibility(View.VISIBLE);
            inputBoxPrize.setVisibility(View.VISIBLE);
            inputBoxNumber.setVisibility(View.VISIBLE);
            commitButton.setVisibility(View.VISIBLE);
            clearButton.setVisibility(View.VISIBLE);
        }
    }
    private void showDialogContent(){
        final AlertDialog dialogContent = new AlertDialog.Builder(AddBoxsActivity.this).create();
        dialogContent.show();
        Window window = dialogContent.getWindow();
        window.setContentView(R.layout.dialog_boxmessage);
        TextView contentText = (TextView) window.findViewById(R.id.dialog_content);
        contentText.setText("纸箱型号：" + mBoxId + "\n"
        + "订单号：" + mWorkId + "\n" +
        "总数：" + mBoxNumber +  "\n" +
                "已做数量：" + mBoxHNumber + "\n" +
        "已有材料：" + mDataHNumber + "\n" +
        "单价：" + mBoxPrize + "\n" +
        "备注：" + mBoxContent + "\n" +
        "提交时间：" + mBoxCreateTime);
        dialogContent.setCancelable(false);
        TextView commitText = (TextView) window.findViewById(R.id.dialog_commit);
        TextView exitText = (TextView) window.findViewById(R.id.dialog_exit);
        commitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Box box = new Box(mWorkId,mBoxId,mBoxNumber,mBoxHNumber,mDataHNumber,mBoxPrize,mBoxCreateTime,mBoxContent,NOT_CARRY_OUT);
                box.save();
                Toast.makeText(AddBoxsActivity.this,"提交成功！",Toast.LENGTH_SHORT).show();
                dialogContent.dismiss();
                finish();

            }
        });
        exitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogContent.dismiss();
            }
        });
    }
}
