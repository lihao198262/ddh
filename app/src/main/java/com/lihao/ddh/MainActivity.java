package com.lihao.ddh;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final int REQUEST_PERMISSION_ALL = 181;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        this.context = this;

        SimpleDateFormat paramBundle = new SimpleDateFormat("yyyy-MM-dd");
        ((EditText) findViewById(R.id.text_date)).setText(paramBundle.format(new Date()));
        ((EditText) findViewById(R.id.text_time)).setText("08:30");

        findViewById(R.id.btn_data_pick).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("MainActivity", "onClick: ");
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(MainActivity.this, 0, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker paramAnonymous2DatePicker, int paramAnonymous2Int1, int paramAnonymous2Int2, int paramAnonymous2Int3) {
                        ((EditText) MainActivity.this.findViewById(R.id.text_date)).setText(String.format("%d-%02d-%02d", new Object[]{Integer.valueOf(paramAnonymous2Int1), Integer.valueOf(paramAnonymous2Int2 + 1), Integer.valueOf(paramAnonymous2Int3)}));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        findViewById(R.id.btn_time_pick).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Log.d("MainActivity", "onClick: ");
                new TimePickerDialog(MainActivity.this, 16974130, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        ((EditText) MainActivity.this.findViewById(R.id.text_time)).setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(hourOfDay), Integer.valueOf(minute)}));
                    }
                }, 8, 30, true).show();
            }
        });

        findViewById(R.id.btn_open_dingding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText dateText = (EditText) MainActivity.this.findViewById(R.id.text_date);
                EditText timeText = (EditText) MainActivity.this.findViewById(R.id.text_time);

                String temp = String.format("%s %s", new Object[]{dateText.getText(), timeText.getText()});
                Log.i(TAG, "onClick: " + temp);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    Date date = simpleDateFormat.parse(temp);
                    Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, (Intent) intent, 0);
                    ((AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
                    Toast.makeText(MainActivity.this.context, "ok", Toast.LENGTH_LONG);
                    MainActivity.this.findViewById(R.id.btn_open_dingding).setEnabled(false);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    void checkPermissions() {
        ArrayList localArrayList = new ArrayList();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != 0) {
            localArrayList.add(Manifest.permission.CHANGE_WIFI_STATE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.DISABLE_KEYGUARD) != 0) {
            localArrayList.add(Manifest.permission.DISABLE_KEYGUARD);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != 0) {
            localArrayList.add(Manifest.permission.INTERNET);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != 0) {
            localArrayList.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != 0) {
            localArrayList.add(Manifest.permission.ACCESS_WIFI_STATE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) != 0) {
            localArrayList.add(Manifest.permission.WAKE_LOCK);
        }

        if (localArrayList.size() == 0) {
            return;
        }
        String[] arrayOfString = new String[localArrayList.size()];
        int i = 0;
        while (i < localArrayList.size()) {
            arrayOfString[i] = ((String) localArrayList.get(i));
            i += 1;
        }
        ActivityCompat.requestPermissions(this, arrayOfString, REQUEST_PERMISSION_ALL);
    }

    public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt) {
        if (paramInt == REQUEST_PERMISSION_ALL) {
            if (paramArrayOfString.length != paramArrayOfInt.length) {
                Log.e(TAG, "onRequestPermissionsResult: permissions.length != grantResults.length");
                System.exit(0);
            }

            for (int i = 0; i < paramArrayOfString.length; i++) {
                StringBuilder localStringBuilder;
                if (paramArrayOfInt[i] == 0) {
                    localStringBuilder = new StringBuilder();
                    localStringBuilder.append("onRequestPermissionsResult: ");
                    localStringBuilder.append(String.format("%s PERMISSION_GRANTED", new Object[]{paramArrayOfString[i]}));
                    Log.i(TAG, localStringBuilder.toString());
                } else {
                    localStringBuilder = new StringBuilder();
                    localStringBuilder.append("onRequestPermissionsResult: ");
                    localStringBuilder.append(String.format("%s not PERMISSION_GRANTED exit", new Object[]{paramArrayOfString[i]}));
                    Log.i(TAG, localStringBuilder.toString());
                    System.exit(0);
                }

            }
        }
        super.onRequestPermissionsResult(paramInt, paramArrayOfString, paramArrayOfInt);
    }
}
