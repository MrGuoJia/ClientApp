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

public class InsertStudents extends AppCompatActivity {
    private EditText edt_id;
    private EditText edt_name;
    private EditText edt_password;
    private Button btn_sure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_students);
        initViews();

    }

    private void initViews() {
        edt_id= (EditText) findViewById(R.id.edt_id);
        edt_name= (EditText) findViewById(R.id.edt_name);
        edt_password= (EditText) findViewById(R.id.edt_password);
        btn_sure= (Button) findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id=edt_id.getText().toString().trim();
                final String name=edt_name.getText().toString().trim();
                final String password=edt_password.getText().toString().trim();
                if(id.length()==0){
                    Toast.makeText(InsertStudents.this,"ID不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else if(name.length()==0){
                    Toast.makeText(InsertStudents.this,"姓名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else if(password.length()==0){
                    Toast.makeText(InsertStudents.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i=new Intent();
               // final String ip="http://192.168.173.1:8080/StudentQuery/LoginServlet?options=insert&id="+id+"&name="+name+"&password="+password;
                final String ip="http://192.168.173.1:8080/StudentQuery/LoginServlet?options=insert";
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
                            out.writeBytes("id="+id+"&name="+name+"&password="+password);
                            Log.e("eeeeee==","id="+id+"&name="+name+"&password="+password);
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
                    }
                }).start();
                i.setClass(InsertStudents.this,MainActivity.class);
               startActivity(i);
                finish();
            }
        });

    }
}
