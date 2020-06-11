package com.yosua.wfhremainder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Date;

public class Detail extends AppCompatActivity {
    Remainder remainder;
    Button cancel;
    Button save;
    TextView title;
    TextView activity;
    TextView time;
    SwitchCompat status;
    TimePicker timePicker;
    Button delete;
    Database database;
    Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        title = findViewById(R.id.title);
        activity = findViewById(R.id.edit_activity);
        time = findViewById(R.id.showtime);
        timePicker = findViewById(R.id.tp);
        status = findViewById(R.id.changeswitch);
        delete = findViewById(R.id.delete);
        database = new Database();

        title.setText(getIntent().getStringExtra("title"));
        timePicker.setIs24HourView(true);
        time.setText(timePicker.getHour()+":"+timePicker.getMinute());
        remainder = new Remainder();

        if(getIntent().getStringExtra("activity") != null) {
            activity.setText(getIntent().getStringExtra("activity"));
            time.setText(getIntent().getStringExtra("time"));
            status.setChecked(getIntent().getExtras().getBoolean("status"));
            String timeData = getIntent().getStringExtra("time");
            String[] splittime = timeData.split(":");
            timePicker.setCurrentHour(Integer.parseInt(splittime[0]));
            timePicker.setCurrentMinute(Integer.parseInt(splittime[1]));
            id = getIntent().getExtras().getInt("id");
        }
        else{
            id = -1;
            cancel.setVisibility(View.GONE);
            save.setGravity(Gravity.CENTER);
        }


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if(minute < 10){
                    time.setText(hourOfDay + ":0" + minute);
                }
                else {
                    time.setText(hourOfDay + ":" + minute);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id != -1){
                    remainder.UpdateAlarm(Detail.this, time.getText().toString(), activity.getText().toString(), id);
                    database.add(new Data(id,time.getText().toString(), activity.getText().toString(), status.isChecked()));
                    Toast.makeText(Detail.this,"Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Detail.this, MainActivity.class);
                    Detail.this.startActivity(intent);
                    }
                else{
                    id = (int) (new Date().getTime() / 1000L ) % Integer.MAX_VALUE;
                    remainder.SetAlarm(Detail.this, time.getText().toString(), activity.getText().toString(), id);
                    database.add(new Data(id,time.getText().toString(), activity.getText().toString(), status.isChecked()));
                    Toast.makeText(Detail.this,"Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Detail.this, MainActivity.class);
                    Detail.this.startActivity(intent);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Detail.this)
                        .setTitle("Confimation")
                        .setMessage("Are you sure ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(remainder.checkIfAlarmIsRegistred(Detail.this, id)){
                                    remainder.cancelAlarm(Detail.this, id);
                                }

                                database.delete(id.toString());
                                Toast.makeText(Detail.this,"Deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Detail.this, MainActivity.class);
                                Detail.this.startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Detail.this, MainActivity.class);
                Detail.this.startActivity(intent);
            }
        });
    }




}
