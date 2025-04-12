package co.edu.uniminuto.actividad_evaluativa_3;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private Activity activity;
    private TextView versionAndroid;
    private int versionSDK;
    private ProgressBar pbLevelBaterry;
    private TextView tvLevelBaterry;
    private IntentFilter baterryFilter;
    private TextView tvConection;
    private ConnectivityManager conexion;
    private CameraManager cameraManager;
    private String cameraId;
    private Button onFlash;
    private Button offFlash;
    private EditText NameFile;
    private Button btnBluetooth;
    private Button btnCamera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initObjects();
        onFlash.setOnClickListener(this::onLight);
        offFlash.setOnClickListener(this::offLight);
        baterryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broReceiver, baterryFilter);
    }
    BroadcastReceiver broReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int levelBaterry = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            pbLevelBaterry.setProgress(levelBaterry);
            tvLevelBaterry.setText("levelBaterry:" +levelBaterry+ "%");
        }
    };


    private void initObjects() {
        this.context = getApplicationContext();
        this.activity = this;
        this.versionAndroid = findViewById(R.id.tvVersionAndroid);
        this.pbLevelBaterry = findViewById(R.id.pbLevelBaterry);
        this.tvLevelBaterry = findViewById(R.id.tvLevelBaterry);
        this.tvConection = findViewById(R.id.tvState);
        this.NameFile = findViewById(R.id.etNameFile);
        this.onFlash = findViewById(R.id.btnOn);
        this.offFlash = findViewById(R.id.btnOff);
        this.btnBluetooth = findViewById(R.id.btnBluetooth);
        this.btnCamera = findViewById(R.id.btnCamera);

        setupButtonListeners();


    }
    private void checkConection() {
        try {
            conexion = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conexion != null) {
                NetworkInfo networkInfo = conexion.getActiveNetworkInfo();
                boolean stateNet = networkInfo != null && networkInfo.isConnectedOrConnecting();
                if (stateNet) {
                    tvConection.setText("State ON");
                } else {
                    tvConection.setText("State OFF OR NO Info");
                }
            }

        } catch (Exception e) {
            Log.i("CONEXION", e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Version Android
        String versionSO = android.os.Build.VERSION.RELEASE;
        versionSDK = Build.VERSION.SDK_INT;
        versionAndroid.setText("version SO"+versionSO+ "/ SDK:" + versionSDK);
        checkConection();


    }

    private void offLight(View view) {
        try {
            cameraManager.setTorchMode(cameraId, false);
        }catch (Exception e){
            Log.i("Linterna:", e.getMessage());
        }
    }

    private void onLight(View view) {
        try {
            cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);

        }catch (Exception e){
            Log.i("Linterna:", e.getMessage());
        }
    }
    private void setupButtonListeners() {
        if (btnBluetooth != null) {
            btnBluetooth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Creación de un Intent para iniciar la BluetoothActivity
                    Intent intent = new Intent(MainActivity.this, BluetoothActivity.class);
                    startActivity(intent);
                }
            });
        }

        if (btnCamera != null) {
            btnCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Creación de un Intent para iniciar la CameraActivity
                    Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                    startActivity(intent);
                }
            });
        }
    }



}