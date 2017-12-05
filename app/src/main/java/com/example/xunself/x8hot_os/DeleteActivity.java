package com.example.xunself.x8hot_os;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

public class DeleteActivity extends AppCompatActivity implements View.OnClickListener{

    private Box selectedBox;

    private String box_id;

    private EditText boxIdText;

    private TextView deleteButton;
    private TextView returnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        init();
    }
    private void init(){
        selectedBox = (Box)getIntent().getSerializableExtra("selected_box");
        box_id = selectedBox.getBox_id();
        boxIdText = (EditText) findViewById(R.id.input_delete_order);
        deleteButton = (TextView) findViewById(R.id.delete_button);
        returnButton = (TextView) findViewById(R.id.delete_return);
        deleteButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);
    }
    public static void actionStart(Context context,Box box){
        Intent intent = new Intent(context,DeleteActivity.class);
        intent.putExtra("selected_box",box);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.delete_button:
                String boxId = boxIdText.getText().toString();
                if (!boxId.equals(box_id)){
                    Toast.makeText(DeleteActivity.this,"删除失败，填写型号与需要删除型号不同",Toast.LENGTH_SHORT).show();
                }else{
                    DataSupport.deleteAll(Box.class,"work_id = ? and box_id = ?",selectedBox.getWork_id(),selectedBox.getBox_id());
                    DataSupport.deleteAll(WorkOrder.class,"work_id = ? and box_id = ?",selectedBox.getWork_id(),selectedBox.getBox_id());
                    Toast.makeText(DeleteActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
            case R.id.delete_return:
                finish();
                break;
            default:
                break;
        }
    }
}
