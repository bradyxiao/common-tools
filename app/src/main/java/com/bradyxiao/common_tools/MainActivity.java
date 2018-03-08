package com.bradyxiao.common_tools;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tencent.qcloud.service_component.MainService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        ((MyApp)this.getApplication()).setValue("MainActivity", "the first activity");
    }

    private void initUI(){
        Button testBtn = (Button)findViewById(R.id.test);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final List<Map<String, String>> status = new ArrayList<>();
                HashMap<String, String> hashMap;
                hashMap = new HashMap<>();
                hashMap.put("name", "XIAO");
                hashMap.put("value", "YAO");
                status.add(hashMap);

                hashMap = new HashMap<>();
                hashMap.put("name", "FU");
                hashMap.put("value", "MEIJIA");
                status.add(hashMap);

                hashMap = new HashMap<>();
                hashMap.put("name", "FU");
                hashMap.put("value", "MEIJIA");
                status.add(hashMap);

                hashMap = new HashMap<>();
                hashMap.put("name", "FU");
                hashMap.put("value", "MEIJIA");
                status.add(hashMap);

                hashMap = new HashMap<>();
                hashMap.put("name", "FU");
                hashMap.put("value", "MEIJIA");
                status.add(hashMap);

                hashMap = new HashMap<>();
                hashMap.put("name", "FU");
                hashMap.put("value", "MEIJIA");
                status.add(hashMap);

                hashMap = new HashMap<>();
                hashMap.put("name", "FU");
                hashMap.put("value", "MEIJIA");
                status.add(hashMap);

                hashMap = new HashMap<>();
                hashMap.put("name", "FU");
                hashMap.put("value", "MEIJIA");
                status.add(hashMap);

                SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, status,
                        R.layout.layout_line, new String[]{"name", "value"}, new int[]{R.id.name, R.id.value});

                BaseAdapter baseAdapter = new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return status.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return status.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        ViewHolder viewHolder;
                        if(convertView == null){
                            viewHolder = new ViewHolder();
                            convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_line, null);
                            viewHolder.checkBox = convertView.findViewById(R.id.checkBox);
                            viewHolder.name = convertView.findViewById(R.id.name);
                            viewHolder.value = convertView.findViewById(R.id.value);
                            convertView.setTag(viewHolder);
                        }else {
                            viewHolder = (ViewHolder) convertView.getTag();
                        }

                        viewHolder.checkBox.setChecked(true);
                        Map<String, String> hashMap1 = status.get(position);
                        viewHolder.name.setText(hashMap1.get("name"));
                        viewHolder.value.setText(hashMap1.get("value"));

                        return convertView;
                    }
                };

                View selectView = getLayoutInflater().inflate(R.layout.layout_list, null);
                ListView listView = selectView.findViewById(R.id.listView);
                listView.setAdapter(adapter);


                new AlertDialog.Builder(MainActivity.this)
                        .setView(selectView)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, MyService.class);
                                startService(intent);
                            }
                        })
                        .show();
            }
        });
    }

    private static class ViewHolder{
        public CheckBox checkBox;
        public TextView name;
        public TextView value;
    }

}
