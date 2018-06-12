package com.bradyxiao.common_tools;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.bradyxiao.log_tool.LogCatHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    LogCatHandler logCatHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        ((MyApp)this.getApplication()).setValue("MainActivity", "the first activity");

        logCatHandler = new LogCatHandler();
        logCatHandler.start();

        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        int limit = activityManager.getMemoryClass();
        Log.d("XIAO", "limit memory =" + limit);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void initUI(){

        Button downloadBtn = findViewById(R.id.test2);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String url = "http://androidtest-1253653367.cosgz.myqcloud.com/upload_service5.txt";
//                String localDir = MainActivity.this.getExternalCacheDir().getPath();
//                String localFile = "download.txt";
//                final DownloadRequest downloadRequest = new DownloadRequest(url, localDir, localFile);
//                downloadRequest.setRange(1024 * 10);
//                downloadRequest.setOnProgressListener(new OnProgressListener() {
//                    @Override
//                    public void onProgress(long receivedLength, long totalLength) {
//                        float progess = (float) (receivedLength * 1.00 / totalLength);
//                        progess = progess * 100;
//                        Log.d("DownloadTest",  receivedLength + "/" + totalLength + "-->" + (long)progess
//                                + "%");
//                    }
//                });
//                Config config = new Config();
//                config.isMultiThreadDownload = true;
//                Downloader downloader = new Downloader(config);
//
//                downloader.download(downloadRequest, new OnDownloadListener() {
//                    @Override
//                    public void onSuccess(DownloadRequest downloadRequest, DownloadResult downloadResult) {
//                        QLogger.xml("<H><a>DownloadTest Download Success</a><b>DownloadTest Download Success2</b></H>");
//                        File file = null;
//                        try {
//                            file = new File(downloadRequest.getLocalSavePath());
//                        } catch (ClientException e) {
//                            e.printStackTrace();
//                        }
//                        Log.d("DownloadTest", "file size =" + file.length());
//                    }
//
//                    @Override
//                    public void onFailed(DownloadRequest downloadRequest, ClientException clientException, ServerException serverException) {
//                        Exception e = clientException == null ? serverException : clientException;
//                        Log.d("DownloadTest", clientException == null ? serverException.getMessage() : clientException.getMessage());
//                        e.printStackTrace();
//                    }
//                });

            }
        });

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


        // Dynamic add permissions
        final int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck== PackageManager.PERMISSION_GRANTED) {
            Log.d("XIAO", "grant all permissions");
        } else if (permissionCheck <= PackageManager.PERMISSION_DENIED) {
            Log.d("XIAO", "need to grant permissions");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        logCatHandler.release();
        Log.d("XIAO", "onDestroy");
    }

    private static class ViewHolder{
        public CheckBox checkBox;
        public TextView name;
        public TextView value;
    }

}
