package com.codegama.productivity_booster.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.codegama.productivity_booster.dao.user;
import com.codegama.productivity_booster.database.DatabaseClient;
import com.codegama.productivity_booster.R;
import com.codegama.productivity_booster.model.User;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            Window w =getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setContentView(R.layout.activity_login);

        TextView forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setMovementMethod(LinkMovementMethod.getInstance());

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                // Handle forgot password click
                // For example, show a dialog to reset the password
                // or start a new activity to reset the password
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(getResources().getColor(R.color.colorAccent));
            }
        };

        SpannableString spannableText = new SpannableString(forgot_password.getText());
        int startIndex = 0;
        int endIndex = spannableText.length();

        spannableText.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgot_password.setText(spannableText);


        TextView registerTextView = findViewById(R.id.register_textview);
        registerTextView.setMovementMethod(LinkMovementMethod.getInstance());


        ClickableSpan clickableSpanRegister = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(getResources().getColor(R.color.colorAccent));
            }
        };

        SpannableString spannableRegisterText = new SpannableString(registerTextView.getText());
        int startIndexRegister = spannableRegisterText.toString().indexOf("Register");
        int endIndexRegister = startIndexRegister + "Register".length();

        spannableRegisterText.setSpan(clickableSpanRegister, startIndexRegister, endIndexRegister, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        registerTextView.setText(spannableRegisterText);



        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);





        Button login_btn = (Button) findViewById(R.id.login_btn);



        login_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Fill all Fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Perform Query
                    DatabaseClient db = DatabaseClient.getInstance(getApplicationContext());
                    final user dao = db.getAppDatabase().userdao();
                    new Thread((new Runnable() {
                        @Override
                        public void run() {
                            User userEntity = dao.login(email,password);
                            if(userEntity == null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this,"Invalid Credentials", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                                String name = userEntity.getFirst_name();
                                startActivity(new Intent(LoginActivity.this, TaskActivity.class ).putExtra("name",name));
                            }
                        }
                    })).start();
                }
            }
        });

    }
}
