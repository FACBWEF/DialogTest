package com.test.bwef.dialogtest;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private long mExitTime = 0;
    private Button button_single,button_multi,button_time,button_date;
    private TextView textView_single, textView_multi,textView_time,textView_date,textView_progress;
    private boolean progressrunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //单选框
        button_single = (Button)findViewById(R.id.button_single);
        button_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowsingleChooseItems();
            }
        });
        //多选框
        button_multi = (Button)findViewById(R.id.button_multi);
        button_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowMultiDialog();
            }
        });
        textView_single = (TextView)findViewById(R.id.textView_single);
        textView_multi = (TextView)findViewById(R.id.textView_multi);
        textView_time = (TextView)findViewById(R.id.textView_time);
        textView_date = (TextView)findViewById(R.id.textView_date);
        textView_progress = (TextView)findViewById(R.id.textView_progress);
    }

    private String singleChoose;
    private String[] single = {"JAVA","C","C++","C#"};
    private String[] multi = {"Android","IOS","WP"};
    private StringBuilder multiChoose;

    public void ShowsingleChooseItems(){
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("单选")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        singleChoose = single[0];
                        Toast.makeText(MainActivity.this,"Choose:" + singleChoose, Toast.LENGTH_SHORT).show();
                        textView_single.append(singleChoose);
                        textView_single.setVisibility(View.VISIBLE);
                        dialogInterface.dismiss();
                    }
                })
                .setSingleChoiceItems(single, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        singleChoose = single[i];
                    }
                })
                .create();
        dialog.show();
    }

    public void ShowMultiDialog(){
        multiChoose = new StringBuilder();
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("Multi Choose")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "You chose " + multiChoose,Toast.LENGTH_SHORT).show();
                        textView_multi.append(multiChoose);
                        textView_multi.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      dialogInterface.dismiss();
                    }
                })
                .setMultiChoiceItems(multi, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            multiChoose.append(multi[i] + "、");
                        }else{
                            int start = multiChoose.indexOf(multi[i]);
                            int end = start + multi[i].length() + 1;
                            multiChoose.delete(start,end);
                        }
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if((System.currentTimeMillis() - mExitTime) > 1000){
                ShowDialog();
                mExitTime = System.currentTimeMillis();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void ShowDialog(){
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("退出程序？")
                .setMessage("确定退出程序？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        dialog.show();
    }

    //时间选择对话框
    public void timePickerDialog(View view){
        Dialog dialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int m) {
                Toast.makeText(MainActivity.this, "The time you choose:" + hourOfDay + ":" + m, Toast.LENGTH_SHORT).show();
                textView_time.append(hourOfDay + ":" + m);
                textView_time.setVisibility(View.VISIBLE);
            }
        },14, 20, true);
        dialog.show();
    }

    //日期选择对话框
    public void datePickerDialog(View view){
        Dialog dialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Toast.makeText(MainActivity.this, "The date you choose:" + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日",Toast.LENGTH_SHORT).show();
                textView_date.append(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                textView_date.setVisibility(View.VISIBLE);
            }
        },2017,1,1);
        dialog.show();
    }

    public void progressFinished(View view){
        if(progressrunning){
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please wait this progress end!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }else{
            progressDialog(view);
        }
    }

    //进程对话框
    public void progressDialog(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("DownLoading Progress");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.setButton("HIDE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                textView_progress.setVisibility(View.VISIBLE);
            }
        });
        progressDialog.onStart();
        new Thread() {
            @Override
            public void run() {
                int i = 0;
                progressrunning = true;
                while (i < 100) {
                    try {
                        Thread.sleep(100);
                        progressDialog.incrementProgressBy(1);
                        Message msg = mHandler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putInt("progress", progressDialog.getProgress());
                        msg.setData(bundle);
                        msg.sendToTarget();
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                progressrunning = false;
                progressDialog.dismiss();
            }
        }.start();
        progressDialog.show();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            textView_progress.setText("The progress of downloading:" + msg.getData().getInt("progress") + "%");
        }
    };

    //自定义对话框
    public void customDialog(View view){

    }
}
