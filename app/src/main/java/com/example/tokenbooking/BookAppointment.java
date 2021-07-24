package com.example.tokenbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BookAppointment extends AppCompatActivity {
    Bundle extras;
    TextView tocken_num,time_allotted;
    EditText name,email,phone;
    String tocken_number;
    String time_allot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        extras = getIntent().getExtras();
        assert extras != null;
        tocken_num=findViewById(R.id.tocken_no);
        time_allotted=findViewById(R.id.text_alloted_time);
        tocken_number=extras.getString("tocken_number");
        time_allot=extras.getString("time_is");
        name=findViewById(R.id.text_name);
        email=findViewById(R.id.text_emai);
        phone=findViewById(R.id.text_phone);
        tocken_num.setText("Token No :"+tocken_number);
        time_allotted.setText("Alloted Time Is :"+time_allot);


    }
    void create_mail(String email){
        System.out.println(email);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Your Token Is Generated");
        intent.putExtra(Intent.EXTRA_TEXT, "your token number is : "+tocken_number+" and time is : "+time_allot);
        startActivity(Intent.createChooser(intent, "Token"));
    }
    public void send(View view){
if(!email.getText().equals("")&&!phone.getText().equals("")&&!name.getText().equals("")){
    if(phone.getText().toString().length()==10&&email.getText().toString().contains("@")&&email.getText().toString().contains(".com")){
        create_mail(email.getText().toString());
    }else {
        Toast.makeText(BookAppointment.this,"Fill Data correctly",Toast.LENGTH_SHORT).show();
    }

}
    }
}