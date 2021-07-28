package com.example.tokenbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener start_time,end_time,avg_time;

    SimpleDateFormat sdf;
    Calendar myCalendar2;
    TextView date_picker,Time_start,text_endtime;
    Spinner text_avg_time;
    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myCalendar2=Calendar.getInstance();
        date_picker = findViewById(R.id.date_picker);
        Time_start= findViewById(R.id.start_time);
        text_endtime= findViewById(R.id.end_time);
        text_avg_time= findViewById(R.id.avg_time);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String myFormat = "hh:mm aa"; //In which you need put here
        sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        //setup_minute_array
        String[] avg_minutes = new String[61];
        setspinner(text_avg_time,setminutes(avg_minutes));

        if (sharedPreferences.getBoolean("loginstatus", false)) {
            Intent intent=new Intent(MainActivity.this,BookAppointment.class);
            intent.putExtra("avg_time",sharedPreferences.getString("avg_time",""));
            intent.putExtra("Current_date",sharedPreferences.getString("Current_date",""));

            intent.putExtra("start_time",sharedPreferences.getString("start_time",""));
            System.out.println("jbsdjbfs "+sharedPreferences.getString("start_time",""));
            intent.putExtra("end_time",sharedPreferences.getString("end_time",""));
            startActivity(intent);
            finish();
        }



        start_time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar2.set(Calendar.HOUR, hourOfDay);
                myCalendar2.set(Calendar.MINUTE, minute);
                myCalendar2.set(Calendar.AM_PM, 0);
                updateTime(myCalendar2.getTime());
            }
        };
        end_time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar2.set(Calendar.HOUR, hourOfDay);
                myCalendar2.set(Calendar.MINUTE, minute);
                myCalendar2.set(Calendar.AM_PM, 0);
                updateendTime(myCalendar2.getTime());
            }
        };

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, month);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar2.getTime());
            }


        };
    }

    private String[] setminutes(String[] avg_minutes) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                avg_minutes[0]="Average time (Minutes)";
                for(int i=1;i<=60;i++){
                    avg_minutes[i]=String.valueOf(i)+" Minutes";
                }
            }
        }).start();
        return avg_minutes;
    }

    public void Datefetcher(View view) {
        Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public void AllocateTocken(View view){

        if (!text_avg_time.getSelectedItem().toString().equals("Average time (Minutes)")&&!date_picker.getText().equals("Select Date(Picker)")){
            try {
               if ( new SimpleDateFormat("hh:mm aa").parse(Time_start.getText().toString()).before(new SimpleDateFormat("hh:mm aa").parse(text_endtime.getText().toString()))){
                    Intent intent=new Intent(MainActivity.this,BookAppointment.class);
                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean("loginstatus",true);
                    editor.putString("avg_time",text_avg_time.getSelectedItem().toString());
                    editor.putString("Current_date",date_picker.getText().toString());
                    editor.putString("start_time",Time_start.getText().toString());
                    editor.putString("end_time",text_endtime.getText().toString());
                    editor.apply();
                    intent.putExtra("avg_time",text_avg_time.getSelectedItem().toString());
                    intent.putExtra("Current_date",date_picker.getText().toString());
                    intent.putExtra("start_time",Time_start.getText().toString());
                    intent.putExtra("end_time",text_endtime.getText().toString());

                    startActivity(intent);
                    finish();
                }
               else {
                   Toast.makeText(MainActivity.this,"Fill all fields",Toast.LENGTH_SHORT).show();
               }
            } catch (ParseException e) {
                Toast.makeText(MainActivity.this,"Fill all fields",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }else {
            Toast.makeText(MainActivity.this,"Fill all fields",Toast.LENGTH_SHORT).show();
        }

    }
int generate_random(){
        Random r = new Random();
        int low = 10;
        int high = 100;
        int result = r.nextInt(high-low) + low;
        System.out.println(result);
        return result;
    }
    private void updateLabel(Date date) {
        String myFormat = "YYYY-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        date_picker.setText(sdf.format(date.getTime()));
    }

    public void time_picker(View view) {
        Calendar myCalendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, start_time, myCalendar.get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }

    void setspinner(Spinner spin,String[] array){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
    }
    public void end_time_picker(View view) {
        Calendar myCalendar1 = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, end_time, myCalendar1.get(Calendar.HOUR), myCalendar1.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }

    private void updateTime(Date date) {
        Time_start.setText(sdf.format(date.getTime()));
//        set_avg_time();
    }
    private void updateendTime(Date time) {
        text_endtime.setText(sdf.format(time.getTime()));
//        set_avg_time();
    }
//
//    private void set_avg_time() {
//       if(!Time_start.getText().equals("Start Time(Picker)") && !text_endtime.getText().equals("End Time(Picker)")){
//           try {
//
//               set_time(text_avg_time,createminute(sdf.parse(Time_start.getText().toString()),sdf.parse(text_endtime.getText().toString())));
////               System.out.println(createminute(myCalendar.getTime(),myCalendar1.getTime()));
//           }catch (ParseException ex){
//
//           }
//
//
//        }else {
//           System.out.println("error");
//       }
//
//    }
void set_time(TextView textView ,int time){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(String.valueOf(time));
            }
        });
}
    private int createminute(Date start, Date End) throws ParseException {
        int time=0;

        System.out.println(start);
        System.out.println(End);


        // Calculating the difference in milliseconds
        long differenceInMilliSeconds
                = Math.abs(End.getTime() - start.getTime());

        // Calculating the difference in Hours
        long differenceInHours
                = (differenceInMilliSeconds / (60 * 60 * 1000))
                % 24;

        // Calculating the difference in Minutes
        long differenceInMinutes
                = (differenceInMilliSeconds / (60 * 1000)) % 60;

        // Calculating the difference in Seconds
        long differenceInSeconds
                = (differenceInMilliSeconds / 1000) % 60;

        // Printing the answer
        System.out.println(
                "Difference is " + differenceInHours + " hours "
                        + differenceInMinutes + " minutes "
                        + differenceInSeconds + " Seconds. ");

time= (int) ((differenceInHours*60)+differenceInMinutes);
       return time ;
    }



}