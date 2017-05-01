package com.example.jia.clientapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class DeleteActivity extends AppCompatActivity {
    private EditText edt_id;
    private Button btn_deleteID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        initViews();
    }

    private void initViews() {
        edt_id= (EditText) findViewById(R.id.edt_delete_id);
        btn_deleteID= (Button) findViewById(R.id.btn_deleteUser);

        btn_deleteID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id=edt_id.getText().toString().trim();
                if(id.length()==0){
                    Toast.makeText(DeleteActivity.this,"ID不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                final Intent i=new Intent();
                final String ip="http://192.168.173.1:8080/StudentQuery/LoginServlet?options=delete";
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
                            out.writeBytes("id="+id);
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
                        i.setClass(DeleteActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }).start();

            }
        });

    }
}
