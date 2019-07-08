package com.example.haeseong.projectline1.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.data.UserData;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

//로그인 했을 때
public class UpdateProfileActivity extends AppCompatActivity {
    public static int CAMERA_REQUEST = 100;

    FirebaseUser mFirebaseUser;
    private StorageReference mStorageRef;
    EditText etNickName;
    EditText etSchool;
    EditText etGrade;
    EditText etSex;

    ImageView ivProfile;
    Button btFinish;

    Bitmap img;
    Uri selectedImageUri;
    String imagePath;
    UploadTask uploadTask;
    Uri photoUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        ivProfile = findViewById(R.id.update_profile_image);
        etNickName = findViewById(R.id.update_profile_name);
        etSchool = findViewById(R.id.update_profile_school);
        etGrade = findViewById(R.id.update_profile_grade);
        etSex = findViewById(R.id.update_profile_sex);
        btFinish = findViewById(R.id.update_profile_ok_button);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        ivProfile.setImageURI(mFirebaseUser.getPhotoUrl());
        if(getIntent() != null){
            String name = getIntent().getStringExtra("name");
            String profileImage = getIntent().getStringExtra("image");
//            ivProfile.setImageURI(Uri.parse(profileImage));
            etNickName.setText(name);
        }

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK); //ACTION_PICK
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });
        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputName = etNickName.getText().toString();
                String inputAge = etGrade.getText().toString();
                String inputSchool = etSchool.getText().toString();
                String inputSex = etSex.getText().toString();
                if(mFirebaseUser != null){
                    if(inputName.length() >= 2 && inputName.length() <= 10){
                        //1.firestore에 유저 db 추가
                        createUser_fireStore(inputName,inputAge,inputSchool,inputSex,imagePath);
                    } else{
                        etNickName.getText().clear();
                        Toast.makeText(UpdateProfileActivity.this,"2글자 이상 10글자 이하입니다.",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }//onCreate
    public void upload_storage(){
        StorageReference profileRef = mStorageRef.child("profile_images/"+mFirebaseUser.getUid()).child(mFirebaseUser.getDisplayName()+".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        uploadTask = profileRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(), "사진 업로드 실패", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "사진 업로드가 잘 됐습니다.", Toast.LENGTH_SHORT).show();
                getPhoto();
//                Intent intent = new Intent(UpdateProfileActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
    }

    protected  void createUser_fireStore(String nickName, String grade, String school, String sex, String photo){
    //등록된 정보가 없으면 로그인화면에서 프로필정보 등록화면으로 넘어간다.

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserData userData = new UserData();
        userData.setEmail(firebaseUser.getEmail());
        userData.setNickName(nickName);
        userData.setName(firebaseUser.getDisplayName());
        userData.setGrade(grade);
        userData.setSex(sex);
        userData.setPhoto(String.valueOf(photoUri));
        userData.setSchool(school);
        userData.setComments(null);
        userData.setPosts(null);
        db.collection("users").document(firebaseUser.getUid()).set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UpdateProfileActivity.this,"DB에 회원 등록 성공.",Toast.LENGTH_SHORT).show();
                        setUserPhoto();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfileActivity.this,"DB에 회원 등록 실패",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    protected void setUserPhoto(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(photoUri)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdateProfileActivity.this,"프로필사진 등록되었습니당! ",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateProfileActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
        println("setUser"+photoUri);
    }

    public void getPhoto(){
        final StorageReference profileRef = mStorageRef.child("profile_images/"+mFirebaseUser.getUid()).child(mFirebaseUser.getDisplayName()+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                photoUri = uri;
                println("success"+photoUri);
                ivProfile.setImageURI(photoUri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(UpdateProfileActivity.this,"getUri onFailure",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    img = BitmapFactory.decodeStream(in);
                    in.close();
                    selectedImageUri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imagePath = cursor.getString(columnIndex);
                        Log.d("image", "image_path3: " + imagePath);
                        cursor.close();
                    }
                    // 이미지 표시
                    upload_storage();

                    Log.d("image", "image_path: " + imagePath);
                    Log.d("image", "image_path: " + selectedImageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    void println(String message){
        Log.d("UpdateActivity_print", message);
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}
