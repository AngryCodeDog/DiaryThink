package com.example.diarythink;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.diarythink.bean.TaskInfo;
import com.example.diarythink.views.ItemTaskView;

import java.util.ArrayList;

public class MainActivity extends Activity {

//    private ListView lvEventList;
    private LinearLayout llContent;
    private ArrayList<TaskInfo> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initData();
        initView();
    }

    private void initData(){
        for (int i = 0; i < 10; i++) {
           TaskInfo taskInfo = new TaskInfo(false,"任务--"+i);
            arrayList.add(taskInfo);
        }
    }

    private void initView() {
        llContent = (LinearLayout) findViewById(R.id.ll_content);
//        lvEventList = new ListView(this);
//        for (int i = 0; i < 20; i++) {
//            ItemTaskView itemTaskView = new ItemTaskView(this);
//            llContent.addView(itemTaskView);
//        }
//        lvEventList.setAdapter(new ThingsAdapter(arrayList));

    }

   /* class ThingsAdapter extends BaseAdapter{

        private ArrayList<TaskInfo> taskInfoList = new ArrayList<TaskInfo>();
        public ThingsAdapter(ArrayList<TaskInfo> list){
            this.taskInfoList = list;

        }

        @Override
        public int getCount() {
            return taskInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return taskInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder myViewHolder;
            if(convertView == null){
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.thingsview,null);
                myViewHolder = new ViewHolder();
                myViewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_ok);
                myViewHolder.textView = (TextView) convertView.findViewById(R.id.tv_descripe);
                convertView.setTag(myViewHolder);
            }else{
                myViewHolder = (ViewHolder) convertView.getTag();
            }
            myViewHolder.checkBox.setChecked(taskInfoList.get(position).isComplete());
            myViewHolder.textView.setText(taskInfoList.get(position).getTaskDescripe());
            myViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        myViewHolder.textView.setBackgroundColor(Color.GREEN);
                    }else {
                        myViewHolder.textView.setBackgroundColor(Color.LTGRAY);
                    }
                }
            });
            return convertView;
        }

        class ViewHolder{
            CheckBox checkBox;
            TextView textView;
        }
    }*/
}
