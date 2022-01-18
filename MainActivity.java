package com.ai.laari;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private final String TOP = "TOP";
    private final String DOWN = "DOWN";
    private final String LEFT = "LEFT";
    private final String RIGHT = "RIGHT";
    private final String IP_ADDRESS = "192.168.11.22";
    private final int PORT = 3310;

    private CardView _mCVRight, _mCVLeft, _mCVTop, _mCVDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViewsByIDS();

        // initializing and handling clicks and sending responses to vehicle.
        _mCVTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _operateVehicle(TOP);
            }
        });

        _mCVDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _operateVehicle(DOWN);
            }
        });

        _mCVRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _operateVehicle(RIGHT);
            }
        });

        _mCVLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _operateVehicle(LEFT);
            }
        });
    }

    void _operateVehicle(String action) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    Socket socket=new Socket(IP_ADDRESS,PORT);
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    try {
                        out.writeUTF(action);
                    }
                    catch(IOException i)
                    {
                        System.out.println(i.getMessage());
                    }
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    void initializeViewsByIDS() {
        _mCVLeft = findViewById(R.id.cv_left);
        _mCVRight = findViewById(R.id.cv_right);
        _mCVTop = findViewById(R.id.cv_top);
        _mCVDown = findViewById(R.id.cv_down);
    }

}