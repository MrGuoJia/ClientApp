package com.example.jia.clientapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class MainActivity extends AppCompatActivity {
    private Button btn_query;
    private Button btn_update;
    private Button btn_insert;
    private Button btn_delete;
    private TextView textView;
    private String options="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

    }

    private void initViews() {
        btn_query= (Button) findViewById(R.id.btn_visit);
        btn_update= (Button) findViewById(R.id.btn_update);
        btn_insert= (Button) findViewById(R.id.btn_insert);
        btn_delete= (Button) findViewById(R.id.btn_delete);
        textView= (TextView) findViewById(R.id.tv_msg);
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options="query";
                new Thread(new Runnable() {//查询所有数据
                    @Override
                    public void run() {
                        String ip="http://192.168.173.1:8080/StudentQuery/LoginServlet?options="+options;
                        try {
                            URL url=new URL(ip);
                            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                            connection.setConnectTimeout(10000);
                            connection.connect();
                            InputStream inputStream=connection.getInputStream();
                            final StringBuilder builder=new StringBuilder();
                            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
                            String line;
                            while ((line=reader.readLine())!=null){
                                builder.append(line);
                            }
                            reader.close();
                            connection.disconnect();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ShowMessage(builder);
                                }
                            });

                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                            Log.e("访问异常原因:-------",e.toString());
                        }
                    }
                }).start();


            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {//更新数据
            @Override
            public void onClick(View v) {
                options="update";
                String ip="http://192.168.173.1:8080/StudentQuery/LoginServlet?options="+options;
                Intent s=new Intent(MainActivity.this,UpdateActivity.class);
                startActivity(s);

            }
        });

        btn_insert.setOnClickListener(new View.OnClickListener() { //插入数据
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,InsertStudents.class);
                startActivity(i);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent d=new Intent(MainActivity.this,DeleteActivity.class);
                startActivity(d);
            }
        });
    }

    private void ShowMessage(StringBuilder builder) {
        textView.setText(builder);
        Log.e("========",""+builder);
    }
}
