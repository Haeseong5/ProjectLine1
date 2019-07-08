package com.example.haeseong.projectline1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.data.UserData;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    String TAG = "LoginActivity";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;
    FirebaseUser mFireBaseUser;

    EditText etEmail;
    EditText etPassword;
    Button btLoginEmail;
    Button btLoginFacebook;
    TextView tvSignUp;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 파이어베이스 인증 객체 선언
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        findView();
        setClickListener();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }//END onCreate();

    protected void setClickListener()
    {
        btLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                println("이메일 로그인");
                emailLogin(etEmail.getText().toString(), etPassword.getText().toString());
                finish();
            }
        });
        btLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookLogin();

            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                println("회원가입하기");
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        btLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    emailLogin(etEmail.getText().toString(), etPassword.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    protected void facebookLogin(){
        //계속 같은 클래스의 메소드를 쓴다면 따로 클래스정의 하기

        // LoginManager - 요청된 읽기 또는 게시 권한으로 로그인 절차를 시작합니다.
        mCallbackManager = CallbackManager.Factory.create(); //로그인 응답을 처리할 콜백 관리자
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
        loginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>()
        { //로그인 결과에 응답하려면 LoginManager 또는 LoginButton에 콜백을 등록해야 합니다.
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("onSuccess", "onSuccess");
                //로그인에 성공하면 LoginResult 매개변수에 새로운 AccessToken과 최근에 부여되거나 거부된 권한이 포함됩니다.
                //로그인 상태 확인
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = (accessToken != null) && (!accessToken.isExpired()); //액세스토큰이 null이 아니고 만료되지 않았다면
                handleFacebookAccessToken(accessToken,isLoggedIn);

            }

            @Override
            public void onCancel() {
                Log.e("onCancel", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("onError", "onError " + exception.getLocalizedMessage());
            }
        });
    }
    private void emailLogin(String email, String password){
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "로그인 실패...", Toast.LENGTH_SHORT).show();
                        } else {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                        }
                    }
                });

    }

    protected void findView()
    {
        etEmail = findViewById(R.id.login_email_edit);
        etPassword = findViewById(R.id.login_password_edit);
        btLoginEmail = findViewById(R.id.login_email_button);
        btLoginFacebook = findViewById(R.id.login_facebook_button);
        tvSignUp = findViewById(R.id.login_signUp);
    }

    void println(String message){
        Log.d(TAG, message);
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
//        마지막으로 onActivityResult 메서드에서 callbackManager.onActivityResult를 호출하여
//        로그인 결과를 callbackManager를 통해 LoginManager에 전달합니다.
    }

    //sharedprfernece 사용하여 인증키 저장하기
    // 페이스북 로그인 이벤트
// 사용자가 정상적으로 로그인한 후 페이스북 로그인 버튼의 onSuccess 콜백 메소드에서 로그인한 사용자의
// 액세스 토큰을 가져와서 Firebase 사용자 인증 정보로 교환하고,
// Firebase 사용자 인증 정보를 사용해 Firebase에 인증.
    private void handleFacebookAccessToken(final AccessToken accessToken, final boolean isLoggedIn) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            if(isLoggedIn == true)
                            {
                                mFireBaseUser = mFirebaseAuth.getCurrentUser();
                                readUser_FireStore();
                                //프로그래스바 추가
                            }
                        } else {
                            println("파이어베이스 로그인 실패");
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if mFirebaseUser is signed in (non-null) and update UI accordingly.
        // 사용자가 로그인 되어있는 지 확인 -> 자동로그인으로 현재 유저 객체 보냄
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        autoLogin(currentUser);
        mFirebaseAuth.addAuthStateListener(mAuthListener);

    }

    protected void autoLogin(FirebaseUser currentUser){
        if (currentUser != null) {
            // User is signed in
            println("자동로그인 되었습니다. "+currentUser.getEmail());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Log.d("currentUser",currentUser.getPhotoUrl()+" "+currentUser.getDisplayName());
            startActivity(intent);
            finish();
        } else {
            // No mFirebaseUser is signed in
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
        //액티비티 애니메이션 x
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }
    protected void readUser_FireStore(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(mFireBaseUser.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserData user = documentSnapshot.toObject(UserData.class);
                if(user!=null){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    println("페이스북 로그인 success");
                    println("파이어베이스 로그인 성공");
                    finish();
                }else{
                    Intent intent = new Intent(LoginActivity.this, UpdateProfileActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
}
