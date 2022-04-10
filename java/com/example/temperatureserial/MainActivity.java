package com.example.temperatureserial;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;

    MyDatabase myDB;
    ArrayList<String> moment;
    ArrayList<Integer> temperature;
    CustomAdapter customAdapter;

    // for bth serial board -> 1 universal unique identifier
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabase(MainActivity.this);
        moment = new ArrayList<String>();
        temperature = new ArrayList<Integer>();

        storeDataInArrays();    // récupère les datas de temperature table

        customAdapter = new CustomAdapter(MainActivity.this, moment, temperature);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    void storeDataInArrays() {
        Cursor cursor = myDB.readLast24Temps();
        if(cursor.getCount()==0){
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                moment.add(cursor.getString(0));
                temperature.add(cursor.getInt(1));
            }
        }
    }

}