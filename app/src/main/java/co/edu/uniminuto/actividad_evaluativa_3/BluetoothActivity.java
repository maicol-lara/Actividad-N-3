package co.edu.uniminuto.actividad_evaluativa_3;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;

    private BluetoothAdapter bluetoothAdapter;
    private TextView tvBluetoothState;
    private Button btnEnableBluetooth;
    private Button btnDisableBluetooth;
    private Button btnPairedDevices;
    private ListView lvListDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        this.tvBluetoothState = findViewById(R.id.tvBluetoothState );
        this.lvListDevices = findViewById(R.id.lvListDevices);
        this.btnEnableBluetooth = findViewById(R.id.btnEnableBluetooth);
        this.btnDisableBluetooth = findViewById(R.id.btnDisableBluetooth);
        this.btnPairedDevices = findViewById(R.id.btnPairedDevices);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            tvBluetoothState.setText("Bluetooth no soportado");
            return;
        }

        tvBluetoothState.setText(bluetoothAdapter.isEnabled() ? "Bluetooth ACTIVADO" : "Bluetooth DESACTIVADO");

        btnEnableBluetooth.setOnClickListener(v -> {
            if (!bluetoothAdapter.isEnabled()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_ENABLE_BT);
                } else {
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT);
                }
            }
        });

        btnDisableBluetooth.setOnClickListener(v -> {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
                tvBluetoothState.setText("Bluetooth DESACTIVADO");
            }
        });

        btnPairedDevices.setOnClickListener(v -> lvListDevices());
    }

    private void lvListDevices() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            Set<BluetoothDevice> dispositivos = bluetoothAdapter.getBondedDevices();
            ArrayList<String> nombres = new ArrayList<>();
            for (BluetoothDevice device : dispositivos) {
                nombres.add(device.getName() + " - " + device.getAddress());
            }
            lvListDevices.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombres));
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT);
            } else {
                Toast.makeText(this, "Bluetooth permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                lvListDevices();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                tvBluetoothState.setText("Bluetooth ACTIVADO");
            } else {
                tvBluetoothState.setText("Bluetooth DENEGADO");
            }
        }
    }
}