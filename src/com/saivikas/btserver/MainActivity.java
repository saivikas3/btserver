package com.saivikas.btserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener {

	Button Connect,Up,Down,Right,Left;
	ToggleButton OnOff;
	TextView Result;
	private String dataToSend;
	
	private static final String TAG = "Vik";
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothSocket btSocket = null;
	private OutputStream outStream = null;
	private static String address = "30:14:12:17:05:91";
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private InputStream inStream = null;
    Handler handler = new Handler(); 
    byte delimiter = 10;
    boolean stopWorker = false;
    int readBufferPosition = 0;
    byte[] readBuffer = new byte[1024];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Connect = (Button) findViewById(R.id.connect);
		Up = (Button) findViewById(R.id.up);
		 Down = (Button) findViewById(R.id.down);
		 Right = (Button) findViewById(R.id.right);
		 Left = (Button) findViewById(R.id.left);
		Result = (TextView) findViewById(R.id.tv1);

		Connect.setOnClickListener(this);
		Up.setOnClickListener(this);
		Down.setOnClickListener(this);
		Right.setOnClickListener(this);
		Left.setOnClickListener(this);
		
		CheckBt();
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		Log.e("Vik", device.toString());

	}

	@Override
	public void onClick(View control) {
		switch (control.getId()) {
		case R.id.connect:
				Connect();
			break;
		case R.id.up:
			dataToSend = "w";
			writeData(dataToSend);
			break;
		case R.id.down:
			dataToSend = "s";
			writeData(dataToSend);
			break;
		case R.id.left:
			dataToSend = "a";
			writeData(dataToSend);
			break;
		case R.id.right:
			dataToSend = "d";
			writeData(dataToSend);
			break;
		}
	}

	private void CheckBt() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (!mBluetoothAdapter.isEnabled()) {
			Toast.makeText(getApplicationContext(), "Bluetooth Disabled !",
					Toast.LENGTH_SHORT).show();
		}

		if (mBluetoothAdapter == null) {
			Toast.makeText(getApplicationContext(),
					"Bluetooth null !", Toast.LENGTH_SHORT)
					.show();
		}
	}
	
		public void Connect() {
			Log.d(TAG, address);
			BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
			Log.d(TAG, "Connecting to ... " + device);
			mBluetoothAdapter.cancelDiscovery();
			try {
				btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
				btSocket.connect();
				Toast.makeText(getApplicationContext(),"Connection made.",5).show();
			} catch (IOException e) {
				try {
					btSocket.close();
				} catch (IOException e2) {
					Toast.makeText(getApplicationContext(),"Unable to end the connection",5).show();
					Log.d(TAG, "Unable to end the connection");
				}
				Toast.makeText(getApplicationContext(),"Socket creation failed",5).show();
				Log.d(TAG, "Socket creation failed");
			}
			
			//beginListenForData();
		}
	
	private void writeData(String data) {
		try {
			outStream = btSocket.getOutputStream();
		} catch (IOException e) {
			Log.d(TAG, "Bug BEFORE Sending stuff", e);
			Toast.makeText(getApplicationContext(),"Bug BEFORE Sending stuff",5).show();
		}

		String message = data;
		byte[] msgBuffer = message.getBytes();

		try {
			outStream.write(msgBuffer);
		} catch (IOException e) {
			Log.d(TAG, "Bug while sending stuff", e);
			Toast.makeText(getApplicationContext(),"Bug WHILE Sending stuff",5).show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	
			try {
				btSocket.close();
			} catch (IOException e) {
			}
	}
	
	
}
