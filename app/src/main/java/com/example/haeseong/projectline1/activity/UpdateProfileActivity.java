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
import com.example.haeseong.projectline1.data.ApiFuture;
import com.example.haeseong.projectline1.data.UserData;
import com.example.haeseong.projectline1.data.UserWriteResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

public class UpdateProfileActivity extends AppCompatActivity {
    public static int CAMERA_REQUEST = 100;

    FirebaseUser firebaseUser;
    private StorageReference mStorageRef;
    private DatabaseReference myRef;
    EditText etName;
    EditText etSchool;
    EditText etAge;
    ImageView ivProfile;
    Button btFinish;

    Bitmap img;
    Uri selectedImageUri;
    String imagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        ivProfile = findViewById(R.id.update_profile_image);
        etName = findViewById(R.id.update_profile_name);
        etSchool = findViewById(R.id.update_profile_school);
        etAge = findViewById(R.id.update_profile_age);
        btFinish = findViewById(R.id.update_profile_ok_button);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        if(getIntent() != null){
            String name = getIntent().getStringExtra("name");
            String profileImage = getIntent().getStringExtra("image");
            ivProfile.setImageURI(Uri.parse(profileImage));
            etName.setText(name);
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
                String inputName = etName.getText().toString();
                String inputAge = etAge.getText().toString();
                String InputSchool = etSchool.getText().toString();

                if(firebaseUser!= null){
                    writeUserDB(inputName,inputAge,InputSchool);

                }
                if(inputName.length() >= 2 && inputName.length() <= 10){
                    userInfoUpdate(inputName);

                } else{
                    etName.getText().clear();
                    Toast.makeText(UpdateProfileActivity.this,"2글자 이상 10글자 이하입니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }//onCreate

//    protected  void uploadStorage(){
//        myRef.child("userPhoto").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String name = dataSnapshot.child("name").getValue().toString();
//
//                String photo = dataSnapshot.child("photo").getValue().toString();
////                Glide.with(getActivity()).load(photo).into(ivProfileImage);
////
////                if(TextUtils.isEmpty(stPhoto)) {
////                } else {
////                    Glide.with(getActivity()).load(value).into(ivProfileImage);
////                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
////                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
//    }
    protected  void writeUserDB(String inputName, String inputAge, String InputSchool){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(imagePath==null || imagePath == ""){
            imagePath = "https://pbs.twimg.com/media/DFZodX_W0AAcsjZ.jpg";
        }
        UserData userData = new UserData(inputName, firebaseUser.getEmail(), inputAge, InputSchool,imagePath);
        db.collection("users").document(firebaseUser.getUid()).set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UpdateProfileActivity.this,"회원정보가 등록되었습니다.",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfileActivity.this,"회원정보가 등록에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
    protected void userInfoUpdate(final String name){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(Uri.parse(imagePath))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("UpdateProfileActivity: ", "User profile updated.");
                            Toast.makeText(UpdateProfileActivity.this,"정보가 등록되었습니당! "+name,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateProfileActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
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
                    ivProfile.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
