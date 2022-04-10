package com.example.temperatureserial;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class AddActivity extends AppCompatActivity {

    //EditText temperature_input;
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");  // Bth serie unique universel id
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //temperature_input = findViewById(R.id.temperature_input);

        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                MyDatabase myDB = new MyDatabase(AddActivity.this);
                //myDB.scanTemperature(Integer.valueOf(temperature_input.getText().toString().trim()));

                // BLUETOOTH PART
                BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                System.out.println(btAdapter.getBondedDevices());       // Afficher les adresses MACs des appareils couplés
                System.out.println("######################################################################################");

                BluetoothDevice hc05 = btAdapter.getRemoteDevice("98:D3:C1:FD:65:69");  // hc05 mac address
                System.out.println(hc05.getName()); // affiche nom de l'appareil bluetooth pour être certain d'utiliser le bon

                // Création socket client (telephone) server (hc05)
                BluetoothSocket btSocket = null;
                int counter = 0;
                do {
                    try {
                        btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                        System.out.println(btSocket);
                        // Connect to HC05 server
                        btSocket.connect();
                        System.out.println("Est connecté : " + btSocket.isConnected());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    counter++;
                } while (!btSocket.isConnected() && counter < 3);

                OutputStream outputStream = null;
                try {   // read the temperature and insert it in the database
                    InputStream inputStream = btSocket.getInputStream();
                    inputStream.skip(inputStream.available()); // residual buffer data
                    int temperatureReceived = inputStream.read();
                    myDB.scanTemperature(temperatureReceived);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Close connexion
                try {
                    btSocket.close();
                    System.out.println("est déconnecté : " + !btSocket.isConnected());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}