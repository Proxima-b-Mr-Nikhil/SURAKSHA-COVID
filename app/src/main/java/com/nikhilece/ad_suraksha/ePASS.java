package com.nikhilece.ad_suraksha;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ePASS extends AppCompatActivity implements View.OnClickListener {

    EditText selectDate,selectTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Button submit;
    CheckBox checkBox;
    ProgressBar progressBar;
    DatabaseReference reference;
    FirebaseUser fuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_p_a_s_s);

        String[] arraySpinner = new String[] {
             "--select--","Medical Reason", "Essential Activities"
        };
        final Spinner s = (Spinner) findViewById(R.id.reason);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        String[] arraySpinne = new String[] {
                "--select--","Two Wheeler", "Four Wheeler","Other large Vehicle"
        };
        final Spinner t = (Spinner) findViewById(R.id.type);
        ArrayAdapter<String> adapte = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinne);
        adapte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        t.setAdapter(adapte);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0) {
                    String sitem = parent.getItemAtPosition(position).toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        t.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0) {
                    String titem = parent.getItemAtPosition(position).toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        selectDate=(EditText)findViewById(R.id.journeydate);
        selectTime=(EditText)findViewById(R.id.journeytime);
        submit=(Button) findViewById(R.id.submit);
        checkBox= (CheckBox)findViewById(R.id.checkBox);
        progressBar=findViewById(R.id.progressBar);




        selectDate.setOnClickListener(this);
        selectTime.setOnClickListener(this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                    if (checkBox.isChecked()) {
                        EditText ownername=(EditText)findViewById(R.id.owner);
                        EditText ownerphone=(EditText)findViewById(R.id.ownerphone);
                        EditText owneremail=(EditText)findViewById(R.id.owneremail);
                        EditText drivername=(EditText)findViewById(R.id.driver);
                        EditText driverphone=(EditText)findViewById(R.id.driverphone);
                        EditText vereg=(EditText)findViewById(R.id.vehiclereg);
                        EditText vecap=(EditText)findViewById(R.id.vehiclecap);
                        EditText from=(EditText)findViewById(R.id.journeyfrom);
                        EditText to=(EditText)findViewById(R.id.journeyto);
                        EditText passengersnames=(EditText)findViewById(R.id.passengers);
                        String oemail=  owneremail.getText().toString();
                        String oname=  ownername.getText().toString();
                        String ophone=  ownerphone.getText().toString();
                        String dname=  drivername.getText().toString();
                        String dphone=  driverphone.getText().toString();
                        String reg=  vereg.getText().toString();
                        String cap=  vecap.getText().toString();
                        String journeyfrom=  from.getText().toString();
                        String jounryto=  to.getText().toString();
                        String passengers= passengersnames.getText().toString();


                        if (oname.equals("")){
                            ownername.setError("This field cannot be blank");
                            return;
                        }
                        if (ophone.equals("")){
                            ownerphone.setError("This field cannot be blank");
                            return;
                        }
                        if (oemail.equals("")){
                            owneremail.setError("This field cannot be blank");
                            return;
                        }
                        if (dname.equals("")){
                            drivername.setError("This field cannot be blank");
                            return;
                        }
                        if (dphone.equals("")){
                            driverphone.setError("This field cannot be blank");
                            return;
                        }
                        if (reg.equals("")){
                            vereg.setError("This field cannot be blank");
                            return;
                        }
                        if (cap.equals("")){
                            vecap.setError("This field cannot be blank");
                            return;
                        }
                        if (journeyfrom.equals("")){
                            from.setError("This field cannot be blank");
                            return;
                        }
                        if (jounryto.equals("")){
                            to.setError("This field cannot be blank");
                            return;
                        }
                        if (passengers.equals("")){
                            passengersnames.setError("This field cannot be blank");
                            return;
                        }

                        String reason=  s.getSelectedItem().toString();
                        String type=  t.getSelectedItem().toString();
                        if (reason.equals("--select--")){
                            s.setFocusable(true);
                            Toast.makeText(ePASS.this,"select reason", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (type.equals("--select--")){
                            t.setFocusable(true);
                            Toast.makeText(ePASS.this,"select vehicle type", Toast.LENGTH_LONG).show();
                            return;
                        }
                        EditText jrnydate=(EditText)findViewById(R.id.journeydate);
                        EditText jrnytime=(EditText)findViewById(R.id.journeytime);
                        String time=  jrnydate.getText().toString();
                        String date=  jrnytime.getText().toString();
                        if (date.equals("")){
                            jrnydate.setError("This field cannot be blank");
                            return;
                        }
                        if (time.equals("")){
                            jrnytime.setError("This field cannot be blank");
                            return;
                        }
                        String ftime =  String.valueOf(android.text.format.DateFormat.format("ddMMyyyyhhmmss", new Date()));

                        progressBar.setVisibility(View.VISIBLE);
                        fuser = FirebaseAuth.getInstance().getCurrentUser();
                        reference = FirebaseDatabase.getInstance().getReference("epass").push();



                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", fuser.getUid());
                        hashMap.put("ownername", oname);
                        hashMap.put("ownerphone", ophone);
                        hashMap.put("owneremail", oemail);
                        hashMap.put("drivername", dname);
                        hashMap.put("driverphone", dphone);
                        hashMap.put("vehicle reg no", reg);
                        hashMap.put("capacity", cap);
                        hashMap.put("from", journeyfrom);
                        hashMap.put("to",jounryto);
                        hashMap.put("passengers name", passengers);
                        hashMap.put("reason", reason);
                        hashMap.put("vehicle", type);
                        hashMap.put("journey date", date);
                        hashMap.put("journey time", time);
                        hashMap.put("unique id",ftime);

                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(ePASS.this,"submited successfully", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                   
                                    finish();
                                    startActivity(getIntent());

                                }
                            }
                        });


                    }


            }
        });

    }

    @Override
    public void onClick(View view) {

        if (view == selectDate) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            selectDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);


                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (view == selectTime) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            selectTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

    }
}
