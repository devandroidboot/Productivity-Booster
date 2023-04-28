package com.codegama.productivity_booster.activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.codegama.productivity_booster.database.DatabaseClient;
import com.codegama.productivity_booster.R;
import com.codegama.productivity_booster.bottomSheetFragment.CreateTaskBottomSheetFragment;
import com.codegama.productivity_booster.dao.user;
import com.codegama.productivity_booster.model.User;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    int mYear, mMonth, mDay;
    int mHour, mMinute;
    CreateTaskBottomSheetFragment.setRefreshListener setRefreshListener;
    AlarmManager alarmManager;
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            Window w =getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setContentView(R.layout.activity_register);

        TextView loginTextView = findViewById(R.id.login_textview);
        loginTextView.setMovementMethod(LinkMovementMethod.getInstance());


        ClickableSpan clickableSpanLogin = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(getResources().getColor(R.color.colorAccent));
            }
        };

        SpannableString spannableLoginText = new SpannableString(loginTextView.getText());
        int startIndexRegister = spannableLoginText.toString().indexOf("Login");
        int endIndexRegister = startIndexRegister + "Login".length();

        spannableLoginText.setSpan(clickableSpanLogin, startIndexRegister, endIndexRegister, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginTextView.setText(spannableLoginText);


        EditText firstname = findViewById(R.id.first_name);

        EditText lastname = findViewById(R.id.last_name);

        EditText birthdate = findViewById(R.id.birth_date);

        EditText email = findViewById(R.id.email);

        EditText password = findViewById(R.id.password);

        EditText password2 = findViewById(R.id.password2);


        birthdate.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(this,
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            birthdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            datePickerDialog.dismiss();
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
            return true;
        });


        Button register_btn = (Button) findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Creating User Entity
                User userEntity = new User();
                userEntity.setFirst_name(firstname.getText().toString());
                userEntity.setLast_name(lastname.getText().toString());

                userEntity.setBirth_date(birthdate.getText().toString());


                userEntity.setEmail(email.getText().toString());
                userEntity.setPassword(password.getText().toString());

                if(validateInput(userEntity)){
                    // Do insert operation
                    DatabaseClient db = DatabaseClient.getInstance(getApplicationContext());
                    final user dao = db.getAppDatabase().userdao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // Register User
                            dao.registerUser(userEntity);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"User Registered!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class );
                                    startActivityForResult(intent, 2000);
                                }
                            });
                        }
                    }).start();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Fill all fields", Toast.LENGTH_SHORT).show();
                };

            }
        });

    }

    private Boolean validateInput(User userEntity){
        if(userEntity.getFirst_name().isEmpty() ||
                userEntity.getLast_name().isEmpty() ||
                userEntity.getBirth_date() == null ||
                userEntity.getPassword().isEmpty() ||
                userEntity.getEmail().isEmpty()){
            return false;
        }
        return true;
    }
}
