package com.example.haeseong.projectline1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.haeseong.projectline1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    static private String TAG = SignUpActivity.class.getSimpleName();
    EditText etMyEmail;
    EditText etMyName;
    EditText etPassword;
    EditText etMyId;
    EditText etCheckPW;
    Button btSignUpOk;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setView();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//                } else {
//                    // User is signed out
//                    Log.d(TAG, "onAuthStateCha
// nged:signed_out");
//                }
//                // ...
//            }
//        };
        btSignUpOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(!isValidEmail()){
                        println("이메일 형식이 올바르지 않습니다.");
                    }else if(!isValidPassword()){
                        println("비밀번호 형식이 올바르지 않습니다.");
                    }else if(!isEqualPassword()){
                        println("비밀번호가 같지 않습니다.");
                    }else{
                        registerUser(etMyEmail.getText().toString(),etPassword.getText().toString(),etMyName.getText().toString());
                    }
                }catch (Exception E){
                    E.printStackTrace();
                    println("Exception");
                }
            }
        });

    }//end onCreate
    public void registerUser(final String email, final String password,final String name){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            println("회원가입 실패:"+task.isSuccessful());
                            etMyEmail.getText().clear();
                            etMyName.getText().clear();
                            etPassword.getText().clear();
                            etMyId.getText().clear();
                            etCheckPW.getText().clear();
                        }else{
                            println("회원가입 성공:"+task.isSuccessful());
                            saveUser(email,password,name); //db에 저장
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private void saveUser(String email, String pw, String name){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(name);

        Hashtable<String, String> user = new Hashtable<String, String>();
        user.put("email", email);
        user.put("password", pw);
        user.put("name", name);
        user.put("time", formattedDate); //가입시간

        myRef.setValue(user);
    }


    // 이메일 유효성 검사
    private boolean isValidEmail() {
        String email = etMyEmail.getText().toString();
        if (email.isEmpty()) {
            // 이메일 공백
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPassword() {
        String password = etPassword.getText().toString();
        final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$"); //비밀번호 정규식
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            return false;
        } else {
            return true;
        }
    }
    private boolean isEqualPassword(){
        String password = etPassword.getText().toString();
        String checkPw = etCheckPW.getText().toString();
        if(password.equals(checkPw)){
            return true;
        }else{
            return false;
        }
    }


    private void setView(){
        etMyEmail = findViewById(R.id.signup_email);
        etMyName = findViewById(R.id.signup_name);
        etPassword = findViewById(R.id.signup_password);
        btSignUpOk = findViewById(R.id.signup_button);
        etMyId = findViewById(R.id.signup_id);
        etCheckPW = findViewById(R.id.signup_password_check);
    }
    void println(String message){
        Log.d(TAG, message);
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

}//end Class
