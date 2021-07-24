package com.example.tokenbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
    TextView date_picker,Time_start,text_endtime,text_avg_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myCalendar2=Calendar.getInstance();
        date_picker = findViewById(R.id.date_picker);
        Time_start= findViewById(R.id.start_time);
        text_endtime= findViewById(R.id.end_time);
        text_avg_time= findViewById(R.id.avg_time);
        String myFormat = "hh:mm aa"; //In which you need put here
        sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
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

    public void Datefetcher(View view) {
        Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public void AllocateTocken(View view){
        if (!text_avg_time.getText().equals("Average Time Per Appointment(In  mins)")&&!date_picker.getText().equals("Select Date(Picker)")){
            Intent intent=new Intent(MainActivity.this,BookAppointment.class);
            intent.putExtra("tocken_number",String.valueOf(generate_random()));
            intent.putExtra("time_is",Time_start.getText());
            startActivity(intent);
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
    public void end_time_picker(View view) {
        Calendar myCalendar1 = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, end_time, myCalendar1.get(Calendar.HOUR), myCalendar1.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }

    private void updateTime(Date date) {
        Time_start.setText(sdf.format(date.getTime()));
        set_avg_time();
    }
    private void updateendTime(Date time) {
        text_endtime.setText(sdf.format(time.getTime()));
        set_avg_time();
    }

    private void set_avg_time() {
       if(!Time_start.getText().equals("Start Time(Picker)") && !text_endtime.getText().equals("End Time(Picker)")){
           try {

               set_time(text_avg_time,createminute(sdf.parse(Time_start.getText().toString()),sdf.parse(text_endtime.getText().toString())));
//               System.out.println(createminute(myCalendar.getTime(),myCalendar1.getTime()));
           }catch (ParseException ex){

           }


        }else {
           System.out.println("error");
       }

    }
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