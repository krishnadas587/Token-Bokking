package com.example.tokenbooking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tokenbooking.DataBase.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BookAppointment extends AppCompatActivity {
    TextView tocken_num,time_allotted;
    EditText name,email,phone;
    int tk_no=1;
    String last_set_time="";
    String alloted_time="";
    String last_date="";
    String start_time="";
    String end_time="";
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        tocken_num=findViewById(R.id.tocken_no);
        time_allotted=findViewById(R.id.text_alloted_time);
        name=findViewById(R.id.text_name);
        email=findViewById(R.id.text_emai);
        phone=findViewById(R.id.text_phone);
        extras=getIntent().getExtras();
        alloted_time=extras.getString("avg_time");
        last_date=extras.getString("Current_date");
        start_time=extras.getString("start_time");
        end_time=extras.getString("end_time");
        doitstart();

    }

    private void doitstart() {
        getfromdb(last_date);
        tocken_num.setText("Token No : "+tk_no);
        Date time1 = null;
        if (extractit(last_set_time,0)==null){
            time_allotted.setText(start_time+" is Your Time");
        }else {
            try {
                time1 = new SimpleDateFormat("hh:mm aa").parse(extractit(last_set_time,0));
                Date date2 = new SimpleDateFormat("hh:mm aa").parse(end_time);
                String minute = extractit(alloted_time,1);
                SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.AM_PM, 0);
                cal.setTime(time1);
                try {
                    cal.add(Calendar.MINUTE, Integer.parseInt(minute));
                }catch (Exception ex){

                }

                String newTime = df.format(cal.getTime());
                if(new SimpleDateFormat("hh:mm aa").parse(newTime).before(date2)) {
                    time_allotted.setText(newTime+" is Your Time");
                } else {
                    show_dialog();
                }



            } catch (ParseException e) {
                time_allotted.setText(start_time+" is Your Time");
                e.printStackTrace();
            }

        }
    }


    private String extractit(String last_set_time, int type) {
        String time = null;
        System.out.println(last_set_time);
        if (type==0){
            try {
                time=last_set_time.substring(0,8);
            }catch (Exception e){

            }
        }else {
            try {
                time=last_set_time.substring(0,2);
               time = time.replaceAll("\\s", "");
            }catch (Exception e){

            }
        }


        return time;
    }
void show_dialog(){
    runOnUiThread(new Runnable() {
        @Override
        public void run() {

            if (!isFinishing()){
                new AlertDialog.Builder(BookAppointment.this)
                        .setTitle("No More Bookings")
                        .setMessage("Time Allotted is Over")
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                changedate(null);
                            }
                        }).show();
            }
        }
    });
}
    void create_mail(String email){
        System.out.println(email);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Your Token Is Generated");
        intent.putExtra(Intent.EXTRA_TEXT, "your token number is : "+tk_no+" and "+time_allotted.getText().toString());
        startActivity(Intent.createChooser(intent, "Token"));
        this.email.setText("");
        phone.setText("");
        name.setText("");


    }
    public void send(View view){
if(!email.getText().equals("")&&!phone.getText().equals("")&&!name.getText().equals("")){
    if(phone.getText().toString().length()==10&&email.getText().toString().contains("@")&&email.getText().toString().contains(".com")){
        databaseinsert(tk_no,last_date,name.getText().toString(),email.getText().toString(),phone.getText().toString(),time_allotted.getText().toString());
        create_mail(email.getText().toString());
        doitstart();
    }else {
        Toast.makeText(BookAppointment.this,"Fill Data correctly",Toast.LENGTH_SHORT).show();
    }

}
    }
    boolean databaseinsert(int token_no,String Date ,String name,String email,String phone,String time){
        database db=new database(getApplicationContext());
        return  db.insertdata(token_no,Date,email,name,phone,time);
    }
    void getfromdb(String date){
        database db = new database(getApplicationContext());
        Cursor cursor = db.getAlldata();

        while (cursor.moveToNext()){
            if (cursor.getString(2).equals(date)){
                tk_no = cursor.getInt(1)+1;
                last_set_time=cursor.getString(6);


            }

        }

    }
    public void changedate(View view){
        Intent intent=new Intent(BookAppointment.this,MainActivity.class);
        startActivity(intent);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=MainActivity.sharedPreferences.edit();
        editor.putBoolean("loginstatus",false);
        editor.apply();
    }
}