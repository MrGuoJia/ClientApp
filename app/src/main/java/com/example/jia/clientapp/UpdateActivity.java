package com.example.jia.clientapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateActivity extends AppCompatActivity {
    private EditText edt_id;
    private EditText edt_name;
    private EditText edt_password;
    private Button btn_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initViews();
    }

    private void initViews() {
        edt_id= (EditText) findViewById(R.id.edtId);
        edt_name= (EditText) findViewById(R.id.edtname);
        edt_password= (EditText) findViewById(R.id.edtPassword);
        btn_update= (Button) findViewById(R.id.btn_update);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String stuId=edt_id.getText().toString().trim();
                final String stuName=edt_name.getText().toString().trim();
                final String stuPassword=edt_password.getText().toString().trim();

                if(stuId.length()==0){
                    Toast.makeText(UpdateActivity.this,"ID不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else if(stuName.length()==0){
                    Toast.makeText(UpdateActivity.this,"姓名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else if(stuPassword.length()==0){
                    Toast.makeText(UpdateActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                final Intent i=new Intent();
                final String ip="http://192.168.173.1:8080/StudentQuery/LoginServlet?options=update";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        URL url= null;
                        try {
                            url = new URL(ip);
                            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setConnectTimeout(10000);
                            connection.connect();
                            DataOutputStream out=new DataOutputStream(connection.getOutputStream());
                            out.writeBytes("id="+stuId+"&name="+stuName+"&password="+stuPassword);
                            InputStream inputStream=connection.getInputStream();
                            final StringBuilder builder=new StringBuilder();
                            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
                            String line;
                            while ((line=reader.readLine())!=null){
                                builder.append(line);
                            }
                            reader.close();
                            connection.disconnect();

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        i.setClass(UpdateActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }).start();

            }
        });

    }
}
